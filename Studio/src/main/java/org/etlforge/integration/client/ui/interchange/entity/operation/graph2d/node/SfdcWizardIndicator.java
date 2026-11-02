package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import com.google.common.base.Preconditions;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.widget.button.KongaButton;

final class SfdcWizardIndicator extends OperationNodeToolBarDropDown {
   public SfdcWizardIndicator(AbstractOperationNodeUi var1) {
      super(var1);
      Operation var2 = this.getOperation();
      Preconditions.checkArgument(var2.isImplementingSalesforcePartnerWizard(), "The operation does not implement a Salesforce Wizard");
   }

   @Override
   protected boolean isActive() {
      return true;
   }

   @Override
   public String getToolTipText() {
      return "Implements a " + this.getWizardType().getDisplayName() + " Wizard. Click to access.";
   }

   private EntityType getWizardType() {
      Operation var1 = this.getNode().getDataObject().getActivity();
      return var1.getConnectType();
   }

   @Override
   protected JPopupMenu getPopupMenu(OperationNodeController var1) {
      return var1.getMenuProvider().getSfdcWizardMenuFor(this.getNode());
   }

   @Override
   protected JButton createButton() {
      Icon var1 = EntityIcons.forType(this.getWizardType()).getDefaultIcon(EntityIcons.Size.MEDIUM);
      return new KongaButton(var1).makeToolbarButton();
   }
}
