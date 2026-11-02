package com.etlforge.integration.client.ui.sfdc.bulkload.wizard;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import java.awt.EventQueue;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.salesforce.bulkload.BulkAction;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringCollection;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class ActionSelectionPage extends AbstractWizardPage {
   private final RadioButtonGroupPanel<BulkAction> selector;
   private final ObjectSelectionModel objectModel;

   public ActionSelectionPage(BulkLoadWizard var1, ObjectSelectionModel var2) {
      super(var1, "Action");
      this.objectModel = var2;
      this.selector = this.createSelector();
      this.updateWizardSections(this.getSelectedAction());
      this.listenToSelection();
      this.setCanContinue(true);
   }

   private RadioButtonGroupPanel<BulkAction> createSelector() {
      RadioButtonGroupPanel var1 = new RadioButtonGroupPanel();
      MnemonicStringCollection var2 = new MnemonicStringCollection();

      for (BulkAction var6 : BulkAction.values()) {
         MnemonicString var7 = var2.autoTranslate(var6.toString());
         var1.addOption(var6, var7.getRaw());
      }

      var1.setSelectedValue(BulkAction.DEFAULT);
      return var1;
   }

   private void updateWizardSections(BulkAction var1) {
      BulkLoadWizard var2 = (BulkLoadWizard)this.getWizard();
      var2.updateSectionsForAction(var1);
   }

   private void listenToSelection() {
      this.selector.addSelectionListener(new Receiver<BulkAction>() {
         public void handle(BulkAction var1) {
            ActionSelectionPage.this.updateWizardSections(var1);
         }
      });
   }

   public BulkAction getSelectedAction() {
      return this.selector.getSelection();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Bulk Process Action");
      var1.add(this.layoutSelector(), true, false);
      JPanel var2 = var1.asNorthBorderLayout();
      this.getUndoRedoInstaller().installIntoComponentTree(var2);
      return var2;
   }

   private JPanel layoutSelector() {
      JPanel var1 = InvisiblePanel.of(this.selector.oneRowLayout());
      var1.setBorder(Empty.border(8, 8, 0, 0));
      return var1;
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ActionSelectionPage.this.selector.requestFocus();
         }
      });
   }

   @Override
   public boolean prepareForForwardNavigation() {
      UiThreadSupplier.call(new Supplier<Void>() {
         public Void get() {
            BulkAction var1 = ActionSelectionPage.this.selector.getSelection();
            ActionSelectionPage.this.objectModel.setBulkAction(var1);
            return null;
         }
      });
      return true;
   }
}
