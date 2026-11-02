package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;

class EditScheduleAction extends AbstractAction {
   private final OperationGraphController controller;
   private final Schedule schedule;

   public EditScheduleAction(OperationGraphController var1, OperationWrapperNode var2) {
      super(Strings.get("Edit.Schedule"));
      this.controller = var1;
      this.schedule = this.getSchedule(var2);
      this.putValue("ShortDescription", Strings.get("Edit.Schedule.ToolTip"));
      this.setEnabled(this.schedule != null);
   }

   private Schedule getSchedule(OperationWrapperNode var1) {
      ScheduleId var2 = var1.getDataObject().getScheduleId();
      return this.controller.getProject().getItemLookup().getEntity(var2, Schedule.class);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.controller.openEditor(this.schedule);
   }
}
