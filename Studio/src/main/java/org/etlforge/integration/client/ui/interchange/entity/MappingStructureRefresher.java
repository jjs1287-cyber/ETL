package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.etlforge.application.Application;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobProgressMonitor;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.mapping.MappingStructureFactory;
import org.etlforge.integration.client.structure.cache.CacheWriter;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;
import org.etlforge.lang.exception.ExceptionHandler;

public final class MappingStructureRefresher {
   private final MappingStructureFactory structureFactory;
   private InputAndOutputMappingTreeStructureCache cache = InputAndOutputMappingTreeStructureCache.EMPTY;
   private final List<UiJobProgressMonitor> progressMonitors = Lists.newCopyOnWriteArrayList();
   private Logger logger = Logger.getAnonymousLogger();

   public MappingStructureRefresher(MappingStructureFactory var1) {
      this.structureFactory = Preconditions.checkNotNull(var1);
      this.logger.setLevel(Level.OFF);
   }

   public void setCache(InputAndOutputMappingTreeStructureCache var1) {
      this.cache = Preconditions.checkNotNull(var1);
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   @Nullable
   public MappingTreeStructure refreshInputStructure(Transformation var1) throws MappingTreeStructureException {
      MappingTreeStructure var2 = MappingStructureRefresher.RefreshStructuresJob.refreshInput(var1, this.structureFactory, this.progressMonitors, this.logger);
      if (var2 != null) {
         var1.setInputMappingStructure(var2);
         var1.setDeployDirty(true);
         this.cacheInputStructure(var1, var2);
      }

      return var2;
   }

   @Nullable
   public MappingTreeStructure refreshOutputStructure(Transformation var1) throws MappingTreeStructureException {
      MappingTreeStructure var2 = MappingStructureRefresher.RefreshStructuresJob.refreshOutput(var1, this.structureFactory, this.progressMonitors, this.logger);
      if (var2 != null) {
         var1.setOutputMappingStructure(var2);
         var1.setDeployDirty(true);
         this.cacheOutputStructure(var1, var2);
      }

      return var2;
   }

   @Nullable
   public MappingTreeStructure[] refreshInputAndOuputStructures(Transformation var1) throws MappingTreeStructureException {
      MappingTreeStructure[] var2 = MappingStructureRefresher.RefreshStructuresJob.refreshTransformation(
         var1, this.structureFactory, this.progressMonitors, this.logger
      );
      if (var2 != null) {
         this.logSuccess(var2);
         var1.setMappingStructures(var2[0], var2[1]);
         var1.setDeployDirty(true);
         this.cacheInputStructure(var1, var2[0]);
         this.cacheOutputStructure(var1, var2[1]);
      } else {
         this.log("The mapping structures were not refreshed. This probably means that an error occurred.");
      }

      return var2;
   }

   private void logSuccess(MappingTreeStructure[] var1) {
      if (this.logger.isLoggable(Level.INFO)) {
         this.log("New mapping structures have been generated.");
         this.log("Input:\n" + var1[0].getString());
         this.log("Output:\n" + var1[1].getString());
      }
   }

   private void log(String var1) {
      this.logger.info(var1);
   }

   public void addProgressMonitor(UiJobProgressMonitor var1) {
      this.progressMonitors.add(var1);
   }

   public void removeProgressMonitor(UiJobProgressMonitor var1) {
      this.progressMonitors.remove(var1);
   }

   private void cacheInputStructure(Transformation var1, MappingTreeStructure var2) {
      CacheWriter var3 = CacheWriter.newInputWriter();
      this.cacheStructure(var1, var2, var3);
   }

   private void cacheOutputStructure(Transformation var1, MappingTreeStructure var2) {
      CacheWriter var3 = CacheWriter.newOutputWriter();
      this.cacheStructure(var1, var2, var3);
   }

   private void cacheStructure(Transformation var1, MappingTreeStructure var2, CacheWriter var3) {
      var3.setErrorHandler(
         new LoggingExceptionHandler(
            MappingStructureRefresher.class, "Failed to update the mapping structure cache for the transformation with ID = " + var1.getID()
         )
      );
      var3.addToCache(this.cache, var1.getID(), var2);
   }

   private static final class RefreshStructuresJob extends UiJob {
      private final Transformation tx;
      private final MappingStructureFactory structureFactory;
      private MappingTreeStructure input;
      private MappingTreeStructure output;
      private final Logger logger;
      private final CountDownLatch latch = new CountDownLatch(1);
      private final boolean doInput;
      private final boolean doOutput;
      private volatile boolean stop;
      private MappingTreeStructureException error;

      private RefreshStructuresJob(Transformation var1, MappingStructureFactory var2, boolean var3, boolean var4, Logger var5) {
         super("Refreshing the mapping structures for the transformation \"" + var1.getName() + "\"");
         assert var3 || var4;
         this.structureFactory = var2;
         this.tx = var1;
         this.doInput = var3;
         this.doOutput = var4;
         this.logger = var5;
      }

      public static MappingTreeStructure refreshInput(Transformation var0, MappingStructureFactory var1, List<UiJobProgressMonitor> var2, Logger var3) throws MappingTreeStructureException {
         MappingStructureRefresher.RefreshStructuresJob var4 = new MappingStructureRefresher.RefreshStructuresJob(var0, var1, true, false, var3);

         for (UiJobProgressMonitor var6 : var2) {
            var4.addProgressMonitor(var6);
         }

         var4.submit();
         return var4.getInputStructure();
      }

      public static MappingTreeStructure refreshOutput(Transformation var0, MappingStructureFactory var1, List<UiJobProgressMonitor> var2, Logger var3) throws MappingTreeStructureException {
         MappingStructureRefresher.RefreshStructuresJob var4 = new MappingStructureRefresher.RefreshStructuresJob(var0, var1, false, true, var3);

         for (UiJobProgressMonitor var6 : var2) {
            var4.addProgressMonitor(var6);
         }

         var4.submit();
         return var4.getOutputStructure();
      }

      public static MappingTreeStructure[] refreshTransformation(
         Transformation var0, MappingStructureFactory var1, List<UiJobProgressMonitor> var2, Logger var3
      ) throws MappingTreeStructureException {
         MappingStructureRefresher.RefreshStructuresJob var4 = new MappingStructureRefresher.RefreshStructuresJob(var0, var1, true, true, var3);

         for (UiJobProgressMonitor var6 : var2) {
            var4.addProgressMonitor(var6);
         }

         var4.submit();
         MappingTreeStructure var7 = var4.getInputStructure();
         MappingTreeStructure var8 = var4.getOutputStructure();
         return var7 != null && var8 != null ? new MappingTreeStructure[]{var7, var8} : null;
      }

      @Override
      protected void runImpl() throws InterruptedException {
         ApplicationWorker.SafeRunnable var1 = new ApplicationWorker.SafeRunnable() {
            @Override
            public void run() {
               try {
                  if (RefreshStructuresJob.this.isCancelled()) {
                     return;
                  }

                  if (RefreshStructuresJob.this.doInput) {
                     RefreshStructuresJob.this.createInputStructure();
                  }

                  if (!RefreshStructuresJob.this.isCancelled() && !RefreshStructuresJob.this.stop) {
                     if (RefreshStructuresJob.this.doOutput) {
                        RefreshStructuresJob.this.createOutputStructure();
                     }

                     RefreshStructuresJob.this.setMessage("Done");
                     return;
                  }
               } catch (MappingTreeStructureException var6) {
                  RefreshStructuresJob.this.error = var6;
                  RefreshStructuresJob.this.logError(var6);
                  return;
               } catch (Exception var7) {
                  RefreshStructuresJob.this.logError(var7);
                  return;
               } finally {
                  RefreshStructuresJob.this.latch.countDown();
               }
            }
         };
         Application.getWorker().submitForParallelAndWait(var1, new ExceptionHandler<Throwable>() {
            @Override
            public void caught(Throwable var1) {
               RefreshStructuresJob.this.logError(var1);
               RefreshStructuresJob.this.latch.countDown();
            }
         });
      }

      private void log(String var1) {
         if (this.logger != null) {
            this.logger.info(var1);
         }
      }

      @Override
      protected void onJobCancelled() {
         this.latch.countDown();
      }

      private void createInputStructure() throws MappingTreeStructureException {
         String var1 = "Refreshing the input structure";
         this.log(var1);
         this.setMessage(var1);
         this.input = this.structureFactory.getInputStructure(this.tx);
         if (this.input == null) {
            this.cancelProcess();
         }
      }

      private void cancelProcess() {
         this.log("The refresh job was cancelled.");
         super.cancel();
         this.stop = true;
      }

      private void createOutputStructure() throws MappingTreeStructureException {
         String var1 = "Refreshing the output structure";
         this.log(var1);
         this.setMessage(var1);
         this.output = this.structureFactory.getOutputStructure(this.tx);
         if (this.output == null) {
            this.cancelProcess();
         }
      }

      private MappingTreeStructure getInputStructure() throws MappingTreeStructureException {
         try {
            this.latch.await();
            if (this.error != null) {
               throw this.error;
            } else {
               return this.input;
            }
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            return null;
         }
      }

      private MappingTreeStructure getOutputStructure() throws MappingTreeStructureException {
         try {
            this.latch.await();
            if (this.error != null) {
               throw this.error;
            } else {
               return this.output;
            }
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            return null;
         }
      }

      private void logError(Object var1) {
         if (!this.isCancelled()) {
            Throwable var2;
            if (var1 instanceof Throwable) {
               var2 = (Throwable)var1;
            } else if (var1 != null) {
               var2 = new Exception(var1.toString());
            } else {
               var2 = new Exception("An unknown error occurred");
            }

            ErrorLog.attention(MappingStructureRefresher.class, "Failed to refresh the transformation \"" + this.tx.getName() + "\"", var2);
         }
      }
   }
}
