package org.etlforge.integration.server.accesscontrol;

import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.server.data.EntityDescriptor;

public interface UserPermissionsLookup {
   Permissions getPermissions(User var1, EntityDescriptor var2) throws AccessLookupException;
}
