package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node;

import javax.swing.JTextField;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.graph2d.DefaultTextFieldEditor;
import org.etlforge.ui.input.TextInputField2;

public class EntityNodeNameEditor extends DefaultTextFieldEditor {
   private final IntegrationEntity entity;
   private TextInputField2 inputField;

   public EntityNodeNameEditor(Object var1, IntegrationEntity var2) {
      super(var1);
      this.entity = var2;
   }

   @Override
   public boolean stopCellEditing() {
      if (this.inputField.isFieldValid()) {
         this.fireEditingStopped();
         return true;
      } else {
         this.showErrorMessage();
         return false;
      }
   }

   private void showErrorMessage() {
      Alert.error(this.inputField.getInvalidMessage(this.getCellEditorValue()), "Invalid Name");
   }

   @Override
   protected JTextField createTextField(Object var1) {
      this.inputField = InterchangeViewUtils.getIntegrationEntityNameInputField(this.entity, "", null, 30);
      this.inputField.setSelectAllWhenFocused(true);
      this.inputField.setShowValidationMessageInToolTip(false);
      return (JTextField)this.inputField.getInputComponent();
   }
}
