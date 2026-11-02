package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.undo.UndoableEdit;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.NextEmailEdit;
import org.etlforge.integration.data.entity.EmailMessage;
import org.etlforge.integration.data.entity.id.EmailMessageId;
import org.etlforge.integration.data.entity.operation.flow.RouteType;

class NextEmailSetter {
   private final OperationGraphController controller;

   public NextEmailSetter(OperationGraphController var1) {
      this.controller = var1;
   }

   public boolean setNextEmail(OperationWrapperNode var1, EmailMessage var2, RouteType var3) {
      if (var3 != RouteType.SUCCESS && var3 != RouteType.FAILURE) {
         throw new IllegalArgumentException("routeType must be one of SUCCESS or FAILURE: " + var3);
      } else if (this.doChange(var1, var2, var3)) {
         UndoableEdit var4 = this.createEdit(var1, var2, var3);
         this.updateFlow(var1, var2, var3);
         this.controller.fireGraphDataEdit(var4);
         return true;
      } else {
         return false;
      }
   }

   private boolean doChange(OperationWrapperNode var1, EmailMessage var2, RouteType var3) {
      EmailMessageId var4 = var3 == RouteType.SUCCESS ? var1.getDataObject().getSuccessEmailId() : var1.getDataObject().getFailureEmailId();
      EmailMessageId var5 = var2 != null ? var2.getID() : null;
      return this.differs(var4, var5) && this.controller.isChangeAllowed(var1);
   }

   private boolean differs(Object var1, Object var2) {
      return var1 == null ? var2 != null : !var1.equals(var2);
   }

   private UndoableEdit createEdit(OperationWrapperNode var1, EmailMessage var2, RouteType var3) {
      return new NextEmailEdit(this.controller, var1.getDataObject(), var3, var2);
   }

   private void updateFlow(OperationWrapperNode var1, EmailMessage var2, RouteType var3) {
      EmailMessageId var4 = var2 != null ? var2.getID() : null;
      if (var3 == RouteType.SUCCESS) {
         var1.getDataObject().setSuccessEmailId(var4);
      } else {
         var1.getDataObject().setFailureEmailId(var4);
      }
   }
}
