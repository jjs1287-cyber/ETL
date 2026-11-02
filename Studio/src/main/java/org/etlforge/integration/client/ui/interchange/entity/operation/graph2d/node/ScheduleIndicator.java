package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.widget.button.KongaButton;

final class ScheduleIndicator extends OperationNodeToolBarDropDown {
   public ScheduleIndicator(AbstractOperationNodeUi var1) {
      super(var1);
      this.addProperty("schedule");
   }

   @Override
   protected JButton createButton() {
      Icon var1 = EntityIcons.forType(EntityType.Schedule).getDefaultIcon(EntityIcons.Size.MEDIUM);
      return new KongaButton(var1).makeToolbarButton();
   }

   @Override
   protected JPopupMenu getPopupMenu(OperationNodeController var1) {
      return var1.getMenuProvider().getScheduleMenuFor(this.getNode());
   }

   @Override
   public String getToolTipText() {
      String var1 = null;
      if (this.isActive()) {
         var1 = this.getToolTipWhenActive();
      }

      return var1;
   }

   private String getToolTipWhenActive() {
      OperationWrapperNode var1 = this.getNode();
      Schedule var2 = var1.getDataObject().getSchedule();
      return var2 != null ? ToolTipStringFactory.getToolTip(var2) : null;
   }

   @Override
   protected boolean isActive() {
      return this.getNode().getDataObject().getScheduleId() != null;
   }
}
