package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Supplier;
import com.etlforge.integration.client.sfdc.facade.SalesforceFacade;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceUpsert;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

public final class ExternalIdFieldSelector implements UiProvider {
   private final SalesforceUpsert upsert;
   private final SalesforceFacade sfdcFacade;
   private final TextInputField textField;
   private final ExternalIdFieldSelector.EditAction editAction;
   private volatile Supplier<SalesforceConnector> connectorSupplier;

   public ExternalIdFieldSelector(SalesforceUpsert var1, SalesforceFacade var2) {
      this.upsert = var1;
      this.sfdcFacade = var2;
      this.textField = createTextField(var1);
      this.editAction = new ExternalIdFieldSelector.EditAction();
   }

   private static TextInputField createTextField(SalesforceUpsert var0) {
      TextInputField var1 = new TextInputField("External ID field", new KongaTextField(Size.columns(20)));
      var1.setValue(var0.getExternalIdFieldName());
      var1.setEditable(false);
      return var1;
   }

   public void registerAsChangeSource(InputPanel var1) {
      var1.addInputField(this.textField);
   }

   public void setConnectorSupplier(Supplier<SalesforceConnector> var1) {
      this.connectorSupplier = var1;
   }

   public String getSelectedFieldName() {
      return this.textField.getValue();
   }

   public void apply() {
      this.upsert.setExternalIdFieldName(this.textField.getValue());
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent[] var2 = this.getLayoutComponents();
      BoxBuilder var3 = BoxBuilder.horizontal().nonOpaque();
      var3.addAllWithSpace(5, var2);
      var3.decorate(var1);
   }

   public JComponent[] getLayoutComponents() {
      KongaLabel var1 = KongaLabel.withMnemonic("E&xternal ID field:");
      JButton var2 = new JButton(this.editAction);
      var1.setLabelForButton(var2);
      return new JComponent[]{var1, this.textField.getInputComponent(), InvisiblePanel.of(var2)};
   }

   @Override
   public void requestFocus() {
      this.textField.requestFocus();
   }

   private class EditAction extends AbstractAction {
      public EditAction() {
         super("Edit");
         this.putValue("ShortDescription", "Select another external ID field");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditAction.this.edit();
            }
         });
      }

      private void edit() {
         SalesforceConnector var1 = ExternalIdFieldSelector.this.connectorSupplier.get();
         if (var1 == null) {
            Alert.error(ApplicationUi.getAppWinFrame(), "Must select a Salesforce Connector.", "Cannot connect");
         } else {
            SObjectSchema.Field var2 = this.selectField(var1);
            this.updateUiWithNewField(var2);
         }
      }

      private SObjectSchema.Field selectField(SalesforceConnector var1) {
         String var2 = ExternalIdFieldSelector.this.upsert.getSalesforceObjectName().getLocalName();
         ExternalFieldListProducer var3 = new ExternalFieldListProducer(ExternalIdFieldSelector.this.sfdcFacade, var1, var2);
         ExternalFieldListDialog var4 = new ExternalFieldListDialog(var3);
         return var4.open();
      }

      private void updateUiWithNewField(SObjectSchema.Field var1) {
         if (var1 != null) {
            final String var2 = var1.getName();
            if (!var2.equals(ExternalIdFieldSelector.this.textField.getValue())) {
               ExternalIdFieldSelector.this.textField.requestFocus();
               EventQueue.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     ExternalIdFieldSelector.this.textField.setValue(var2);
                  }
               });
            }
         }
      }
   }
}
