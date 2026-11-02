package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.OperationActivityNodeDeletedEdit;
import org.etlforge.integration.data.entity.operation.flow.OperationActivityWrapper;
import org.etlforge.integration.data.entity.operation.flow.OperationFlow;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.Route;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

class OperationNodeDeleter {
   private final OperationWrapperNode nodeToDelete;
   private final OperationGraphController controller;

   public OperationNodeDeleter(OperationGraphController var1, OperationWrapperNode var2) {
      this.controller = var1;
      this.nodeToDelete = var2;
   }

   public boolean delete() {
      if (this.isDeleteable() && !this.isPotentialConflict()) {
         OperationActivityNodeDeletedEdit var1 = new OperationActivityNodeDeletedEdit(this.controller, this.nodeToDelete);
         this.updateFlow();
         this.controller.fireGraphDataEdit(var1);
         return true;
      } else {
         return false;
      }
   }

   private void updateFlow() {
      OperationWrapper var1 = this.nodeToDelete.getDataObject();
      this.getFlow().deleteOperation(var1);
   }

   private boolean isDeleteable() {
      OperationWrapper var1 = this.nodeToDelete.getDataObject();
      if (var1 == this.getFlow().getStartPoint()) {
         Alert.info("The selected operation is the main operation in this graph. It cannot be deleted.", "Cannot delete this node");
         return false;
      } else if (var1.findIncomingRoute(RouteType.SCRIPTED) != null) {
         Alert.info("The selected operation is triggered through a RunOperation() call in a script. It cannot be deleted.", "Cannot delete this node");
         return false;
      } else {
         return true;
      }
   }

   private boolean isPotentialConflict() {
      if (!this.controller.isChangeAllowed(this.nodeToDelete)) {
         return true;
      }

      OperationWrapper var1 = this.nodeToDelete.getDataObject();
      Predicate var2 = this.getRoutePredicate();

      for (Object __etlItem60 : var1.findAllIncomingRoutes(var2)) {
         Route var4 = (Route) __etlItem60;
         if (!this.controller.isChangeAllowed(var4.getFrom())) {
            return true;
         }
      }

      for (Object __etlItem66 : var1.findAllOutgoingRoutes(var2)) {
         Route var7 = (Route) __etlItem66;
         OperationActivityWrapper var5 = var7.getTo();
         if (var5 instanceof OperationWrapper && !this.controller.isChangeAllowed((OperationWrapper)var5)) {
            return true;
         }
      }

      return false;
   }

   private Predicate<Route> getRoutePredicate() {
      return Predicates.or(RouteType.SUCCESS, RouteType.FAILURE);
   }

   private OperationFlow getFlow() {
      return this.controller.getGraph().getModel().getModeledFlow();
   }
}
