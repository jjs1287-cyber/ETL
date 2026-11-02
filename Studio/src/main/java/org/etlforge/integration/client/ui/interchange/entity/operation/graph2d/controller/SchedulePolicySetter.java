package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.SchedulePolicyEdit;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;

class SchedulePolicySetter {
   private final OperationGraphController controller;

   public SchedulePolicySetter(OperationGraphController var1) {
      this.controller = var1;
   }

   public void setPolicy(OperationWrapperNode var1, SchedulePolicy var2) {
      if (this.doChange(var1, var2)) {
         SchedulePolicyEdit var3 = new SchedulePolicyEdit(this.controller, var1.getDataObject(), var2);
         var1.getDataObject().setSchedulePolicy(var2);
         this.controller.fireGraphDataEdit(var3);
      }
   }

   private boolean doChange(OperationWrapperNode var1, SchedulePolicy var2) {
      return var2 != var1.getDataObject().getSchedulePolicy() && this.controller.isChangeAllowed(var1);
   }
}
