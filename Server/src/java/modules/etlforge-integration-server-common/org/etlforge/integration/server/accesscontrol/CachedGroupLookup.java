package org.etlforge.integration.server.accesscontrol;

import com.google.common.base.Preconditions;
import java.util.List;

public class CachedGroupLookup extends AbstractCachedLookup<User, List<Group>> implements GroupLookup {
   private final GroupLookup impl;

   public CachedGroupLookup(GroupLookup var1) {
      Preconditions.checkNotNull(var1, "impl");
      this.impl = var1;
   }

   @Override
   public List<Group> getGroups(User var1) throws AccessLookupException {
      return this.lookup(var1);
   }

   protected List<Group> produceNewValue(User var1) {
      return this.impl.getGroups(var1);
   }

   @Override
   public void reset() {
      super.reset();
   }
}
