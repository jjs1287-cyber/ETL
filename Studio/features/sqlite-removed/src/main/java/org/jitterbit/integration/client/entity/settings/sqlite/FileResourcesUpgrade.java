package org.jitterbit.integration.client.entity.settings.sqlite;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.jitterbit.application.errorlog.ErrorLog;
import org.jitterbit.integration.data.entity.EntityType;
import org.jitterbit.integration.data.entity.IntegrationEntityId;
import org.jitterbit.io.Folder;
import org.jitterbit.io.KongaIoUtils;
import org.jitterbit.lang.KongaStringUtils;

public final class FileResourcesUpgrade {
   private final Folder oldSettingsFolder;
   private final SQLiteEntityResources target;
   private boolean deleteOriginalFiles;

   public FileResourcesUpgrade(Folder var1, SQLiteEntityResources var2) {
      this.oldSettingsFolder = var1;
      this.target = var2;
   }

   public void setDeleteOriginalFiles(boolean var1) {
      this.deleteOriginalFiles = var1;
   }

   public void runUpgrade() {
      for (EntityType var4 : EntityType.values()) {
         if (var4 == EntityType.Operation) {
            Folder var5 = new Folder(this.oldSettingsFolder, var4.name(), "Resources");
            if (var5.exists()) {
               this.upgradeEntityType(var4, var5);
            }
         }
      }
   }

   private void upgradeEntityType(EntityType var1, Folder var2) {
      FileResourcesUpgrade.TypeUpgrader var3 = new FileResourcesUpgrade.TypeUpgrader(var1, var2, this.target);
      var3.runUpgrade();
      if (this.deleteOriginalFiles) {
         var2.delete();
      }
   }

   private static class TypeUpgrader {
      private final EntityType type;
      private final Folder root;
      private final SQLiteEntityResources target;

      public TypeUpgrader(EntityType var1, Folder var2, SQLiteEntityResources var3) {
         this.type = var1;
         this.root = var2;
         this.target = var3;
      }

      public void runUpgrade() {
         for (Folder var4 : this.root.listSubFolders()) {
            try {
               IntegrationEntityId var5 = this.type.getIntegrationId(var4.getName());
               this.processEntity(var4, var5);
            } catch (Exception var6) {
               var6.printStackTrace();
            }
         }
      }

      private void processEntity(Folder var1, IntegrationEntityId var2) {
         for (File var6 : var1.listNonFolders()) {
            try {
               String var7 = this.readFile(var6);
               if (var7 != null) {
                  this.target.importFileResource(var2, var6.getName(), var7);
               }
            } catch (IOException var9) {
               String var8 = String.format("Failed to import the file resource %s for the %s with GUID = %s", var6.getName(), this.type.getDisplayName(), var2);
               ErrorLog.log(this.getClass(), var8, var9);
            }
         }
      }

      private String readFile(File var1) throws IOException {
         if (var1.getName().toLowerCase().startsWith(".konga.")) {
            return null;
         } else if (!var1.canRead()) {
            return null;
         } else if (var1.length() == 0L) {
            return "";
         } else if (isSerializedObject(var1)) {
            byte[] var2 = Files.toByteArray(var1);
            return KongaStringUtils.toHexString(var2);
         } else {
            return Files.toString(var1, Charsets.UTF_8);
         }
      }

      private static boolean isSerializedObject(File var0) throws IOException {
         if (var0.length() < 4L) {
            return false;
         }

         DataInputStream var1 = new DataInputStream(new FileInputStream(var0));

         try {
            return var1.readShort() == -21267 && var1.readShort() == 5;
         } finally {
            KongaIoUtils.close(var1);
         }
      }
   }
}
