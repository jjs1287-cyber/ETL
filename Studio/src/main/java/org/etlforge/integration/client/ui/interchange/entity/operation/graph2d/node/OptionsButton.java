package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.ui.widget.button.KongaButton;

class OptionsButton extends OperationNodeToolBarDropDown {
   public OptionsButton(AbstractOperationNodeUi var1) {
      super(var1);
   }

   @Override
   protected JButton createButton() {
      Icon var1 = ApplicationResources.ICONS.getIcon("ArrowDown.16");
      return new KongaButton(var1).makeToolbarButton();
   }

   @Override
   protected JPopupMenu getPopupMenu(OperationNodeController var1) {
      return var1.getMenuProvider().getActionMenuFor(this.getNode());
   }

   @Override
   public String getToolTipText() {
      return "Options";
   }
}
