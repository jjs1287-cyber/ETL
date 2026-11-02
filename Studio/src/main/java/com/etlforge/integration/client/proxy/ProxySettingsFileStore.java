package com.etlforge.integration.client.proxy;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import org.etlforge.io.Folder;
import org.etlforge.util.net.ProxySettings;

public final class ProxySettingsFileStore implements ProxySettingsStore {
   private final Folder dir;
   private ProxySettingsPropertiesStore propsStore;
   private final Object lock = new Object();

   public ProxySettingsFileStore(Folder var1) {
      this.dir = Preconditions.checkNotNull(var1);
   }

   private File getFile() {
      return this.dir.getFile("dataloader.proxysettings.properties");
   }

   @Override
   public ProxySettings get() {
      synchronized (this.lock) {
         if (this.propsStore == null) {
            this.initPropsStore();
            File var2 = this.getFile();
            if (var2.exists() && var2.canRead() && var2.length() > 0L) {
               try {
                  this.propsStore.readFromFile(var2);
               } catch (IOException var5) {
                  var5.printStackTrace();
                  return ProxySettings.NONE;
               }
            }
         }

         return this.propsStore.get();
      }
   }

   private void initPropsStore() {
      if (this.propsStore == null) {
         this.propsStore = new ProxySettingsPropertiesStore();
         this.propsStore.setPwdCrypto(new PasswordCrypto());
      }
   }

   @Override
   public boolean put(ProxySettings var1) {
      synchronized (this.lock) {
         boolean var3 = false;
         this.initPropsStore();

         try {
            var3 = this.propsStore.put(var1);
            if (var3) {
               this.propsStore.writeToFile(this.getFile());
            }
         } catch (IOException var6) {
            var6.printStackTrace();
         }

         return var3;
      }
   }
}
