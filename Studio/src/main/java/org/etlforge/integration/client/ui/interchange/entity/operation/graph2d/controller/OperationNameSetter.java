package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.OperationWrapperRenameEdit;
import org.etlforge.integration.data.entity.operation.flow.OperationWrapper;

class OperationNameSetter {
   private final OperationGraphController controller;

   public OperationNameSetter(OperationGraphController var1) {
      this.controller = var1;
   }

   public void setName(OperationWrapperNode var1, String var2) {
      OperationWrapper var3 = var1.getDataObject();
      String var4 = var3.getName();
      if (!var4.equals(var2) && this.controller.isChangeAllowed(var3)) {
         OperationWrapperRenameEdit var5 = new OperationWrapperRenameEdit(this.controller, var3, var2);
         var3.setName(var2);
         this.controller.fireGraphDataEdit(var5);
      }
   }
}
