package org.etlforge.integration.client.project.permission;

import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.user.GroupId;

class GroupMap extends MapImpl<GroupId, GroupPermission> {
   protected GroupPermission createRolePermission(IntegrationEntityId var1, GroupId var2, Permissions var3) {
      return new GroupPermission(var2, var1, var3);
   }
}
