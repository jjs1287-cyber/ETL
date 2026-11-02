package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.ui.Size;
import org.etlforge.ui.widget.text.KongaTextField;

final class ConditionValueField {
   private final KongaTextField textField = new KongaTextField(Size.columns(20));
   private final ConditionValueAutoCompleter autoCompleter = new ConditionValueAutoCompleter(this.textField);

   public void addActionListener(ActionListener var1) {
      this.textField.addActionListener(var1);
   }

   public void setField(SObjectSchema.Field var1) {
      this.textField.setText("");
      this.autoCompleter.setField(var1);
   }

   public String getValue() {
      return this.textField.getText();
   }

   public JComponent getUi() {
      return this.textField;
   }
}
