package org.etlforge.integration.data.validation;

import com.google.common.base.Preconditions;

public final class ValidationMessage {
   public static final ValidationMessage VALID = new ValidationMessage(ValidationStatus.VALID, "");
   private final ValidationStatus status;
   private final String message;

   public ValidationMessage(ValidationStatus var1, String var2) {
      Preconditions.checkNotNull(var1, "status");
      Preconditions.checkNotNull(var2, "message");
      this.status = var1;
      this.message = var2;
   }

   public String message() {
      return this.message;
   }

   public ValidationStatus status() {
      return this.status;
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof ValidationMessage)) {
         return false;
      }

      ValidationMessage var2 = (ValidationMessage)var1;
      return this.message().equals(var2.message()) && this.status() == var2.status();
   }

   @Override
   public int hashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.status.hashCode();
      return 31 * var1 + this.message.hashCode();
   }

   @Override
   public String toString() {
      return this.status + " [" + this.message + "]";
   }
}
