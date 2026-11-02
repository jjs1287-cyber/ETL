package com.etlforge.integration.client.ui.sfdc.wizard;

import com.etlforge.integration.client.ui.sfdc.ObjectSelectionController;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionModel;
import com.etlforge.integration.client.ui.sfdc.ObjectSelectionView;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.ValidStateBinding;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.util.Property;

public final class ObjectSelectionPage extends AbstractWizardPage {
   private final ObjectSelectionModel model;
   private final ObjectSelectionView view;
   private final ObjectSelectionController controller;

   public ObjectSelectionPage(SalesforceWizard<?> var1, ObjectSelectionModel var2, ObjectSelectionController var3) {
      super(var1, "Salesforce Object");
      this.model = var2;
      this.controller = var3;
      this.view = new ObjectSelectionView(var2, var3);
      this.bindToModel();
   }

   private void bindToModel() {
      Property var1 = this.model.getProperty("valid");
      Bindings var2 = new Bindings(ValidStateBinding.forBoolean(this, var1), new ObjectSelectionPage.DefaultActionBinding(this.model));
      var2.syncUi();
   }

   public ObjectSelectionModel getModel() {
      return this.model;
   }

   @Override
   protected void onFirstDisplay() {
      this.controller.getObjectList();
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(null);
      JPanel var2 = BorderLayoutBuilder.fillWith(this.view);
      var1.add(var2, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public void requestFocus() {
      this.view.requestFocus();
   }

   private class DefaultActionBinding extends AbstractBinding<Boolean> {
      public DefaultActionBinding(ObjectSelectionModel var2) {
         super(var2.getProperty("requiresExternalIdField"));
      }

      protected void updateUiWithValue(Boolean var1) {
         if (var1) {
            ObjectSelectionPage.this.view.setDefaultAction(ObjectSelectionPage.this.new FocusFieldSelectorAction());
         } else {
            ObjectSelectionPage.this.view.setDefaultAction(ObjectSelectionPage.this.new NextPageAction());
         }
      }
   }

   private final class FocusFieldSelectorAction extends AbstractSelectionAction {
      private FocusFieldSelectorAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ObjectSelectionPage.this.view.focusFieldSelector();
      }
   }

   private final class NextPageAction extends AbstractSelectionAction {
      private NextPageAction() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (ObjectSelectionPage.this.canContinue()) {
            ObjectSelectionPage.this.getWizard().nextPageRequested();
         }
      }
   }
}
