package org.etlforge.integration.client.project.permission;

import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.user.GroupId;

public final class GroupPermission extends RolePermission<GroupId> {
   public GroupPermission(GroupId var1, IntegrationEntityId var2, int var3, int var4) {
      super(var1, var2, var3, var4);
   }

   public GroupPermission(GroupId var1, IntegrationEntityId var2, Permissions var3) {
      super(var1, var2, var3);
   }
}
