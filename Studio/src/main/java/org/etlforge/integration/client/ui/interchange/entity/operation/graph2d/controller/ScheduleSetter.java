package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.undo.OperationScheduleEdit;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;

class ScheduleSetter {
   private final OperationGraphController controller;

   public ScheduleSetter(OperationGraphController var1) {
      this.controller = var1;
   }

   public boolean setSchedule(OperationWrapperNode var1, Schedule var2) {
      if (this.controller.isChangeAllowed(var1)) {
         ScheduleId var3 = var1.getDataObject().getScheduleId();
         ScheduleId var4 = var2 != null ? var2.getID() : null;
         if (differs(var3, var4)) {
            var1.getDataObject().setScheduleId(var4);
            OperationScheduleEdit var5 = new OperationScheduleEdit(this.controller, var1, var3, var4);
            this.controller.fireGraphDataEdit(var5);
            return true;
         }
      }

      return false;
   }

   private static boolean differs(Object var0, Object var1) {
      return var0 == null ? var1 != null : !var0.equals(var1);
   }
}
