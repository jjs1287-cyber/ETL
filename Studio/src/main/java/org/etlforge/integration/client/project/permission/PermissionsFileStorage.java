package org.etlforge.integration.client.project.permission;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.io.Folder;

public final class PermissionsFileStorage implements PermissionsStorage {
   private final Folder root;
   private final PermissionsFileReader fileReader;

   public PermissionsFileStorage(Folder var1, Iterable<EntityType> var2) {
      this.root = var1;
      this.fileReader = new PermissionsFileReader(var1, var2);
   }

   @Override
   public void save(List<UserPermission> var1, List<GroupPermission> var2) throws PermissionsStorageException {
      PermissionsFileStorage.SaveWorker var3 = new PermissionsFileStorage.SaveWorker();
      var3.save(var1, var2);
   }

   @Override
   public List<UserPermission> readUserPermissions() throws PermissionsStorageException {
      return this.fileReader.readUserPermissions();
   }

   @Override
   public List<GroupPermission> readGroupPermissions() throws PermissionsStorageException {
      return this.fileReader.readGroupPermissions();
   }

   private final class SaveWorker {
      private final Map<EntityType, PermissionsFileWriter> writers = Maps.newHashMap();

      private SaveWorker() {
      }

      public void save(List<UserPermission> var1, List<GroupPermission> var2) throws PermissionsStorageException {
         try {
            this.writeToDisk(var1, var2);
         } catch (IOException var7) {
            throw new PermissionsStorageException("Failed to save new permissions.", var7);
         } finally {
            this.closeWriters();
         }
      }

      private void writeToDisk(List<UserPermission> var1, List<GroupPermission> var2) throws IOException {
         for (UserPermission var4 : var1) {
            PermissionsFileWriter var5 = this.getWriter(var4);
            var5.writeUser(var4.entityId, var4.roleId, var4.allowedBits(), var4.deniedBits());
         }

         for (GroupPermission var7 : var2) {
            PermissionsFileWriter var8 = this.getWriter(var7);
            var8.writeGroup(var7.entityId, var7.roleId, var7.allowedBits(), var7.deniedBits());
         }
      }

      private PermissionsFileWriter getWriter(RolePermission<?> var1) {
         EntityType var2 = var1.entityId.getEntityType();
         PermissionsFileWriter var3 = this.writers.get(var2);
         if (var3 == null) {
            var3 = new PermissionsFileWriter(PermissionsFileStorage.this.root, var1.entityId.getEntityType());
            this.writers.put(var1.entityId.getEntityType(), var3);
         }

         return var3;
      }

      private void closeWriters() {
         for (PermissionsFileWriter var2 : this.writers.values()) {
            try {
               var2.close();
            } catch (IOException var4) {
               var4.printStackTrace();
            }
         }
      }
   }
}
