package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Predicate;

public final class ScriptValidationFilter implements Predicate<FieldValidation> {
   public boolean apply(FieldValidation var1) {
      ValidationRule var2 = var1.getRule().orNull();
      if (var2 == null) {
         return false;
      }

      OperatorArgument var3 = var2.getArgument();
      return var3 instanceof ScriptArgument;
   }

   @Override
   public String toString() {
      return "FieldValidation calling Script";
   }
}
