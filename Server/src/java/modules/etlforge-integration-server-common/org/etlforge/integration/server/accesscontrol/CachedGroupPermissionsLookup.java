package org.etlforge.integration.server.accesscontrol;

import org.etlforge.collections.Pair;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.server.data.EntityDescriptor;

public final class CachedGroupPermissionsLookup extends AbstractCachedLookup<Pair<Group, EntityDescriptor>, Permissions> implements GroupPermissionsLookup {
   private final GroupPermissionsLookup impl;

   public CachedGroupPermissionsLookup(GroupPermissionsLookup var1) {
      this.impl = var1;
   }

   @Override
   public Permissions getPermissions(Group var1, EntityDescriptor var2) {
      return this.lookup(Pair.of(var1, var2));
   }

   protected Permissions produceNewValue(Pair<Group, EntityDescriptor> var1) {
      return this.impl.getPermissions((Group)var1.first, (EntityDescriptor)var1.second);
   }
}
