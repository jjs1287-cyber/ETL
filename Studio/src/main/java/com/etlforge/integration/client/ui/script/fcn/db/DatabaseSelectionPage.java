package com.etlforge.integration.client.ui.script.fcn.db;

import com.google.common.collect.Iterables;
import com.etlforge.integration.client.ui.script.fcn.TreeSelectorLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.entity.selector.EntityTreeSelector;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

final class DatabaseSelectionPage extends AbstractWizardPage {
   private final EntityTreeSelector<IntegrationEntity> selector;

   public DatabaseSelectionPage(Wizard var1, List<Locatable> var2) {
      super(var1, "Select a Database");
      this.selector = this.createSelector(var2);
      this.updateNextPageEnabled();
   }

   private void updateNextPageEnabled() {
      this.setCanContinue(this.selector.getSelectedEntity() != null);
   }

   private EntityTreeSelector<IntegrationEntity> createSelector(List<Locatable> var1) {
      EntityTreeSelector var2 = EntityTreeSelector.newTree(Iterables.filter(var1, IntegrationEntity.class), IntegrationEntity.class);
      var2.collapseAll();
      var2.expandRootFolders();
      var2.setProjectVisible(false);
      var2.getSelectionSource().addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            DatabaseSelectionPage.this.updateNextPageEnabled();
         }
      });
      var2.setDefaultAction(new AbstractSingleObjectSelectionAction<Locatable>(Locatable.class) {
         protected void run(Locatable var1, ActionEvent var2x) {
            DatabaseSelectionPage.this.getWizard().nextPageRequested();
         }
      });
      return var2;
   }

   public Locatable getSelectedDatabase() {
      IntegrationEntity var1 = this.selector.getSelectedEntity();
      return var1 instanceof Locatable ? (Locatable)var1 : null;
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout("Select a Database");
      var1.add(new TreeSelectorLayout(this.selector, false), true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DatabaseSelectionPage.this.selector.requestFocus();
         }
      });
   }
}
