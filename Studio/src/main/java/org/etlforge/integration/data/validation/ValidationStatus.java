package org.etlforge.integration.data.validation;

public enum ValidationStatus {
   VALID(1),
   WARNING(100),
   INVALID(10000);

   private final int severity;

   ValidationStatus(int var3) {
      this.severity = var3;
   }

   public boolean isMoreSevere(ValidationStatus var1) {
      return this.severity > var1.severity;
   }

   public static ValidationStatus mostSevere(Iterable<ValidationStatus> var0) {
      ValidationStatus var1 = VALID;

      for (ValidationStatus var3 : var0) {
         if (var3 == INVALID) {
            return var3;
         }

         if (var3 == WARNING) {
            var1 = var3;
         } else if (var3 == null) {
            throw new NullPointerException("null status encountered");
         }
      }

      return var1;
   }
}
