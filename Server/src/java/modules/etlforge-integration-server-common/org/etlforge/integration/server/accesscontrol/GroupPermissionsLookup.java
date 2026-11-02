package org.etlforge.integration.server.accesscontrol;

import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.server.data.EntityDescriptor;

public interface GroupPermissionsLookup {
   Permissions getPermissions(Group var1, EntityDescriptor var2);
}
