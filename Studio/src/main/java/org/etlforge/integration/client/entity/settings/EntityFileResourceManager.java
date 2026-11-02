package org.etlforge.integration.client.entity.settings;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.io.Folder;

public final class EntityFileResourceManager {
   private final Folder location;
   private final Object lock = new Object();
   private final Map<EntityType, Map<IntegrationEntity, EntityFileResourceManager.EntityRoot>> typeRoots = Maps.newHashMap();

   public EntityFileResourceManager(Folder var1) {
      this.location = var1;
   }

   public FileResource getResource(IntegrationEntity var1, String var2) {
      Preconditions.checkNotNull(var1, "entity");
      Preconditions.checkArgument(!StringUtils.isBlank(var2), "name may not be null or blank");
      Preconditions.checkArgument(!var2.startsWith(".konga."), "name may not start with \".konga.\"");
      EntityFileResourceManager.EntityRoot var3 = this.getEntityRoot(var1);
      return var3.getResource(var2);
   }

   private EntityFileResourceManager.EntityRoot getEntityRoot(IntegrationEntity var1) {
      EntityType var2 = var1.getEntityType();
      synchronized (this.lock) {
         Map var4 = this.getTypeRoot(var2);
         EntityFileResourceManager.EntityRoot var5 = (EntityFileResourceManager.EntityRoot)var4.get(var1);
         if (var5 == null) {
            var5 = new EntityFileResourceManager.EntityRoot(var1, this.getEntityLocation(var1));
            var4.put(var1, var5);
         }

         return var5;
      }
   }

   private Map<IntegrationEntity, EntityFileResourceManager.EntityRoot> getTypeRoot(EntityType var1) {
      Map var2 = this.typeRoots.get(var1);
      if (var2 == null) {
         var2 = this.createTypeRoot(var1);
         this.typeRoots.put(var1, var2);
      }

      return var2;
   }

   private Map<IntegrationEntity, EntityFileResourceManager.EntityRoot> createTypeRoot(EntityType var1) {
      Folder var2 = this.getTypeFolder(var1);
      this.deleteEmptyFolders(var2);
      return Maps.newHashMap();
   }

   private Folder getTypeFolder(EntityType var1) {
      return this.location.subFolder(var1.name()).subFolder("Resources");
   }

   private void deleteEmptyFolders(Folder var1) {
      for (Folder var5 : var1.listSubFolders()) {
         if (var5.isEmpty()) {
            var5.delete();
         }
      }
   }

   public void cleanup() {
      synchronized (this.lock) {
         for (Map var3 : this.typeRoots.values()) {
            for (Object __etlItem77 : var3.values()) {
               EntityFileResourceManager.EntityRoot var5 = (EntityFileResourceManager.EntityRoot) __etlItem77;
               var5.cleanup();
            }
         }
      }
   }

   public void refresh(IntegrationEntity var1) {
      synchronized (this.lock) {
         Map var3 = this.getTypeRoot(var1.getEntityType());
         var3.put(var1, new EntityFileResourceManager.EntityRoot(var1, this.getEntityLocation(var1)));
      }
   }

   public File getEntityLocation(IntegrationEntity var1) {
      return this.getTypeFolder(var1.getEntityType()).getFile(var1.getID().toString());
   }

   private static class EntityRoot {
      private final IntegrationEntity entity;
      private final Folder location;
      private final Map<String, EntityFileResourceImpl> files = Maps.newHashMap();
      private final Object lock = new Object();

      public EntityRoot(IntegrationEntity var1, File var2) {
         this.entity = var1;
         this.location = new Folder(var2);
         this.location.createOnDisk();
         this.load();
      }

      public EntityFileResourceImpl getResource(String var1) {
         synchronized (this.lock) {
            EntityFileResourceImpl var3 = this.files.get(var1);
            if (var3 != null) {
               if (!var3.isDeleted()) {
                  return var3;
               }

               if (var3.getFile().exists()) {
                  this.deleteFileFromDisk(var3.getFile());
               }
            }

            var3 = new EntityFileResourceImpl(this.location.getFile(var1));
            this.files.put(var1, var3);
            return var3;
         }
      }

      public void cleanup() {
         if (this.entity.isDeleted()) {
            this.deleteEverything();
         } else {
            for (EntityFileResourceImpl var2 : this.files.values()) {
               if (var2.getLifeTime() == FileResource.LifeTime.PROJECT_SESSION) {
                  var2.delete();
               }

               if (var2.isDeleted()) {
                  this.deleteFileFromDisk(var2.getFile());
               }
            }
         }
      }

      private void deleteEverything() {
         boolean var1 = this.location.delete();
         if (!var1) {
            ErrorLog.log(EntityFileResourceManager.class, null, new Exception("Failed to delete the file resource for the entity " + this.entity.getName()));

            for (File var5 : this.location.listFiles()) {
               var5.deleteOnExit();
            }
         }
      }

      private void deleteFileFromDisk(File var1) {
         try {
            if (var1.exists()) {
               FileUtils.forceDelete(var1);
            }
         } catch (IOException var3) {
            var1.deleteOnExit();
            ErrorLog.log(EntityFileResourceManager.class, "Failed to delete the entity file resource " + var1.getAbsolutePath() + " from disk.", var3);
         }
      }

      private void load() {
         for (File var4 : this.location.listFiles()) {
            String var5 = var4.getName();
            if (var5.equals(".konga.files")) {
               var4.delete();
            } else {
               this.files.put(var5, new EntityFileResourceImpl(var4));
            }
         }
      }
   }
}
