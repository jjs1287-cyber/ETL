package org.etlforge.integration.client.ui.interchange.entity.webservice.xsd;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import org.etlforge.application.ui.Alert;
import org.etlforge.system.Disposable;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.ActionEnabledBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.property.WaitStateBinding;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.FileNameField;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.util.Property;

public final class SoapSchemaUi implements UiProvider, Disposable {
   private final SoapSchemaModel model;
   private final SoapSchemaController controller;
   private final FileNameField nameField = new FileNameField("File Name", "File name:", 30);
   private final SoapSchemaUi.GenerateXsdsAction generateXsdsAction = new SoapSchemaUi.GenerateXsdsAction();
   private final Bindings bindings = new Bindings();
   private final WaitStateBinding waitState;
   private final KongaButton generateButton;
   private JButton cancelButton;

   public SoapSchemaUi(SoapSchemaModel var1, SoapSchemaController var2) {
      this.model = var1;
      this.controller = var2;
      this.nameField.addActionListener(this.generateXsdsAction);
      this.nameField.setMaxLength(var1.getNameMaxLength());
      this.waitState = this.bindWaitState();
      this.generateButton = new KongaButton(this.generateXsdsAction);
      this.bindToModel();
   }

   private WaitStateBinding bindWaitState() {
      Property var1 = this.model.getProperty("busy");
      WaitStateBinding var2 = new WaitStateBinding(var1);
      this.bindings.add(var2);
      return var2;
   }

   private void bindToModel() {
      Property var1 = this.model.getProperty("name");
      this.bindings.add(new TextBinding(var1, this.nameField.getInputComponent()));
      this.bindings.add(ActionEnabledBinding.forBoolean(this.generateXsdsAction, this.model, "valid"));
      this.bindings.add(new SoapSchemaUi.ConflictsBinding(this.model));
      this.bindings.syncUi();
   }

   @Override
   public void dispose() {
      this.bindings.dispose();
   }

   public KongaButton getGenerateButton() {
      return this.generateButton;
   }

   public void setWaitService(WaitService var1) {
      this.waitState.setWaitService(var1);
   }

   public void setCancelButton(JButton var1) {
      this.cancelButton = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel()
         .withInsets(0, 0, 10, 0)
         .withBorder(Empty.border(10))
         .setWeightX(1.0)
         .setFill(GridBagLayoutPanel.Fill.HORIZONTAL)
         .setAnchor(GridBagLayoutPanel.Anchor.WEST);
      var2.add("Generate XML schemas representing the request and response of this Web Service Call.");
      BoxBuilder var3 = BoxBuilder.horizontal();
      var3.addAllWithSpace(5, this.nameField.getLabel(), this.nameField.getInputComponent());
      var2.add(var3);
      var2.add("This name will be used as basis for the request and response XSDs.");
      var2.setFillNone();
      var2.setAnchor(GridBagLayoutPanel.Anchor.EAST);
      BoxBuilder var4 = BoxBuilder.horizontal().glue().add(this.generateButton);
      if (this.cancelButton != null) {
         var4.rigid(10).add(this.cancelButton);
      }

      var2.add(var4);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.nameField.requestFocus();
   }

   private class ConflictsBinding extends AbstractBinding<Boolean> {
      public ConflictsBinding(SoapSchemaModel var2) {
         super(var2, "conflicts");
      }

      protected void updateUiWithValue(Boolean var1) {
         if (var1) {
            this.showConflictsDialog();
         }
      }

      private void showConflictsDialog() {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ConflictsDialog var1 = new ConflictsDialog(SoapSchemaUi.this.controller);
               var1.open(SoapSchemaUi.this.nameField.getInputComponent());
            }
         });
      }
   }

   private class GenerateXsdsAction extends AbstractAction {
      public GenerateXsdsAction() {
         super("Generate XSDs...");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               if (GenerateXsdsAction.this.isEnabled() && GenerateXsdsAction.this.isValid()) {
                  SoapSchemaUi.this.controller.generateWithCaution();
               }
            }
         });
      }

      private boolean isValid() {
         if (SoapSchemaUi.this.model.isValid()) {
            return true;
         }

         Alert.error(SoapSchemaUi.this.nameField.getInputComponent(), "Invalid file name.", "Error");
         return false;
      }
   }
}
