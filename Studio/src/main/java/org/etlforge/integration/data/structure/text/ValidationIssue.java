package org.etlforge.integration.data.structure.text;

public interface ValidationIssue {
   ValidationIssue.Factory FACTORY = new ValidationIssue.Factory() {
      @Override
      public ValidationIssue createFatal(final String var1) {
         return new ValidationIssue() {
            @Override
            public String getDescription() {
               return var1;
            }

            @Override
            public boolean preventsSave() {
               return true;
            }
         };
      }

      @Override
      public ValidationIssue createWarning(final String var1) {
         return new ValidationIssue() {
            @Override
            public String getDescription() {
               return var1;
            }

            @Override
            public boolean preventsSave() {
               return false;
            }
         };
      }
   };

   String getDescription();

   boolean preventsSave();

   interface Factory {
      ValidationIssue createFatal(String var1);

      ValidationIssue createWarning(String var1);
   }
}
