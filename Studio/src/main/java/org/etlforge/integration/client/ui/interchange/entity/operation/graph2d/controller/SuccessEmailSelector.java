package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

public class SuccessEmailSelector extends AbstractOnSuccessFailureSelector<EmailMessage> {
   public SuccessEmailSelector(OperationGraphController var1, OperationWrapperNode var2) {
      super(var1, var2, EntityType.EmailMessage, EmailMessage.class, RouteType.SUCCESS);
   }

   @Override
   protected IntegrationEntityId getCurrentEntityId(OperationWrapperNode var1) {
      return var1.getDataObject().getSuccessEmailId();
   }

   protected void apply(OperationGraphController var1, OperationWrapperNode var2, EmailMessage var3) {
      var1.setNextEmail(var2, var3, RouteType.SUCCESS);
   }
}
