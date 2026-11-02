package org.etlforge.integration.server.accesscontrol;

import org.etlforge.collections.Pair;
import org.etlforge.integration.data.access.Permissions;
import org.etlforge.integration.server.data.EntityDescriptor;

public final class CachedUserPermissionsLookup extends AbstractCachedLookup<Pair<User, EntityDescriptor>, Permissions> implements UserPermissionsLookup {
   private final UserPermissionsLookup impl;

   public CachedUserPermissionsLookup(UserPermissionsLookup var1) {
      this.impl = var1;
   }

   @Override
   public Permissions getPermissions(User var1, EntityDescriptor var2) throws AccessLookupException {
      return this.lookup(Pair.of(var1, var2));
   }

   protected Permissions produceNewValue(Pair<User, EntityDescriptor> var1) {
      return this.impl.getPermissions((User)var1.first, (EntityDescriptor)var1.second);
   }

   @Override
   public void reset() {
      super.reset();
   }
}
