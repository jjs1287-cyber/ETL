package org.etlforge.integration.client.project.permission;

import java.util.List;

public final class ItemPermissions {
   private final List<UserPermission> userPermissions;
   private final List<GroupPermission> groupPermissions;

   ItemPermissions(List<UserPermission> var1, List<GroupPermission> var2) {
      this.userPermissions = var1;
      this.groupPermissions = var2;
   }

   public Iterable<UserPermission> getUserPermissions() {
      return this.userPermissions;
   }

   public Iterable<GroupPermission> getGroupPermissions() {
      return this.groupPermissions;
   }
}
