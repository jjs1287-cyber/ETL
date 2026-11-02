package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

final class CreateNewOperationAction extends AbstractAction {
   private final OperationGraphController controller;
   private final OperationWrapperNode opNode;
   private final RouteType routeType;

   public CreateNewOperationAction(OperationGraphController var1, OperationWrapperNode var2, RouteType var3) {
      super(Strings.format("CreateNew.Name", EntityType.Operation.getDisplayName()));
      this.controller = var1;
      this.opNode = var2;
      this.routeType = var3;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      if (this.controller.isChangeAllowed(this.opNode)) {
         Request var2 = Request.builder(EntityType.Operation).openEditor(false).build();
         this.controller.getEntityFactory().createEntity(var2, new UiEntityFactoryCallback.Adapter() {
            @Override
            public void postProjectInsertion(IntegrationEntity var1) {
               CreateNewOperationAction.this.insertOperation((Operation)var1);
            }
         });
      }
   }

   private void insertOperation(Operation var1) {
      this.controller.saveProjectItem(var1);
      this.controller.setNextOperation(this.opNode, var1, this.routeType);
      NewOperationProcessor var2 = new NewOperationProcessor(this.controller);
      var2.process(var1);
   }
}
