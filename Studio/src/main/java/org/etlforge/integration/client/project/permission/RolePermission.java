package org.etlforge.integration.client.project.permission;

import com.google.common.base.Objects;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.user.RoleId;

public abstract class RolePermission<T extends RoleId> {
   public final T roleId;
   public final IntegrationEntityId entityId;
   public final Permissions permissions;

   public RolePermission(T var1, IntegrationEntityId var2, int var3, int var4) {
      this((T)var1, var2, new Permissions(var3, var4));
   }

   public RolePermission(T var1, IntegrationEntityId var2, Permissions var3) {
      this.roleId = (T)var1;
      this.entityId = var2;
      this.permissions = var3;
   }

   public final int allowedBits() {
      return this.permissions.getAllowedBits();
   }

   public final int deniedBits() {
      return this.permissions.getDeniedBits();
   }

   @Override
   public final String toString() {
      return Objects.toStringHelper(this).add("roleId", this.roleId).add("entityId", this.entityId).add("permissions", this.permissions).toString();
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (var1 == null) {
         return false;
      }

      if (!var1.getClass().equals(this.getClass())) {
         return false;
      }

      RolePermission var2 = (RolePermission)var1;
      return this.roleId.equals(var2.roleId) && this.entityId.equals(var2.entityId) && this.permissions.equals(var2.permissions);
   }

   @Override
   public final int hashCode() {
      return Objects.hashCode(this.roleId, this.entityId, this.permissions);
   }
}
