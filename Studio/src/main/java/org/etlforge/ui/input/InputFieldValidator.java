package org.etlforge.ui.input;

public interface InputFieldValidator {
   boolean isFieldRequired(InputField var1);

   boolean isFieldValid(InputField var1);

   void validateField(InputField var1) throws InputFieldException;
}
