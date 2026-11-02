package org.etlforge.integration.client.project.permission;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.GroupId;
import org.etlforge.integration.user.User;
import org.etlforge.integration.user.UserId;

final class PermissionMaps {
   private final ProjectDependencies dependencies;
   private final Map<EntityType, GroupMap> groupMaps;
   private final Map<EntityType, UserMap> userMaps;
   private final PermissionsSetter<User, UserId, UserPermission> userPermissionSetter;
   private final PermissionsSetter<Group, GroupId, GroupPermission> groupPermissionSetter;

   public PermissionMaps(Collection<EntityType> var1, ProjectDependencies var2) {
      this.dependencies = var2;
      int var3 = var1.size();
      this.groupMaps = Maps.newHashMapWithExpectedSize(var3);
      this.userMaps = Maps.newHashMapWithExpectedSize(var3);

      for (EntityType var5 : var1) {
         this.groupMaps.put(var5, new GroupMap());
         this.userMaps.put(var5, new UserMap());
      }

      this.userPermissionSetter = new PermissionMaps.UserPermissionsSetter();
      this.groupPermissionSetter = new PermissionMaps.GroupPermissionsSetter();
   }

   public void read(PermissionsStorage var1) {
      this.reset();

      try {
         for (UserPermission var3 : var1.readUserPermissions()) {
            UserMap var4 = this.userMaps.get(var3.entityId.getEntityType());
            var4.put(var3);
         }

         for (GroupPermission var7 : var1.readGroupPermissions()) {
            GroupMap var8 = this.groupMaps.get(var7.entityId.getEntityType());
            var8.put(var7);
         }
      } catch (PermissionsStorageException var5) {
         ErrorLog.attention(PermissionMaps.class, "An unexpected error occurred when loading the project permissions: " + var5.getMessage(), var5);
      }
   }

   private void reset() {
      for (UserMap var2 : this.userMaps.values()) {
         var2.clear();
      }

      for (GroupMap var4 : this.groupMaps.values()) {
         var4.clear();
      }
   }

   public boolean hasPermission(User var1, IntegrationEntity var2, Permission var3) {
      Permissions var4 = this.getEffective(var1, var2);
      return var4 != null && var4.isAllowed(var3);
   }

   public boolean hasPermission(Group var1, IntegrationEntity var2, Permission var3) {
      Permissions var4 = this.getEffective(var1, var2);
      return var4 != null && var4.isAllowed(var3);
   }

   public Permissions getEffective(User var1, IntegrationEntity var2) {
      EffectivePermissionsGetter var3 = new EffectivePermissionsGetter(this.dependencies, this.groupMaps, this.userMaps);
      return var3.get(var2, var1);
   }

   public Permissions getEffective(Group var1, IntegrationEntity var2) {
      EffectivePermissionsGetter var3 = new EffectivePermissionsGetter(this.dependencies, this.groupMaps, this.userMaps);
      return var3.get(var2, var1);
   }

   public Permissions get(Group var1, IntegrationEntity var2) {
      GroupMap var3 = this.groupMaps.get(var2.getEntityType());
      return var3.get(var2.getID(), var1.getId());
   }

   public Permissions get(User var1, IntegrationEntity var2) {
      UserMap var3 = this.userMaps.get(var2.getEntityType());
      return var3.get(var2.getID(), var1.getId());
   }

   public Collection<IntegrationEntityId> set(Group var1, IntegrationEntity var2, Permissions var3, boolean var4) {
      this.groupPermissionSetter.setClean();
      this.groupPermissionSetter.set(var1, var1.getId(), var2, var3, var4);
      return this.groupPermissionSetter.getDirtyEntities();
   }

   public Collection<IntegrationEntityId> set(User var1, IntegrationEntity var2, Permissions var3, boolean var4) {
      this.userPermissionSetter.setClean();
      this.userPermissionSetter.set(var1, var1.getId(), var2, var3, var4);
      return this.userPermissionSetter.getDirtyEntities();
   }

   public List<IntegrationEntity> getVetosReadPermission(User var1, IntegrationEntity var2) {
      ArrayList var3 = Lists.newArrayList();

      for (IntegrationEntity var4 = var2.getParent(); var4 != null && !(var4 instanceof RootFolder); var4 = var4.getParent()) {
         if (!this.hasPermission(var1, var4, Permission.READ)) {
            var3.add(var4);
         }
      }

      for (IntegrationEntity var6 : this.dependencies.getRequiredObjects(var2)) {
         if (!this.hasPermission(var1, var6, Permission.READ)) {
            var3.add(var6);
         }
      }

      return var3;
   }

