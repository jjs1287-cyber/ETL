package org.etlforge.integration.client.plugin.pipeline.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.plugin.pipeline.xml.XmlToManifestTranslator;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginInfoProvider;
import org.etlforge.integration.client.plugin.serverapi.PipelinePluginManifestDownloadCallback;
import org.etlforge.integration.client.plugin.serverapi.PluginAsXml;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.plugin.PluginSpecificationException;
import org.etlforge.integration.plugin.pipeline.PipelinePluginManifest;
import org.etlforge.lang.exception.ExceptionHandler;

public final class PipelinePluginCacheUpdateCall {
   private final PipelinePluginManifestServerCache serverCache;
   private final PipelinePluginInfoProvider serverApi;
   private ExceptionHandler<Throwable> errorHandler;

   public PipelinePluginCacheUpdateCall(PipelinePluginManifestServerCache var1, PipelinePluginInfoProvider var2) {
      Preconditions.checkNotNull(var1, "serverCache");
      Preconditions.checkNotNull(var2, "serverApi");
      this.serverCache = var1;
      this.serverApi = var2;
      this.errorHandler = new PipelinePluginCacheUpdateCall.DefaultServerErrorHandler();
   }

   public final void setErrorHandler(ExceptionHandler<Throwable> var1) {
      this.errorHandler = var1;
   }

   public void call() {
      PipelinePluginCacheUpdateCall.CallbackImpl var1 = new PipelinePluginCacheUpdateCall.CallbackImpl();
      this.serverApi.downloadPluginManifests(var1);
   }

   private final class CallbackImpl implements PipelinePluginManifestDownloadCallback {
      private CallbackImpl() {
      }

      @Override
      public void pluginInfoDownloaded(ServerInfo var1, PluginAsXml[] var2) {
         this.clearCurrentCache(var1);
         this.addNewManifests(var1, var2);
      }

      private void clearCurrentCache(ServerInfo var1) {
         PipelinePluginManifestCache var2 = PipelinePluginCacheUpdateCall.this.serverCache.getCache(var1.getGuid());
         if (var2 != null) {
            var2.removeAll();
         }
      }

      private void addNewManifests(ServerInfo var1, PluginAsXml[] var2) {
         ArrayList var3 = Lists.newArrayList();

         for (PluginAsXml var7 : var2) {
            for (PipelinePluginManifest var9 : this.parseManifests(var7)) {
               var3.add(var9);
            }
         }

         try {
            PipelinePluginCacheUpdateCall.this.serverCache.addManifests(var1, var3);
         } catch (DataCacheException var10) {
            var10.printStackTrace();
         }
      }

      private Iterable<PipelinePluginManifest> parseManifests(PluginAsXml var1) {
         XmlToManifestTranslator var2 = new XmlToManifestTranslator();
         ArrayList var3 = new ArrayList();

         try {
            KongaListUtils.add(var3, var2.translate(var1));
         } catch (PluginSpecificationException var5) {
            ErrorLog.log(this.getClass(), "An error occurred while parsing a plugin manifest.", var5);
         }

         return var3;
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void caught(Throwable var1) {
         PipelinePluginCacheUpdateCall.this.errorHandler.caught(var1);
      }
   }

   private static final class DefaultServerErrorHandler implements ExceptionHandler<Throwable> {
      private DefaultServerErrorHandler() {
      }

      @Override
      public void caught(Throwable var1) {
         String var2 = "An error occurred when updating the local pipeline plugin cache: " + var1.getMessage();
         ErrorLog.attention(this, var2, var1);
      }
   }
}
