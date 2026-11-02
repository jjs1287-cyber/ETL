package org.etlforge.integration.client.ui.connector.mscrm;

import com.google.common.base.Optional;
import java.io.File;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.ui.connector.ConnectorObjectCache;
import org.etlforge.integration.data.connector.MSCrmEndpoint;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.io.Folder;
import org.etlforge.util.KongaSerializationUtils;

final class DetailedMetaDataCache {
   private final Folder root;
   private final Object lock = new Object();

   public DetailedMetaDataCache(Folder var1) {
      this.root = var1;
   }

   public Optional<MSCrmObject> getMetaData(MSCrmEndpoint var1, String var2) {
      Folder var3 = this.getLocationForEndpoint(var1);
      synchronized (this.lock) {
         if (var3.exists()) {
            File var5 = var3.getFile(var2);
            if (var5.exists() && var5.canRead()) {
               Optional var10000;
               try {
                  MSCrmObject var6 = KongaSerializationUtils.deserializeFromFile(var5, MSCrmObject.class);
                  var10000 = Optional.of(var6);
               } catch (Exception var8) {
                  ErrorLog.internal(this.getClass(), "Failed to deserialize cached MSCRM object metadata.", var8);
                  return Optional.absent();
               }

               return var10000;
            }
         }
      }

      return Optional.absent();
   }

   private Folder getLocationForEndpoint(MSCrmEndpoint var1) {
      ConnectorObjectCache.Key var2 = new ConnectorObjectCache.Key(var1);
      return this.root.subFolder(var2.toString());
   }

   public void storeMetaData(MSCrmEndpoint var1, MSCrmObject var2) {
      Folder var3 = this.getLocationForEndpoint(var1);
      File var4 = var3.getFile(var2.getName());

      try {
         synchronized (this.lock) {
            var3.createOnDisk();
            KongaSerializationUtils.serializeToFile(var2, var4);
         }
      } catch (Exception var8) {
         ErrorLog.internal(this.getClass(), "Failed to serialize MSCRM object metadata to local cache.", var8);
      }
   }

   public void evict(Iterable<String> var1) {
      synchronized (this.lock) {
         for (String var4 : var1) {
            Folder var5 = this.root.subFolder(var4);
            var5.delete();
         }
      }
   }
}
