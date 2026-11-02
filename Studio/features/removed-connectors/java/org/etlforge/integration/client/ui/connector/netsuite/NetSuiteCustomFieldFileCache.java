package org.etlforge.integration.client.ui.connector.netsuite;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;
import org.etlforge.io.Folder;
import org.etlforge.util.KongaSerializationUtils;

final class NetSuiteCustomFieldFileCache implements NetSuiteCustomFieldCache {
   private final Folder root;
   private final Object lock = new Object();

   public NetSuiteCustomFieldFileCache(Folder var1) {
      this.root = Preconditions.checkNotNull(var1);
   }

   @Override
   public Optional<NetSuiteObjectCustomFields> getCache(NetSuiteCustomFieldCache.NetSuiteCacheInput var1) {
      synchronized (this.lock) {
         File var3;
         var3 = this.getCacheFile(var1);
         label24:
         if (var3.canRead()) {
            Optional var10000;
            try {
               NetSuiteObjectCustomFields var4 = KongaSerializationUtils.deserializeFromFile(var3, NetSuiteObjectCustomFields.class);
               var10000 = Optional.of(var4);
            } catch (Exception var6) {
               ErrorLog.internal(this.getClass(), "Failed to restore NetSuite custom fields from the local cache." + var3.getAbsolutePath(), var6);
               break label24;
            }

            return var10000;
         }

         this.evictCache(var3);
      }

      return Optional.absent();
   }

   private File getCacheFile(NetSuiteCustomFieldCache.NetSuiteCacheInput var1) {
      ConnectorObjectCache.Key var2 = var1.getKey();
      String var3 = var2.toString();
      NetSuiteWsdlVersion var4 = var1.getNetSuiteEndpoint().getNetSuiteWsdlDownloadUrl().getNetSuiteWsdlVersion();
      String var5 = var4 == null ? "" : var4.toWsdlVersion();
      String var6 = "nscf" + var5 + var3 + NetSuiteCacheVersion.getVersionExtension();
      return this.root.getFile(var6);
   }

   @Override
   public void storeCache(NetSuiteCustomFieldCache.NetSuiteCacheInput var1, NetSuiteObjectCustomFields var2) {
      Preconditions.checkNotNull(var2);
      synchronized (this.lock) {
         File var4 = this.getCacheFile(var1);

         try {
            KongaSerializationUtils.serializeToFile(var2, var4);
         } catch (IOException var7) {
            ErrorLog.internal(this.getClass(), "Failed to store NetSuite custom fields in the local cache." + var4.getAbsolutePath(), var7);
            this.evictCache(var4);
            return;
         }
      }
   }

   @Override
   public void evictCache(NetSuiteCustomFieldCache.NetSuiteCacheInput var1) {
      synchronized (this.lock) {
         File var3 = this.getCacheFile(var1);
         this.evictCache(var3);
      }
   }

   private void evictCache(File var1) {
      if (var1.exists()) {
         var1.delete();
      }
   }
}
