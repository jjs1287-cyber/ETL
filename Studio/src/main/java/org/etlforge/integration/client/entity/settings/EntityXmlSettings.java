package org.etlforge.integration.client.entity.settings;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.io.Folder;
import org.etlforge.util.persist.Persistor;
import org.etlforge.util.persist.XmlPersistor;

public final class EntityXmlSettings implements EntitySettings {
   private static final String CLASS_SETTINGS = "ClassSettings";
   private final Folder location;
   private final Map<EntityType, EntityXmlSettings.Files> typeMap = Maps.newHashMap();

   public EntityXmlSettings(Folder var1) {
      ensureLocationExists(var1);
      this.location = var1;
   }

   private static void ensureLocationExists(Folder var0) {
      if (!var0.exists() && !var0.createOnDisk()) {
         ErrorLog.log(EntityXmlSettings.class, null, new Exception("Failed to create the settings location " + var0.getAbsolutePath()));
      }
   }

   @Override
   public synchronized Persistor getClassSettings(EntityType var1) {
      XmlPersistor var2 = this.getRoot(var1);
      Persistor var3 = var2.getFirstChild("ClassSettings");
      if (var3 == null) {
         var3 = var2.createChild("ClassSettings");
      }

      return var3;
   }

   private XmlPersistor getRoot(EntityType var1) {
      EntityXmlSettings.Files var2 = this.typeMap.get(var1);
      if (var2 == null) {
         var2 = new EntityXmlSettings.Files(var1, this.location);
         this.typeMap.put(var1, var2);
      }

      return var2.getRoot();
   }

   @Override
   public synchronized Persistor getEntitySettings(IntegrationEntity var1) {
      XmlPersistor var2 = this.getRoot(var1.getEntityType());
      String var3 = getNameOfEntityPersistor(var1);
      Persistor var4 = var2.getFirstChild(var3);
      if (var4 == null) {
         var4 = var2.createChild(var3);
      }

      return var4;
   }

   private static String getNameOfEntityPersistor(IntegrationEntity var0) {
      return "ID-" + var0.getID().toString();
   }

   @Override
   public synchronized void deleteSettings(IntegrationEntity var1) {
      XmlPersistor var2 = this.getRoot(var1.getEntityType());
      var2.removeChildren(getNameOfEntityPersistor(var1));
   }

   @Override
   public synchronized void persist() {
      for (EntityXmlSettings.Files var2 : this.typeMap.values()) {
         var2.persist();
      }
   }

   private static final class Files {
      private final EntityType type;
      private final File xmlFile;
      private XmlPersistor root;

      public Files(EntityType var1, Folder var2) {
         this.type = var1;
         Folder var3 = var2.subFolder(var1.name());
         this.xmlFile = var3.getFile(var1.name() + ".xml");
      }

      public XmlPersistor getRoot() {
         if (this.root == null) {
            this.root = this.createRootPersistor();
         }

         return this.root;
      }

      private XmlPersistor createRootPersistor() {
         XmlPersistor var1 = null;
         if (this.xmlFile.exists()) {
            try {
               var1 = XmlPersistor.load(this.xmlFile);
            } catch (Exception var3) {
               ErrorLog.log(EntityXmlSettings.class, "Failed to load settings from XML file", var3);
            }
         }

         if (var1 == null) {
            var1 = XmlPersistor.newRoot(this.type.name());
         }

         return var1;
      }

      private void persist() {
         if (this.root != null) {
            try {
               File var1 = this.xmlFile.getParentFile();
               if (var1.exists() || var1.mkdir()) {
                  this.root.store(this.xmlFile);
               }
            } catch (IOException var2) {
               ErrorLog.log(EntityXmlSettings.class, "Failed to persist entity settings to the file " + this.xmlFile, var2);
            }
         }
      }
   }
}
