package com.etlforge.integration.client.ui.sfdc.query.page.v4;

import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputFieldException;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class BatchSizeField implements UiProvider {
   private final IntegerInputField field;

   public BatchSizeField() {
      KongaLabel var1 = KongaLabel.withMnemonic("&Batch size:");
      this.field = new IntegerInputField("Batch size", new KongaTextField(6), 200, 2000, var1) {
         @Override
         public String getInvalidMessage(String var1) {
            return "Batch size must be >= 200 and <= 2000";
         }
      };
      this.field.setSelectAllWhenFocused(true);
      this.field.setValue(2000);
   }

   public void registerWithInputPanel(InputPanel var1) {
      var1.addInputField(this.field, this.field, this.field);
   }

   public void validate() throws InputFieldException {
      this.field.validateField();
   }

   public boolean isValid() {
      return this.field.isFieldValid();
   }

   public int getBatchSize() {
      return this.field.getIntValue(2000);
   }

   public void setBatchSize(int var1) {
      this.field.setValue(var1);
   }

   public void addChangeListener(DocumentListener var1) {
      this.field.getDocument().addDocumentListener(var1);
   }

   public void addActionListener(ActionListener var1) {
      ((JTextField)this.field.getInputComponent()).addActionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JPanel var2 = InvisiblePanel.of(BoxBuilder.horizontal().addAllWithSpace(10, this.field.getLabel(), this.field.getInputComponent()));
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   public InputField asInputFieldForLayout() {
      return this.field;
   }
}
