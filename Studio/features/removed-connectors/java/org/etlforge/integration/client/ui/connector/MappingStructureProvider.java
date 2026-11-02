package org.etlforge.integration.client.ui.connector;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.application.Application;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.mapping.serverapi.GetMappingStructureCallback;
import org.etlforge.integration.client.mapping.serverapi.transformation.TreeMapperInfoProvider;
import org.etlforge.integration.client.server.ResultProducingServerCallback;
import org.etlforge.integration.client.structure.cache.CacheWriter;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.connector.ConnectorCall;
import org.etlforge.integration.data.structure.XsdBasedStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

@ParametersAreNonnullByDefault
public final class MappingStructureProvider {
   private final InputAndOutputMappingTreeStructureCache cache;
   private final TreeMapperInfoProvider treeMapperInfoProvider;
   private WaitService waitService = WaitService.NONE;

   public MappingStructureProvider(InputAndOutputMappingTreeStructureCache var1, TreeMapperInfoProvider var2) {
      this.cache = Preconditions.checkNotNull(var1, "cache");
      this.treeMapperInfoProvider = Preconditions.checkNotNull(var2, "treeMapperInfoProvider");
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1);
   }

   public void loadStructures(final ConnectorCall var1, final MappingStructureProvider.Callback var2) {
      final WaitLock var3 = this.waitService.startWait();
      Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            MappingStructureProvider.this.loadStructuresImpl(var1, var2, var3);
         }
      });
   }

   private void loadStructuresImpl(ConnectorCall var1, MappingStructureProvider.Callback var2, WaitLock var3) {
      label38: {
         try {
            MappingTreeStructure var4 = this.getCachedInputStructure(var1);
            MappingTreeStructure var5 = this.cache.getOutputStructure(var1.getFunctionID()).orNull();
            if (!this.isDone(var1, var4, var5)) {
               break label38;
            }

            var2.gotStructures(var4, var5);
         } catch (DataCacheException var9) {
            ErrorLog.internal(this.getClass(), null, var9);
            break label38;
         } finally {
            var3.release();
         }

         return;
      }

      this.refreshStructures(var1, var2);
   }

   private MappingTreeStructure getCachedInputStructure(ConnectorCall var1) throws DataCacheException {
      return var1.hasRequestTransformation() ? this.cache.getInputStructure(var1.getFunctionID()).orNull() : null;
   }

   private boolean isDone(ConnectorCall var1, MappingTreeStructure var2, MappingTreeStructure var3) {
      return var3 != null && (var2 != null || !var1.hasRequestTransformation());
   }

   public void refreshStructures(ConnectorCall var1, MappingStructureProvider.Callback var2) {
      MappingStructureProvider.RefreshJob var3 = new MappingStructureProvider.RefreshJob(var1, var2);
      var3.submit(this.waitService, 0);
   }

   public interface Callback {
      void gotStructures(@Nullable MappingTreeStructure var1, MappingTreeStructure var2);
   }

   private class RefreshJob extends UiJob {
      private final ConnectorCall connectorCall;
      private final MappingStructureProvider.Callback externalCallback;

      public RefreshJob(ConnectorCall var2, MappingStructureProvider.Callback var3) {
         super("Creating mapping structures for " + var2.getName());
         this.connectorCall = var2;
         this.externalCallback = var3;
         this.setBlockUntilDone(true);
      }

      @Override
      protected void runImpl() throws InterruptedException {
         try {
            this.setMessage("Creating request structure");
            MappingTreeStructure var1 = null;
            if (this.connectorCall.hasRequestTransformation()) {
               var1 = this.askServerForJtr(this.connectorCall.getInputStructure(), false);
               if (var1 == null) {
                  return;
               }
            }

            MappingTreeStructure var2 = this.askServerForJtr(this.connectorCall.getOutputStructure(), true);
            if (var2 == null) {
               return;
            }

            this.processReturnedStructures(var1, var2);
         } catch (Exception var3) {
            ErrorLog.log(MappingStructureProvider.class, null, var3);
         }
      }

      private MappingTreeStructure askServerForJtr(XsdBasedStructure var1, boolean var2) throws Exception {
         MappingStructureProvider.RefreshJob.InternalCallback var3 = new MappingStructureProvider.RefreshJob.InternalCallback();
         this.setUncaughtExceptionHandler(var3);
         MappingStructureProvider.this.treeMapperInfoProvider.getJtrForStructure(var1.getXsdPath(), var1.getRootName(), var2, var3);
         MappingTreeStructure var4 = var3.awaitResult().orNull();
         return this.isCancelled() ? null : var4;
      }

      private void processReturnedStructures(MappingTreeStructure var1, MappingTreeStructure var2) {
         if (!this.isCancelled()) {
            this.cacheRequestStructure(var1);
            if (!this.isCancelled()) {
               this.cacheResponseStructure(var2);
               if (!this.isCancelled()) {
                  this.externalCallback.gotStructures(var1, var2);
               }
            }
         }
      }

      private void cacheRequestStructure(MappingTreeStructure var1) {
         if (var1 != null) {
            CacheWriter var2 = CacheWriter.newInputWriter();
            this.writeJtrToCache(var1, var2);
         }
      }

      private void writeJtrToCache(MappingTreeStructure var1, CacheWriter var2) {
         var2.setErrorHandler(
            new LoggingExceptionHandler(this.getClass(), ErrorLevel.INTERNAL, "Failed to cache Connector Call structure. This is not a fatal error.")
         );
         var2.addToCache(MappingStructureProvider.this.cache, this.connectorCall.getFunctionID(), var1);
      }

      private void cacheResponseStructure(MappingTreeStructure var1) {
         CacheWriter var2 = CacheWriter.newOutputWriter();
         this.writeJtrToCache(var1, var2);
      }

      private class InternalCallback extends ResultProducingServerCallback<MappingTreeStructure> implements GetMappingStructureCallback {
         private InternalCallback() {
         }

         @Override
         public void jtrGenerated(MappingTreeStructure var1) {
            if (RefreshJob.this.isCancelled()) {
               this.cancelled();
            } else {
               this.set(var1);
            }
         }
      }
   }
}
