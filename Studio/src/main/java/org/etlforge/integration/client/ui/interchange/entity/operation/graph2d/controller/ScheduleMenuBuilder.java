package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import javax.swing.JPopupMenu;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

class ScheduleMenuBuilder {
   private final OperationGraphController controller;

   public ScheduleMenuBuilder(OperationGraphController var1) {
      this.controller = var1;
   }

   public KongaMenu buildMenu(OperationWrapperNode var1) {
      KongaMenu var2 = new KongaMenu("Schedule", EntityIcons.forType(EntityType.Schedule).getDefaultIcon(EntityIcons.Size.SMALL));
      var2.addItems(this.getItems(var1));
      return var2;
   }

   public JPopupMenu buildPopupMenu(OperationWrapperNode var1) {
      KongaPopupMenu var2 = new KongaPopupMenu();
      var2.addItems(this.getItems(var1));
      return var2;
   }

   private Object[] getItems(OperationWrapperNode var1) {
      return new Object[]{
         new SelectScheduleMenuBuilder(this.controller).build(var1),
         new SchedulePolicyMenuBuilder(this.controller).buildMenu(var1),
         new EditScheduleAction(this.controller, var1),
         new RemoveScheduleAction(this.controller, var1)
      };
   }
}
