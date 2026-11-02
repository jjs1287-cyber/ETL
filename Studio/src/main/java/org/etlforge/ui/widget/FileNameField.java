package org.etlforge.ui.widget;

import java.awt.event.ActionListener;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.file.WindowsFileNameValidator;

public final class FileNameField extends TextInputField2 {
   private final WindowsFileNameValidator validator = new WindowsFileNameValidator();

   public FileNameField(String var1, String var2, int var3) {
      super(var1, new KongaTextField(var3), var2);
      this.setSelectAllWhenFocused(true);
      this.setAutoTrim(true);
      this.setActive(true);
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
      this.decorateField();
      KongaTextField var4 = this.getInputComponent();
      var4.setLabel(this.getLabel());
   }

   public void setMaxLength(int var1) {
      this.validator.setMaxLength(var1);
   }

   @Override
   public boolean isInputValid(String var1) {
      return var1.isEmpty() && !this.isRequired() ? true : this.validator.isValid(var1);
   }

   @Override
   public String getInvalidMessage(String var1) {
      try {
         this.validator.validate(var1);
         return null;
      } catch (Exception var3) {
         return var3.getMessage();
      }
   }

   public KongaTextField getInputComponent() {
      return (KongaTextField)super.getInputComponent();
   }

   public void addActionListener(ActionListener var1) {
      this.getInputComponent().addActionListener(var1);
   }

   public UiProvider asUiProvider() {
      return this.getInputComponent().asUiProvider();
   }
}
