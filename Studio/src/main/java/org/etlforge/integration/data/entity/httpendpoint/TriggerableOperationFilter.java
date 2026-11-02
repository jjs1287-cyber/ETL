package org.etlforge.integration.data.entity.httpendpoint;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.functors.EntityFilter;

public final class TriggerableOperationFilter implements EntityFilter {
   public static final TriggerableOperationFilter FILTER = new TriggerableOperationFilter();

   @Override
   public boolean apply(IntegrationEntity var1) {
      return var1 instanceof Operation ? this.isTriggerable((Operation)var1) : false;
   }

   private boolean isTriggerable(Operation var1) {
      OperationType var2 = var1.getOperationType();
      return var2.isStartedByEngine();
   }
}
