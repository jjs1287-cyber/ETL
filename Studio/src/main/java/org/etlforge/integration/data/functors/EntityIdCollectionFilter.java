package org.etlforge.integration.data.functors;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class EntityIdCollectionFilter implements EntityFilter {
   private final Set<IntegrationEntityId> idsToAccept;

   public EntityIdCollectionFilter(Collection<IntegrationEntityId> var1) {
      this.idsToAccept = Sets.newHashSet(var1);
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      return this.idsToAccept.contains(var1.getID());
   }
}
