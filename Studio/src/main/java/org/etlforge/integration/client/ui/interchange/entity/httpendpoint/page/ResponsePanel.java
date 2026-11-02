package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import org.etlforge.application.ui.look.Colors;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.httpendpoint.ResponseType;
import org.etlforge.integration.data.entity.id.OperationId;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.RadioButtonInputField;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.label.KongaLabel;

public class ResponsePanel implements UiProvider {
   private final ResponseModel model;
   private final InputPanel panel;
   private final RadioButtonInputField noResponseSelector;
   private final RadioButtonInputField operationSelector;
   private final RadioButtonInputField variableSelector;
   private final TextInputField2 variableNameField;
   private final JLabel responseGeneratorLabel;
   private IntegrationEntityLookup operationLookup;

   public ResponsePanel(ResponseModel var1, InputPanel var2) {
      this.model = var1;
      this.panel = new InputPanel();
      ButtonGroup var3 = new ButtonGroup();
      ResponsePanel.RadioButtonListener var4 = new ResponsePanel.RadioButtonListener();
      this.noResponseSelector = this.createSelector("No Response", var3, var4);
      this.operationSelector = this.createSelector("Use final target as response", var3, var4);
      this.responseGeneratorLabel = new JLabel();
      this.variableSelector = this.createSelector("Use global variable for response", var3, var4);
      this.variableSelector.getInputComponent().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            ResponsePanel.this.variableNameField.requestFocus();
         }
      });
      this.variableNameField = this.createVariableField();
      var1.addPropertyChangeListener(new ResponsePanel.ModelListener());
      this.update();
      if (var2 != null) {
         var2.addInputPanel(this.panel);
      }

      this.panel.setSendInputChangeEvents(true);
      this.panel.setSendUndoEvents(true);
   }

   public void setOperationLookup(IntegrationEntityLookup var1) {
      this.operationLookup = var1;
      this.updateResponseGeneratorLabel();
   }

   public BoundProperty asBoundProperty() {
      return BoundPropertyFactory.textProperty("response_variable", this.variableNameField);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.doLayout();
      var1.display(this.panel.getUi());
   }

   @Override
   public void requestFocus() {
      this.noResponseSelector.requestFocus();
   }

   private RadioButtonInputField createSelector(String var1, ButtonGroup var2, ItemListener var3) {
      RadioButtonInputField var4 = new RadioButtonInputField("Response Setting", new KongaRadioButton(var1), null);
      var4.getInputComponent().addItemListener(var3);
      var2.add(var4.getInputComponent());
      return var4;
   }

   private TextInputField2 createVariableField() {
      TextInputField2 var1 = InputFieldFactory.getNonEmptyTextInputField("Response Variable Name", KongaLabel.withMnemonic("Variable name"), 24);
      var1.getDocument().addDocumentListener(new ResponsePanel.NameListener());
      this.panel.addInputField(var1, var1, var1);
      return var1;
   }

   private void update() {
      switch (this.model.getResponseType()) {
         case NONE:
            this.noResponseSelector.setSelected(true);
            this.variableNameField.setActive(false);
            this.responseGeneratorLabel.setEnabled(false);
            break;
         case OPERATION:
            this.noResponseSelector.setSelected(false);
            this.operationSelector.setSelected(true);
            this.responseGeneratorLabel.setEnabled(true);
            this.variableSelector.setSelected(false);
            this.variableNameField.setActive(false);
            break;
         case VARIABLE:
            this.noResponseSelector.setSelected(false);
            this.operationSelector.setSelected(false);
            this.responseGeneratorLabel.setEnabled(false);
            this.variableSelector.setSelected(true);
            this.variableNameField.setActive(true);
            this.variableNameField.setValue(this.model.getVariableName());
      }

      this.updateAsynchronousSettings();
      this.updateResponseGeneratorLabel();
   }

   private void updateResponseGeneratorLabel() {
      OperationId var1 = this.model.getResponseGenerator();
      if (var1 == null) {
         this.responseGeneratorLabel.setText("[Select an operation in the table above]");
         this.responseGeneratorLabel.setForeground(Colors.ERROR_TEXT_COLOR);
         this.responseGeneratorLabel.setIcon(null);
      } else {
         Operation var2 = this.operationLookup != null ? this.operationLookup.getEntity(var1, Operation.class) : null;
         this.responseGeneratorLabel.setText(var2 != null ? var2.getName() : var1.toString());
         this.responseGeneratorLabel.setForeground(javax.swing.UIManager.getColor("Label.foreground"));
         this.responseGeneratorLabel.setIcon(EntityIcons.forType(EntityType.Operation).getDefaultIcon(EntityIcons.Size.SMALL));
      }
   }

   private void updateAsynchronousSettings() {
      boolean var1 = this.model.isAsynchronous();
      this.operationSelector.setEnabled(!var1);
      this.variableSelector.setEnabled(!var1);
   }

   private void doLayout() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 8, 0);
      var1.add(InputFieldFactory.getCapsule(this.noResponseSelector));
      var1.add(InputFieldFactory.getCapsule(this.operationSelector));
      var1.withInsets(0, 0, 4, 0);
      this.responseGeneratorLabel.setBorder(Empty.border(0, 24, 4, 0));
      var1.add(this.responseGeneratorLabel);
      var1.add(InputFieldFactory.getCapsule(this.variableSelector));
      JComponent var2 = InputFieldFactory.getCapsule(this.variableNameField);
      var2.setBorder(Empty.border(0, 24, 0, 0));
      var1.add(var2);
      this.panel.wrap(var1);
      this.panel.setBorder(StyleSheet.getDefaultTitleBorder("Response Settings"));
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         ResponsePanel.this.panel.setSendInputChangeEvents(false);
         ResponsePanel.this.panel.setSendUndoEvents(false);

         try {
            String var2 = var1.getPropertyName();
            if (var2 == null) {
               ResponsePanel.this.update();
            } else if ("response_type".equals(var2)) {
               ResponsePanel.this.update();
            } else if ("variable_name".equals(var2)) {
               ResponsePanel.this.variableNameField.setValue(ResponsePanel.this.model.getVariableName());
            } else if ("response_generator".equals(var2)) {
               ResponsePanel.this.updateResponseGeneratorLabel();
            } else if ("asynchronous".equals(var2)) {
               ResponsePanel.this.updateAsynchronousSettings();
            }
         } finally {
            ResponsePanel.this.panel.setSendInputChangeEvents(true);
            ResponsePanel.this.panel.setSendUndoEvents(true);
         }
      }
   }

   private class NameListener extends DocumentContentChangeListener {
      private NameListener() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         ResponsePanel.this.model.setVariableNameSilently(ResponsePanel.this.variableNameField.getValue().trim());
      }
   }

   private class RadioButtonListener implements ItemListener {
      private RadioButtonListener() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         if (ResponsePanel.this.noResponseSelector.isSelected()) {
            ResponsePanel.this.model.setResponseType(ResponseType.NONE);
         } else if (ResponsePanel.this.operationSelector.isSelected()) {
            ResponsePanel.this.model.setResponseType(ResponseType.OPERATION);
         } else if (ResponsePanel.this.variableSelector.isSelected()) {
            ResponsePanel.this.model.setResponseType(ResponseType.VARIABLE);
         }
      }
   }
}