   public List<IntegrationEntity> getVetosReadPermission(Group var1, IntegrationEntity var2) {
      ArrayList var3 = Lists.newArrayList();

      for (IntegrationEntity var4 = var2.getParent(); var4 != null; var4 = var4.getParent()) {
         if (!this.hasPermission(var1, var4, Permission.READ)) {
            var3.add(var4);
         }
      }

      for (IntegrationEntity var6 : this.dependencies.getRequiredObjects(var2)) {
         if (!this.hasPermission(var1, var6, Permission.READ)) {
            var3.add(var6);
         }
      }

      return var3;
   }

   public ItemPermissions getItemPermissions(IntegrationEntity var1) {
      EntityType var2 = var1.getEntityType();
      IntegrationEntityId var3 = var1.getID();
      UserMap var4 = this.userMaps.get(var2);
      List var5 = var4.getAllPermissionsForItem(var3);
      GroupMap var6 = this.groupMaps.get(var2);
      List var7 = var6.getAllPermissionsForItem(var3);
      return new ItemPermissions(var5, var7);
   }

   public void removeEntity(IntegrationEntity var1) {
      EntityType var2 = var1.getEntityType();
      IntegrationEntityId var3 = var1.getID();
      this.userMaps.get(var2).remove(var3);
      this.groupMaps.get(var2).remove(var3);
   }

   public void save(PermissionsStorage var1) throws PermissionsStorageException {
      ArrayList var2 = Lists.newArrayList();

      for (UserMap var4 : this.userMaps.values()) {
         var2.addAll(var4.getAllPermissions());
      }

      ArrayList var6 = Lists.newArrayList();

      for (GroupMap var5 : this.groupMaps.values()) {
         var6.addAll(var5.getAllPermissions());
      }

      var1.save(var2, var6);

      for (UserMap var10 : this.userMaps.values()) {
         var10.setClean();
      }

      for (GroupMap var11 : this.groupMaps.values()) {
         var11.setClean();
      }
   }

   public void exportTo(IntegrationEntity var1, PermissionMaps var2) {
      if (var2 != this) {
         ItemPermissions var3 = this.getItemPermissions(var1);
         this.exportUserPermissions(var2, var1, var3);
         this.exportGroupPermissions(var2, var1, var3);
      }
   }

   private void exportUserPermissions(PermissionMaps var1, IntegrationEntity var2, ItemPermissions var3) {
      UserMap var4 = var1.userMaps.get(var2.getEntityType());
      var4.putAll(var3.getUserPermissions());
   }

   private void exportGroupPermissions(PermissionMaps var1, IntegrationEntity var2, ItemPermissions var3) {
      GroupMap var4 = var1.groupMaps.get(var2.getEntityType());
      var4.putAll(var3.getGroupPermissions());
   }

   public void copyPermissions(IntegrationEntity var1, IntegrationEntity var2) {
      UserMap var3 = this.userMaps.get(var1.getEntityType());
      var3.copy(var1.getID(), var2.getID());
      GroupMap var4 = this.groupMaps.get(var1.getEntityType());
      var4.copy(var1.getID(), var2.getID());
   }

   private class GroupPermissionsSetter extends PermissionsSetter<Group, GroupId, GroupPermission> {
      public GroupPermissionsSetter() {
         super(PermissionMaps.this.dependencies);
      }

      @Override
      protected Map<EntityType, ? extends MapImpl<GroupId, GroupPermission>> getMaps() {
         return PermissionMaps.this.groupMaps;
      }

      protected boolean hasPermission(Group var1, IntegrationEntity var2, Permission var3) {
         return PermissionMaps.this.hasPermission(var1, var2, var3);
      }
   }

   private class UserPermissionsSetter extends PermissionsSetter<User, UserId, UserPermission> {
      public UserPermissionsSetter() {
         super(PermissionMaps.this.dependencies);
      }

      @Override
      protected Map<EntityType, ? extends MapImpl<UserId, UserPermission>> getMaps() {
         return PermissionMaps.this.userMaps;
      }

      protected boolean hasPermission(User var1, IntegrationEntity var2, Permission var3) {
         return PermissionMaps.this.hasPermission(var1, var2, var3);
      }
   }
}
