package org.etlforge.integration.client.project.permission;

import com.google.common.collect.Sets;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.RequiredObjectsCollector;
import org.etlforge.integration.data.project.ProjectDependencies;
import org.etlforge.integration.user.Role;
import org.etlforge.integration.user.RoleId;

abstract class PermissionsSetter<K extends Role, T extends RoleId, V extends RolePermission<T>> {
   private static final EnumSet<Permission> READ_SET = EnumSet.of(Permission.READ);
   private final ProjectDependencies dependencies;
   private Set<IntegrationEntityId> dirtyEntities;

   protected PermissionsSetter(ProjectDependencies var1) {
      this.dependencies = var1;
      this.dirtyEntities = new HashSet<>();
   }

   protected abstract Map<EntityType, ? extends MapImpl<T, V>> getMaps();

   protected abstract boolean hasPermission(K var1, IntegrationEntity var2, Permission var3);

   private MapImpl<T, V> getMap(IntegrationEntity var1) {
      return (MapImpl<T, V>)this.getMaps().get(var1.getEntityType());
   }

   public void set(K var1, T var2, IntegrationEntity var3, Permissions var4, boolean var5) {
      if (!(var3 instanceof BuiltInIntegrationEntity)) {
         MapImpl var6 = this.getMap(var3);
         var6.put(var3.getID(), var2, var4);
         this.setDirty(var3);
         if (var4.isDenied(Permission.READ)) {
            this.denyReadOnDependingObjects((K)var1, (T)var2, var3);
         } else if (!var4.isAllowed(Permission.READ)) {
            this.revokeReadOnDependingObjects((K)var1, (T)var2, var3);
         } else if (var4.getEffectivePermissions().contains(Permission.READ)) {
            PermissionsSetter.DependencyChainReadSetter var7 = new PermissionsSetter.DependencyChainReadSetter();
            var7.makeReadable((K)var1, (T)var2, var3);
         }

         if (var5) {
            for (IntegrationEntity var8 : var3.getAllChildren()) {
               this.set((K)var1, (T)var2, var8, var4, true);
            }
         }
      }
   }

   public void setClean() {
      this.dirtyEntities = new HashSet<>();
   }

   public Set<IntegrationEntityId> getDirtyEntities() {
      return this.dirtyEntities;
   }

   public void grantReadPermission(K var1, T var2, IntegrationEntity var3) {
      HashSet var4 = new HashSet();
      this.grantReadPermissionImpl((K)var1, (T)var2, var3, var4);
   }

   private void grantReadPermissionImpl(K var1, T var2, IntegrationEntity var3, Set<IntegrationEntity> var4) {
      var4.add(var3);
      MapImpl var5 = this.getMap(var3);
      Permissions var6 = var5.get(var3.getID(), var2);
      if (var6 == null || !var6.isAllowed(Permission.READ)) {
         var6 = Permissions.grantRead(var6);
         var5.put(var3.getID(), var2, var6);
         this.setDirty(var3);
      }

      for (IntegrationEntity var7 = var3.getParent(); var7 != null; var7 = var7.getParent()) {
         if (!var4.contains(var7)) {
            this.grantReadPermissionImpl((K)var1, (T)var2, var7, var4);
         }
      }

      for (IntegrationEntity var9 : this.dependencies.getRequiredObjects(var3)) {
         if (!var4.contains(var3)) {
            this.grantReadPermissionImpl((K)var1, (T)var2, var9, var4);
         }
      }
   }

   private void setDirty(IntegrationEntity var1) {
      this.dirtyEntities.add(var1.getID());
   }

   private void denyReadOnDependingObjects(K var1, T var2, IntegrationEntity var3) {
      PermissionsSetter.ReadDenier var4 = new PermissionsSetter.ReadDenier();
      var4.denyReadOnDependingObjects((K)var1, (T)var2, var3);
   }

   private void revokeReadOnDependingObjects(K var1, T var2, IntegrationEntity var3) {
      PermissionsSetter.ReadRevoker var4 = new PermissionsSetter.ReadRevoker();
      var4.revokeReadOnDependingObjects((K)var1, (T)var2, var3);
   }

   private class DependencyChainReadSetter {
      private Set<IntegrationEntity> objects;

      private DependencyChainReadSetter() {
      }

      public void makeReadable(K var1, T var2, IntegrationEntity var3) {
         this.collectObjects(var3);
         this.processObjects((K)var1, (T)var2);
      }

