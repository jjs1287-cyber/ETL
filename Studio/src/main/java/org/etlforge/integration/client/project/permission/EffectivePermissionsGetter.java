package org.etlforge.integration.client.project.permission;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.user.Group;
import org.etlforge.integration.user.User;

final class EffectivePermissionsGetter {
   private static final EnumSet<Permission> READ_SET = EnumSet.of(Permission.READ);
   private final ProjectDependencies dependencies;
   private final Map<EntityType, GroupMap> groupMaps;
   private final Map<EntityType, UserMap> userMaps;

   public EffectivePermissionsGetter(ProjectDependencies var1, Map<EntityType, GroupMap> var2, Map<EntityType, UserMap> var3) {
      this.dependencies = var1;
      this.groupMaps = var2;
      this.userMaps = var3;
   }

   public Permissions get(IntegrationEntity var1, User var2) {
      return new EffectivePermissionsGetter.EffectiveUserPermissions(var2).get(var1);
   }

   public Permissions get(IntegrationEntity var1, Group var2) {
      return new EffectivePermissionsGetter.EffectiveGroupPermissions(var2).get(var1);
   }

   private static Permissions mergeReadBit(Permissions var0, Permissions var1) {
      if (var1 != null) {
         if (var1.isDenied(Permission.READ)) {
            var0 = Permissions.deny(var0, Permission.READ);
         } else if (!var1.isAllowed(Permission.READ) && var0 != null) {
            var0 = Permissions.remove(var0, READ_SET, null);
         }
      }

      return var0;
   }

   private class EffectiveGroupPermissions {
      private final Group group;
      private Set<IntegrationEntity> cyclicDependencyGuard;

      public EffectiveGroupPermissions(Group var2) {
         this.group = var2;
      }

      public Permissions get(IntegrationEntity var1) {
         this.cyclicDependencyGuard = new HashSet<>();
         return new EffectivePermissionsGetter.EffectiveGroupPermissions.Step(var1).process();
      }

      private class Step {
         private final IntegrationEntity entity;
         private Permissions perms;

         public Step(IntegrationEntity var2) {
            this.entity = var2;
            this.perms = Permissions.fullControl();
         }

         public Permissions process() {
            if (!(this.entity instanceof BuiltInIntegrationEntity)) {
               this.processImpl();
            }

            return this.perms;
         }

         private void processImpl() {
            if (this.checkExplicitGroupPemissions() && this.checkParent()) {
               this.checkDependencies();
            }
         }

         private boolean checkExplicitGroupPemissions() {
            if (this.entity instanceof RootFolder) {
               return false;
            }

            GroupMap var1 = EffectivePermissionsGetter.this.groupMaps.get(this.entity.getEntityType());
            this.perms = var1.get(this.entity.getID(), EffectiveGroupPermissions.this.group.getId());
            return this.readIsNotDenied();
         }

         private boolean readIsNotDenied() {
            return !this.readIsDenied();
         }

         private boolean checkParent() {
            IntegrationEntity var1 = this.entity.getParent();
            if (var1 != null && EffectiveGroupPermissions.this.cyclicDependencyGuard.add(var1)) {
               this.perms = EffectivePermissionsGetter.mergeReadBit(this.perms, EffectiveGroupPermissions.this.new Step(var1).process());
            }

            return this.readIsNotDenied();
         }

         private void checkDependencies() {
            for (IntegrationEntity var2 : EffectivePermissionsGetter.this.dependencies.getRequiredObjects(this.entity)) {
               if (EffectiveGroupPermissions.this.cyclicDependencyGuard.add(var2)) {
                  this.perms = EffectivePermissionsGetter.mergeReadBit(this.perms, EffectiveGroupPermissions.this.new Step(var2).process());
                  if (this.readIsDenied()) {
                     return;
                  }
               }
            }
         }

         private boolean readIsDenied() {
            return this.perms != null && this.perms.isDenied(Permission.READ);
         }
      }
   }

   private class EffectiveUserPermissions {
      private final User user;
      private Set<IntegrationEntity> cyclicDependencyGuard;

      public EffectiveUserPermissions(User var2) {
         this.user = var2;
      }

      public Permissions get(IntegrationEntity var1) {
         this.cyclicDependencyGuard = new HashSet<>();
         return new EffectivePermissionsGetter.EffectiveUserPermissions.Step(var1).process();
      }

      private class Step {
         private final IntegrationEntity entity;
         private Permissions userPerms;

         public Step(IntegrationEntity var2) {
            this.entity = var2;
            this.userPerms = Permissions.fullControl();
         }

         public Permissions process() {
            if (!(this.entity instanceof BuiltInIntegrationEntity)) {
               this.processImpl();
            }

            return this.userPerms;
         }

         private void processImpl() {
            if (this.checkExplicitUserPermissions() && this.checkGroupPermissions() && this.processParent()) {
               this.processDependencies();
            }
         }

         private boolean checkExplicitUserPermissions() {
            if (this.entity instanceof RootFolder) {
               return false;
            }

            UserMap var1 = EffectivePermissionsGetter.this.userMaps.get(this.entity.getEntityType());
            this.userPerms = var1.get(this.entity.getID(), EffectiveUserPermissions.this.user.getId());
            return this.userPerms == null || !this.userPerms.isEverythingDenied();
         }

         private boolean checkGroupPermissions() {
            GroupMap var1 = EffectivePermissionsGetter.this.groupMaps.get(this.entity.getEntityType());

            for (Group var3 : EffectiveUserPermissions.this.user.getGroups()) {
               Permissions var4 = var1.get(this.entity.getID(), var3.getId());
               this.userPerms = Permissions.add(this.userPerms, var4);
               if (this.userPerms != null && this.userPerms.isEverythingDenied()) {
                  return false;
               }
            }

            return this.readIsNotDenied();
         }

         private boolean readIsNotDenied() {
            return this.userPerms == null || !this.userPerms.isDenied(Permission.READ);
         }

         private boolean processParent() {
            IntegrationEntity var1 = this.entity.getParent();
            if (var1 != null && EffectiveUserPermissions.this.cyclicDependencyGuard.add(var1)) {
               this.userPerms = EffectivePermissionsGetter.mergeReadBit(this.userPerms, EffectiveUserPermissions.this.new Step(var1).process());
            }

            return this.readIsNotDenied();
         }

         private void processDependencies() {
            for (IntegrationEntity var2 : EffectivePermissionsGetter.this.dependencies.getRequiredObjects(this.entity)) {
               if (EffectiveUserPermissions.this.cyclicDependencyGuard.add(var2)) {
                  this.userPerms = EffectivePermissionsGetter.mergeReadBit(this.userPerms, EffectiveUserPermissions.this.new Step(var2).process());
                  if (this.readIsDenied()) {
                     return;
                  }
               }
            }
         }

         private boolean readIsDenied() {
            return this.userPerms != null && this.userPerms.isDenied(Permission.READ);
         }
      }
   }
}
