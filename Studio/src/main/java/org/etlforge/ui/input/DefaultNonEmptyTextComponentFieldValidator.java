package org.etlforge.ui.input;

import javax.swing.text.JTextComponent;

public class DefaultNonEmptyTextComponentFieldValidator implements InputFieldValidator {
   @Override
   public boolean isFieldRequired(InputField var1) {
      return true;
   }

   @Override
   public boolean isFieldValid(InputField var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("field cannot be null.");
      } else if (!(var1.getInputComponent() instanceof JTextComponent)) {
         throw new IllegalArgumentException("field must be an instance of " + JTextComponent.class.getName() + ".");
      } else {
         return ((JTextComponent)var1.getInputComponent()).getText().trim().length() > 0;
      }
   }

   @Override
   public void validateField(InputField var1) throws InputFieldException {
      if (!this.isFieldValid(var1)) {
         throw new InputFieldException(var1, PackageResources.VALUE_CANNOT_BE_EMPTY);
      }
   }
}
