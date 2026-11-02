package org.jitterbit.integration.client.entity.settings.sqlite;

import java.io.File;
import org.jitterbit.application.errorlog.ErrorLog;
import org.jitterbit.integration.client.entity.settings.EntitySettings;
import org.jitterbit.integration.data.entity.EntityType;
import org.jitterbit.integration.data.entity.IntegrationEntityId;
import org.jitterbit.io.Folder;
import org.jitterbit.util.persist.Persistor;
import org.jitterbit.xml.Elements;
import org.jitterbit.xml.KongaXmlUtils;
import org.jitterbit.xml.dom.DomParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public final class EntitySettingsUpgrade {
   private final Folder oldSettingsFolder;
   private final SQLiteEntitySettings target;
   private boolean deleteOriginalFiles;

   public EntitySettingsUpgrade(Folder var1, SQLiteEntityResources var2) {
      this.oldSettingsFolder = var1;
      EntitySettings var3 = var2.getSettings();
      this.target = var3 instanceof SQLiteEntitySettings ? (SQLiteEntitySettings)var3 : null;
   }

   public void setDeleteOriginalFiles(boolean var1) {
      this.deleteOriginalFiles = var1;
   }

   public void runUpgrade() {
      if (this.target != null) {
         for (EntityType var4 : EntityType.values()) {
            this.upgradeTypeSettings(var4);
         }
      }
   }

   private void upgradeTypeSettings(EntityType var1) {
      Folder var2 = this.oldSettingsFolder.subFolder(var1.name());
      if (var2.exists()) {
         File var3 = var2.getFile(var1.name() + ".xml");
         if (var3.canRead()) {
            EntitySettingsUpgrade.TypeUpgrader var4 = new EntitySettingsUpgrade.TypeUpgrader(var1, var3, this.target, this.deleteOriginalFiles);
            var4.doUpgrade();
         }
      }
   }

   private static class TypeUpgrader {
      private final EntityType type;
      private final File xmlFile;
      private final SQLiteEntitySettings target;
      private final boolean deleteWhenDone;

      public TypeUpgrader(EntityType var1, File var2, SQLiteEntitySettings var3, boolean var4) {
         this.type = var1;
         this.xmlFile = var2;
         this.target = var3;
         this.deleteWhenDone = var4;
      }

      public void doUpgrade() {
         try {
            this.migrateSettings();
            this.deleteFile();
         } catch (Exception var2) {
            ErrorLog.log(this.getClass(), "Could not upgrade the Entity Settings stored in the file " + this.xmlFile.getAbsolutePath(), var2);
         }
      }

      private void migrateSettings() throws Exception {
         Document var1 = DomParser.parseFile(this.xmlFile);

         for (Element var3 : Elements.under(var1.getDocumentElement())) {
            String var4 = var3.getNodeName();
            if ("ClassSettings".equals(var4)) {
               this.migrateTypeSettings(var3);
            } else if (var4.startsWith("ID-")) {
               this.migrateSettingsForEntity(var3, var4.substring(3));
            }
         }
      }

      private void migrateTypeSettings(Element var1) {
         this.migrateSettings(var1, this.target.getClassSettings(this.type));
      }

      private void migrateSettings(Element var1, Persistor var2) {
         NamedNodeMap var3 = var1.getAttributes();
         int var4 = 0;

         for (int var5 = var3.getLength(); var4 < var5; var4++) {
            Attr var6 = (Attr)var3.item(var4);
            String var7 = var6.getName();
            String var8 = var6.getValue();
            var2.putString(var7, var8);
         }

         for (Element var10 : Elements.under(var1)) {
            String var11 = var10.getNodeName();
            if ("konga.string".equals(var11)) {
               String var12 = var10.getAttribute("name");
               String var14 = KongaXmlUtils.getElementValue(var10);
               var2.putString(var12, var14);
            } else {
               Persistor var13 = var2.createChild(var11);
               this.migrateSettings(var10, var13);
            }
         }
      }

      private void migrateSettingsForEntity(Element var1, String var2) {
         IntegrationEntityId var3 = this.type.getIntegrationId(var2);
         this.migrateSettings(var1, this.target.getEntitySettings(var3));
      }

      private void deleteFile() {
         if (this.deleteWhenDone) {
            this.xmlFile.delete();
         }
      }
   }
}
