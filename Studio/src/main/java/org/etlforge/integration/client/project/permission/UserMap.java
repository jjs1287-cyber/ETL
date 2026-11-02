package org.etlforge.integration.client.project.permission;

import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.user.UserId;

class UserMap extends MapImpl<UserId, UserPermission> {
   protected UserPermission createRolePermission(IntegrationEntityId var1, UserId var2, Permissions var3) {
      return new UserPermission(var2, var1, var3);
   }
}
