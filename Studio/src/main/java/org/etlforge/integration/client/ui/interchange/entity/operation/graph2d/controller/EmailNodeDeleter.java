package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Iterables;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.EmailWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.OperationActivityNodeDeletedEdit;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;
import org.etlforge.integration.data.entity.operation.flow.RouteFunctions;

class EmailNodeDeleter {
   private final OperationGraphController controller;

   public EmailNodeDeleter(OperationGraphController var1) {
      this.controller = var1;
   }

   public boolean delete(EmailWrapperNode var1) {
      if (!this.isConflict(var1)) {
         OperationActivityNodeDeletedEdit var2 = new OperationActivityNodeDeletedEdit(this.controller, var1);
         this.controller.getGraph().getModel().getModeledFlow().deleteEmail(var1.getDataObject());
         this.controller.fireGraphDataEdit(var2);
         return true;
      } else {
         return false;
      }
   }

   private boolean isConflict(EmailWrapperNode var1) {
      for (OperationWrapper var3 : this.getAffectedOperations(var1)) {
         if (!this.controller.isChangeAllowed(var3)) {
            return true;
         }
      }

      return false;
   }

   private Iterable<OperationWrapper> getAffectedOperations(EmailWrapperNode var1) {
      return Iterables.transform(var1.getDataObject().getIncomingRoutes(), RouteFunctions.FROM);
   }
}