      private void collectObjects(IntegrationEntity var1) {
         this.objects = Sets.newHashSet();
         this.addParents(var1);
         RequiredObjectsCollector var2 = new RequiredObjectsCollector(PermissionsSetter.this.dependencies);

         for (IntegrationEntity var4 : var2.collect(var1)) {
            if (!(var4 instanceof BuiltInIntegrationEntity)) {
               this.objects.add(var4);
               this.addParents(var4);
            }
         }

         this.objects.remove(var1);
      }

      private void addParents(IntegrationEntity var1) {
         for (IntegrationEntity var2 = var1.getParent(); var2 != null; var2 = var2.getParent()) {
            this.objects.add(var2);
         }
      }

      private void processObjects(K var1, T var2) {
         for (IntegrationEntity var4 : this.objects) {
            if (!PermissionsSetter.this.hasPermission((K)var1, var4, Permission.READ)) {
               Permissions var5 = this.ensureReadPermissionIsSet((T)var2, var4);
               MapImpl var6 = PermissionsSetter.this.getMap(var4);
               var6.put(var4.getID(), var2, var5);
               PermissionsSetter.this.setDirty(var4);
            }
         }
      }

      private Permissions ensureReadPermissionIsSet(T var1, IntegrationEntity var2) {
         MapImpl var3 = PermissionsSetter.this.getMap(var2);
         Permissions var4 = var3.get(var2.getID(), var1);
         if (var4 == null) {
            var4 = new Permissions(PermissionsSetter.READ_SET, null);
         } else {
            var4 = Permissions.add(var4, PermissionsSetter.READ_SET, null);
         }

         return var4;
      }
   }

   private class ReadDenier {
      private final Set<IntegrationEntity> cyclicGuard = Sets.newHashSet();

      private ReadDenier() {
      }

      public void denyReadOnDependingObjects(K var1, T var2, IntegrationEntity var3) {
         for (IntegrationEntity var5 : PermissionsSetter.this.dependencies.getDependingObjects(var3)) {
            if (this.cyclicGuard.add(var3)) {
               MapImpl var6 = PermissionsSetter.this.getMap(var5);
               Permissions var7 = var6.get(var5.getID(), var2);
               if (var7 == null) {
                  this.addNewPermissions((T)var2, var5, var6);
               } else if (!var7.isDenied(Permission.READ)) {
                  this.updateExistingPermissions((T)var2, var5, var6, var7);
               }

               this.denyReadOnDependingObjects((K)var1, (T)var2, var5);
            }
         }
      }

      private void addNewPermissions(T var1, IntegrationEntity var2, MapImpl<T, V> var3) {
         var3.put(var2.getID(), var1, new Permissions(null, PermissionsSetter.READ_SET));
         PermissionsSetter.this.setDirty(var2);
      }

      private void updateExistingPermissions(T var1, IntegrationEntity var2, MapImpl<T, V> var3, Permissions var4) {
         var3.put(var2.getID(), var1, Permissions.add(var4, null, PermissionsSetter.READ_SET));
         PermissionsSetter.this.setDirty(var2);
      }
   }

   private class ReadRevoker {
      private final Set<IntegrationEntity> cyclicGuard = Sets.newHashSet();

      private ReadRevoker() {
      }

      public void revokeReadOnDependingObjects(K var1, T var2, IntegrationEntity var3) {
         for (IntegrationEntity var5 : PermissionsSetter.this.dependencies.getDependingObjects(var3)) {
            if (this.cyclicGuard.add(var3)) {
               MapImpl var6 = PermissionsSetter.this.getMap(var5);
               Permissions var7 = var6.get(var5.getID(), var2);
               if (var7 == null) {
                  this.addEmptyPermissions((T)var2, var5, var6);
               } else if (var7.isAllowed(Permission.READ)) {
                  this.removeReadPermission((T)var2, var5, var6, var7);
               }

               this.revokeReadOnDependingObjects((K)var1, (T)var2, var5);
            }
         }
      }

      private void addEmptyPermissions(T var1, IntegrationEntity var2, MapImpl<T, V> var3) {
         var3.put(var2.getID(), var1, new Permissions(null, null));
         PermissionsSetter.this.setDirty(var2);
      }

      private void removeReadPermission(T var1, IntegrationEntity var2, MapImpl<T, V> var3, Permissions var4) {
         var3.put(var2.getID(), var1, Permissions.remove(var4, PermissionsSetter.READ_SET, null));
         PermissionsSetter.this.setDirty(var2);
      }
   }
}
