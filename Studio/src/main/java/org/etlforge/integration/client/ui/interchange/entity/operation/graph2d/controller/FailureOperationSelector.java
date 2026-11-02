package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.NextOperationFilter;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

public class FailureOperationSelector extends AbstractOnSuccessFailureSelector<Operation> {
   public FailureOperationSelector(OperationGraphController var1, OperationWrapperNode var2) {
      super(var1, var2, EntityType.Operation, Operation.class, RouteType.FAILURE);
      this.setFilter(new NextOperationFilter());
   }

   @Override
   protected IntegrationEntityId getCurrentEntityId(OperationWrapperNode var1) {
      return var1.getDataObject().getFailureOperationId();
   }

   protected void apply(OperationGraphController var1, OperationWrapperNode var2, Operation var3) {
      var1.setNextOperation(var2, var3, RouteType.FAILURE);
   }
}
