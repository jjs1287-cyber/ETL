package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.JMenu;
import javax.swing.JPopupMenu.Separator;
import org.etlforge.integration.client.ui.entity.selector.EntityMenuSelector;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.menu.KongaMenu;

class SelectScheduleMenuBuilder {
   private final OperationGraphController controller;

   public SelectScheduleMenuBuilder(OperationGraphController var1) {
      this.controller = var1;
   }

   public JMenu build(OperationWrapperNode var1) {
      KongaMenu var2 = this.buildSelectorMenu(var1);
      this.addCreateNewItem(var2, var1);
      this.checkEnabledState(var2, var1);
      return var2;
   }

   private KongaMenu buildSelectorMenu(final OperationWrapperNode var1) {
      EntityMenuSelector var2 = new EntityMenuSelector<>(this.controller.getProject(), EntityType.Schedule, Schedule.class);
      var2.setPreselectedEntity(this.getCurrentSchedule(var1));
      return var2.getMenu(Strings.get("Schedule.Menu.Name"), null, new Receiver<Schedule>() {
         public void handle(Schedule var1x) {
            SelectScheduleMenuBuilder.this.controller.setSchedule(var1, var1x);
         }
      });
   }

   private Schedule getCurrentSchedule(OperationWrapperNode var1) {
      ScheduleId var2 = var1.getDataObject().getScheduleId();
      return this.controller.getProject().getItemLookup().getEntity(var2, Schedule.class);
   }

   private void addCreateNewItem(KongaMenu var1, OperationWrapperNode var2) {
      CreateNewScheduleAction var3 = new CreateNewScheduleAction(this.controller, var2);
      var1.add(var3, 0);
      var1.add(new Separator(), 1);
   }

   private void checkEnabledState(JMenu var1, OperationWrapperNode var2) {
      boolean var3 = var2.getDataObject().isSchedulable();
      var1.setEnabled(var3);
      if (!var3) {
         var1.setToolTipText(Strings.get("Schedule.Menu.Disabled"));
      }
   }
}
