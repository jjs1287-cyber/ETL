package org.etlforge.integration.client.ui.connector.netsuite;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.design.netsuite.NetSuiteWsdlDownloadUrl;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.io.Folder;
import org.etlforge.util.KongaSerializationUtils;
import org.etlforge.util.file.InvalidCharactersReplacer;

final class NetSuiteDefinitionDesignFileCache implements NetSuiteDefinitionDesignCache {
   private final NetSuiteDefinitionDesignMemoryCache memoryCache;
   private final Folder root;
   private final Object lock = new Object();

   public NetSuiteDefinitionDesignFileCache(Folder var1) {
      this.root = Preconditions.checkNotNull(var1);
      this.memoryCache = new NetSuiteDefinitionDesignMemoryCache(4);
   }

   @Override
   public Optional<NetSuiteDefinitionDesign> getNetSuiteDefinitionDesign(NetSuiteEndpoint var1) {
      File var2 = this.getCacheFile(var1);
      synchronized (this.lock) {
         Optional var4 = this.memoryCache.getNetSuiteDefinitionDesign(var1);
         if (var4.isPresent()) {
            return var4;
         }

         label29:
         if (var2.canRead()) {
            Optional var10000;
            try {
               NetSuiteDefinitionDesign var5 = KongaSerializationUtils.deserializeFromFile(var2, NetSuiteDefinitionDesign.class);
               var10000 = Optional.of(var5);
            } catch (Exception var7) {
               ErrorLog.internal(this.getClass(), "Failed to restore NetSuite definitions from the local cache. " + var2.getAbsolutePath(), var7);
               break label29;
            }

            return var10000;
         }

         this.evictCache(var2);
      }

      return Optional.absent();
   }

   private File getCacheFile(NetSuiteEndpoint var1) {
      Preconditions.checkNotNull(var1);
      return this.root.getFile(getFileName(var1));
   }

   static String getFileName(NetSuiteEndpoint var0) {
      NetSuiteWsdlDownloadUrl var1 = var0.getNetSuiteWsdlDownloadUrl();
      String var2 = var1.getKey();
      InvalidCharactersReplacer var3 = new InvalidCharactersReplacer("_");
      var2 = var3.replace(var2);
      return "nsdesigndef" + var2 + NetSuiteCacheVersion.getVersionExtension();
   }

   @Override
   public void storeNetSuiteDefinitionDesign(NetSuiteEndpoint var1, NetSuiteDefinitionDesign var2) {
      Preconditions.checkNotNull(var2);
      File var3 = this.getCacheFile(var1);
      synchronized (this.lock) {
         try {
            this.memoryCache.storeNetSuiteDefinitionDesign(var1, var2);
            KongaSerializationUtils.serializeToFile(var2, var3);
         } catch (IOException var7) {
            ErrorLog.internal(this.getClass(), "Failed to store NetSuite definitions in the local cache." + var3.getAbsolutePath(), var7);
            this.evictCache(var3);
            return;
         }
      }
   }

   public void evict(ConnectorObjectCache.Key var1) {
      synchronized (this.lock) {
         if (var1 != null) {
            ConnectorEndpoint var3 = var1.getConnectorEndpoint();
            if (var3 != null) {
               if (var3 instanceof NetSuiteEndpoint) {
                  NetSuiteEndpoint var4 = (NetSuiteEndpoint)var3;
                  File var5 = this.getCacheFile(var4);
                  this.evictCache(var5);
               }
            }
         }
      }
   }

   private void evictCache(File var1) {
      this.memoryCache.evictCache(var1);
      if (var1.exists()) {
         var1.delete();
      }
   }
}
