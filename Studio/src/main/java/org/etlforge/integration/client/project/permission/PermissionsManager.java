package org.etlforge.integration.client.project.permission;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

public final class PermissionsManager {
   private final IntegrationProject project;
   private final PermissionMaps permissions;
   private final PermissionsStorage storage;
   private final EntityPersistor entityPersistor;

   public PermissionsManager(IntegrationProject var1, PermissionsStorage var2, ProjectDependencies var3, EntityPersistor var4) {
      this.project = Preconditions.checkNotNull(var1, "project");
      this.storage = Preconditions.checkNotNull(var2, "storage");
      this.entityPersistor = Preconditions.checkNotNull(var4, "entityPersistor");
      Preconditions.checkNotNull(var3, "projectDependencies");
      this.permissions = new PermissionMaps(getTypesToInclude(var1), var3);
      this.permissions.read(var2);
   }

   private static Collection<EntityType> getTypesToInclude(IntegrationProject var0) {
      HashSet var1 = Sets.newHashSet(var0.getSupportedEntityTypes());
      var1.add(EntityType.Project);
      var1.add(EntityType.RootFolder);
      var1.add(EntityType.Folder);
      return var1;
   }

   public void save() throws PermissionsStorageException {
      this.permissions.save(this.storage);
   }

   public Permissions getPermissions(User var1, IntegrationEntity var2) {
      return this.permissions.get(var1, var2);
   }

   public Permissions getPermissions(Group var1, IntegrationEntity var2) {
      return this.permissions.get(var1, var2);
   }

   public Permissions getEffectivePermissions(User var1, IntegrationEntity var2) {
      return this.permissions.getEffective(var1, var2);
   }

   public Permissions getEffectivePermissions(Group var1, IntegrationEntity var2) {
      return this.permissions.getEffective(var1, var2);
   }

   public void setPermissions(Group var1, IntegrationEntity var2, Permissions var3, boolean var4) {
      Collection var5 = this.permissions.set(var1, var2, var3, var4);
      this.saveChangedEntities(var5);
   }

   public void setPermissions(User var1, IntegrationEntity var2, Permissions var3, boolean var4) {
      Collection var5 = this.permissions.set(var1, var2, var3, var4);
      this.saveChangedEntities(var5);
   }

   private void saveChangedEntities(Collection<IntegrationEntityId> var1) {
      IntegrationEntityLookup var2 = this.project.getItemLookup();

      for (IntegrationEntityId var4 : var1) {
         IntegrationEntity var5 = var2.getEntity(var4);
         if (shouldBeMadeDirty(var5)) {
            try {
               this.entityPersistor.save(var5);
            } catch (InterchangeSavingException var7) {
               ErrorLog.log(
                  this.getClass(),
                  "Failed to save the changes made to \"" + var5.getName() + "\" to disk after it was made deploy dirty by new permissions settings.",
                  var7
               );
            }
         }
      }
   }

   private static boolean shouldBeMadeDirty(IntegrationEntity var0) {
      return var0 != null && !var0.isDeployDirty() && !(var0 instanceof RootFolder);
   }

   public void exportPermissions(IntegrationEntity var1, PermissionsManager var2) throws PermissionsStorageException {
      if (var2 != this) {
         this.permissions.exportTo(var1, var2.permissions);
         var2.save();
      }
   }

   public List<IntegrationEntity> getVetosReadPermission(User var1, IntegrationEntity var2) {
      return this.permissions.getVetosReadPermission(var1, var2);
   }

   public List<IntegrationEntity> getVetosReadPermission(Group var1, IntegrationEntity var2) {
      return this.permissions.getVetosReadPermission(var1, var2);
   }

   public ItemPermissions getItemPermissions(IntegrationEntity var1) {
      return this.permissions.getItemPermissions(var1);
   }

   public void removeEntity(IntegrationEntity var1) {
      this.permissions.removeEntity(var1);
   }

   public void copyPermissions(IntegrationEntity var1, IntegrationEntity var2) {
      this.permissions.copyPermissions(var1, var2);
   }
}
