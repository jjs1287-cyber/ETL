package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.Icon;
import javax.swing.JButton;
import org.etlforge.ui.widget.button.KongaButton;

abstract class NodeExpansionToggleButton extends OperationNodeToolBarButton {
   public NodeExpansionToggleButton(AbstractOperationNodeUi var1) {
      super(var1);
   }

   protected abstract Icon getToggleButtonIcon();

   @Override
   protected JButton createButton() {
      return new KongaButton(this.getToggleButtonIcon()).makeToolbarButton();
   }

   @Override
   public String getToolTipText() {
      return "Toggle the node expansion mode";
   }
}
