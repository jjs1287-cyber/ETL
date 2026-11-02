package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import java.awt.EventQueue;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ldap.ui.ExtendedAttributesSelectorUi;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.util.UiUtils;

final class SetExtendedAttributesAction extends AbstractClassNodeAction {
   private ExtendedAttributesSelectorUi ui;
   private ClassNameNode node;

   public SetExtendedAttributesAction(AttributeSelectionTree var1) {
      super(var1, "Set &Extended Attributes...");
   }

   @Override
   protected void runOnNode(ClassNameNode var1) {
      this.node = var1;
      this.ui = this.createSelector();
      this.openDialog(var1);
   }

   private ExtendedAttributesSelectorUi createSelector() {
      ExtendedAttributesSelectorUi var1 = new ExtendedAttributesSelectorUi();
      LdapStructureAttribute[] var2 = this.getModel().getExtendedAttributes(this.node.getOid());
      var1.setAttributes(var2);
      return var1;
   }

   private void openDialog(ClassNameNode var1) {
      KongaDialog var2 = this.createDialog();
      var2.setVisible(true);
      if (var2.wasOkPressed()) {
         if (this.ui.isValid()) {
            this.applyAttributes();
         } else {
            Alert.error("Invalid input. Make sure all attributes have valid names.", "Invalid Input");
            this.openDialog(var1);
         }
      }
   }

   private KongaDialog createDialog() {
      KongaDialog var1 = new KongaDialog(UiUtils.getActiveWindow(), "Define Extended Attributes");
      String var2 = "Define the extended attributes to use for " + this.node.getUserObject().getName();
      CaptionLayout var3 = CaptionLayout.of(var2, this.ui);
      var1.standardLayout(var3, KongaDialog.OK_CANCEL);
      var1.manageLocation(SetExtendedAttributesAction.class);
      var1.setOkButtonAsDefault();
      return var1;
   }

   private void applyAttributes() {
      this.getModel().setExtendedAttributes(this.node.getOid(), this.ui.getAttributes());
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SetExtendedAttributesAction.this.getTree().ensureExtendedAttributesAreSelected(SetExtendedAttributesAction.this.node.getOid());
         }
      });
   }
}
