package org.etlforge.integration.data.validation;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

public interface ValidationResult {
   ValidationResult VALID = new ValidationResult() {
      @Override
      public ImmutableList<ValidationMessage> allMessages() {
         return ImmutableList.of();
      }

      @Override
      public String concatenateMessages() {
         return "";
      }

      @Override
      public boolean isEmpty() {
         return true;
      }

      @Override
      public boolean isFatal() {
         return false;
      }

      @Override
      public ValidationStatus status() {
         return ValidationStatus.VALID;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            return var1 instanceof ValidationResult ? this.equals((ValidationResult)var1) : false;
         }
      }

      private boolean equals(ValidationResult var1) {
         return var1.status() == ValidationStatus.VALID && var1.isEmpty();
      }

      @Override
      public int hashCode() {
         return ValidationStatus.VALID.hashCode();
      }
   };
   Function<ValidationResult, ValidationStatus> TO_STATUS = new Function<ValidationResult, ValidationStatus>() {
      public ValidationStatus apply(ValidationResult var1) {
         return var1.status();
      }
   };

   boolean isEmpty();

   ValidationStatus status();

   boolean isFatal();

   ImmutableList<ValidationMessage> allMessages();

   String concatenateMessages();

   @Override
   boolean equals(Object var1);
}
