package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.undo.UndoableEdit;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.NextEmailEdit;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.NextOperationEdit;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.SiblingOperationEdit;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.OperationFlow;
import org.etlforge.integration.data.entity.operation.flow.OperationFlowActivity;
import org.etlforge.integration.data.entity.operation.flow.Route;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

class RouteDeleter {
   private final OperationGraphController controller;

   public RouteDeleter(OperationGraphController var1) {
      this.controller = var1;
   }

   public void delete(Route var1) {
      if (this.controller.isChangeAllowed(var1.getFrom())) {
         UndoableEdit var2 = this.createEdit(var1);
         this.updateFlow(var1);
         this.sendEdit(var2);
      }
   }

   private void updateFlow(Route var1) {
      OperationFlow var2 = this.controller.getGraph().getModel().getModeledFlow();
      var2.deleteRoute(var1);
   }

   private UndoableEdit createEdit(Route var1) {
      RouteType var2 = var1.getType();
      if (var2 == RouteType.SIBLING) {
         return this.createSiblingEdit(var1);
      } else if (var2 != RouteType.SUCCESS && var2 != RouteType.FAILURE) {
         throw new IllegalArgumentException("The route cannot be deleted: " + var1);
      } else {
         return this.createSuccessFailureEdit(var1);
      }
   }

   private UndoableEdit createSiblingEdit(Route var1) {
      Operation var2 = (Operation)var1.getTo().getActivity();
      return SiblingOperationEdit.siblingRemoved(this.controller, var1.getFrom(), var2);
   }

   private UndoableEdit createSuccessFailureEdit(Route var1) {
      OperationFlowActivity var2 = var1.getTo().getActivity();
      if (var2 instanceof Operation) {
         return new NextOperationEdit(this.controller, var1.getFrom(), var1.getType(), null);
      } else if (var2 instanceof EmailMessage) {
         return new NextEmailEdit(this.controller, var1.getFrom(), var1.getType(), null);
      } else {
         throw new IllegalStateException("Unexpected target activity: " + var2);
      }
   }

   private void sendEdit(UndoableEdit var1) {
      if (var1 != null) {
         this.controller.fireGraphDataEdit(var1);
      }
   }
}
