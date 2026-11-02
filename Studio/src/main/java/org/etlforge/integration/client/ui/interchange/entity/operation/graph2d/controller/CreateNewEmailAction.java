package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

final class CreateNewEmailAction extends AbstractAction {
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;
   private final RouteType routeType;

   public CreateNewEmailAction(OperationGraphController var1, OperationWrapperNode var2, RouteType var3) {
      super(Strings.format("CreateNew.Name", EntityType.EmailMessage.getDisplayName()));
      this.controller = var1;
      this.opNode = var2;
      this.routeType = var3;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.controller.isChangeAllowed(this.opNode)) {
         this.controller.getEntityFactory().createEntity(EntityType.EmailMessage, new UiEntityFactoryCallback.Adapter() {
            @Override
            public void postProjectInsertion(IntegrationEntity var1) {
               CreateNewEmailAction.this.insertEmail((EmailMessage)var1);
            }
         });
      }
   }

   private void insertEmail(EmailMessage var1) {
      this.controller.setNextEmail(this.opNode, var1, this.routeType);
   }
}
