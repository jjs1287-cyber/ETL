package org.etlforge.ui.input;

public class InputFieldException extends Exception {
   private final InputField inputField;

   public InputFieldException(InputField var1, String var2) {
      super(var2);
      this.inputField = var1;
   }

   public InputField getInputField() {
      return this.inputField;
   }

   @Override
   public String getMessage() {
      return super.getMessage();
   }
}
