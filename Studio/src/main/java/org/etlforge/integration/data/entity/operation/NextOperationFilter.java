package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.functors.EntityFilter;

public final class NextOperationFilter implements EntityFilter {
   private final EntityFilter impl = new NextOperationFilter.TypeFilter();

   @Override
   public boolean apply(IntegrationEntity var1) {
      return var1 instanceof Operation && this.impl.apply(var1);
   }

   private static class TypeFilter implements EntityFilter {
      private TypeFilter() {
      }

      @Override
      public boolean apply(IntegrationEntity var1) {
         return var1 instanceof Operation && ((Operation)var1).getOperationType().isStartedByEngine();
      }
   }
}
