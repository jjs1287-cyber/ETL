package org.etlforge.integration.client.project.permission;

import com.google.common.base.Objects;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.user.RoleId;

abstract class MapImpl<T extends RoleId, V extends RolePermission<T>> {
   private final Table<IntegrationEntityId, T, Permissions> permissionsMap = HashBasedTable.create();
   private boolean dirty;

   public final void putAll(Iterable<V> var1) {
      for (RolePermission var3 : var1) {
         this.put((V)var3);
      }
   }

   public final void put(V var1) {
      this.put(var1.entityId, var1.roleId, var1.permissions);
   }

   public final void put(IntegrationEntityId var1, T var2, Permissions var3) {
      this.permissionsMap.put(var1, (T)var2, var3);
      this.dirty = true;
   }

   public final Permissions get(IntegrationEntityId var1, T var2) {
      return this.permissionsMap.get(var1, var2);
   }

   public final List<V> getAllPermissionsForItem(IntegrationEntityId var1) {
      ArrayList var2 = Lists.newArrayList();
      this.collect(var1, var2);
      return var2;
   }

   private void collect(IntegrationEntityId var1, List<V> var2) {
      Map var3 = this.permissionsMap.row(var1);

      for (Object __etlItem48 : var3.entrySet()) {
         Entry var5 = (Entry) __etlItem48;
         V var6 = this.createRolePermission(var1, (T)var5.getKey(), (Permissions)var5.getValue());
         var2.add(var6);
      }
   }

   public final List<V> getAllPermissions() {
      ArrayList var1 = Lists.newArrayList();

      for (Cell var3 : this.permissionsMap.cellSet()) {
         RolePermission var4 = this.createRolePermission((IntegrationEntityId)var3.getRowKey(), (T)var3.getColumnKey(), (Permissions)var3.getValue());
         var1.add(var4);
      }

      return var1;
   }

   protected abstract V createRolePermission(IntegrationEntityId var1, T var2, Permissions var3);

   public final void remove(IntegrationEntityId var1) {
      Map var2 = this.permissionsMap.row(var1);
      if (!var2.isEmpty()) {
         var2.clear();
         this.dirty = true;
      }
   }

   public final void clear() {
      this.permissionsMap.clear();
      this.dirty = true;
   }

   public final boolean isDirty() {
      return this.dirty;
   }

   public final void setClean() {
      this.dirty = false;
   }

   public final void copy(IntegrationEntityId var1, IntegrationEntityId var2) {
      Map var3 = this.permissionsMap.row(var2);
      Map var4 = this.permissionsMap.row(var1);
      this.dirty = this.dirty || !Objects.equal(var3, var4);
      var3.clear();
      var3.putAll(var4);
   }
}
