package com.etlforge.integration.client.ui.script.fcn.runop;

import com.etlforge.integration.client.ui.script.fcn.TreeSelectorLayout;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.entity.selector.EntityTreeSelector;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

final class OperationSelectionPage extends AbstractWizardPage {
   private final EntityTreeSelector<Operation> operationSelector;

   public OperationSelectionPage(Wizard<?> var1, List<Operation> var2) {
      super(var1, "Run Operation Wizard");
      this.operationSelector = this.createSelector(var2);
      this.operationSelector.getSelectionSource().addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            OperationSelectionPage.this.updateNavigation();
         }
      });
      this.updateNavigation();
   }

   private EntityTreeSelector<Operation> createSelector(List<Operation> var1) {
      EntityTreeSelector var2 = EntityTreeSelector.newTree(var1, Operation.class);
      var2.collapseAll();
      var2.expandRootFolders();
      var2.setProjectVisible(false);
      var2.getSelectionSource().addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            OperationSelectionPage.this.updateNavigation();
         }
      });
      return var2;
   }

   private void updateNavigation() {
      this.setCanContinue(this.getSelectedOperation() != null);
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Select the operation to run");
      var1.add(new TreeSelectorLayout(this.operationSelector, true), true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            OperationSelectionPage.this.operationSelector.requestFocus();
         }
      });
   }

   public Operation getSelectedOperation() {
      return this.operationSelector.getSelectedEntity();
   }
}
