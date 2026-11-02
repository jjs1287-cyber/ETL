package org.etlforge.integration.client.connect.db2xml.wizard;

import org.etlforge.ui.Size;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.widget.text.KongaTextField;
import org.etlforge.util.file.WindowsFileNameValidator;

final class FileNameField extends TextInputField2 {
   private final WindowsFileNameValidator validator = new WindowsFileNameValidator();

   public FileNameField(Size.Columns var1) {
      super("File Name", new KongaTextField(var1), "XSD file &name:");
      this.setSelectAllWhenFocused(true);
      this.setActive(true);
      this.decorateField();
   }

   @Override
   public boolean isInputValid(String var1) {
      return this.validator.isValid(var1);
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
}
