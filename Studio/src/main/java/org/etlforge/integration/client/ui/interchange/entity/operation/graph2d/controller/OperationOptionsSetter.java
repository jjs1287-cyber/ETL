package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.OperationOptionsEdit;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;

class OperationOptionsSetter {
   private final OperationGraphController controller;

   public OperationOptionsSetter(OperationGraphController var1) {
      this.controller = var1;
   }

   public void setOptions(OperationWrapperNode var1, OperationOptions var2) {
      if (this.doChange(var1, var2)) {
         OperationOptionsEdit var3 = new OperationOptionsEdit(this.controller, var1.getDataObject(), var2);
         var1.getDataObject().setOptions(var2);
         this.controller.fireGraphDataEdit(var3);
      }
   }

   private boolean doChange(OperationWrapperNode var1, OperationOptions var2) {
      OperationOptions var3 = var1.getDataObject().getOptions();
      return !var3.equals(var2) && this.controller.isChangeAllowed(var1);
   }
}
