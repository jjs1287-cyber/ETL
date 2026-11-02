package org.etlforge.integration.data.functors;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class CompositeEntityFilter implements EntityFilter {
   private final List<EntityFilter> filters;

   public CompositeEntityFilter(Collection<EntityFilter> var1) {
      this.filters = Lists.newArrayList(var1);
   }

   public CompositeEntityFilter(EntityFilter... var1) {
      this.filters = Lists.newArrayList(var1);
   }

   @Override
   public boolean apply(IntegrationEntity var1) {
      for (EntityFilter var3 : this.filters) {
         if (!var3.apply(var1)) {
            return false;
         }
      }

      return true;
   }
}
