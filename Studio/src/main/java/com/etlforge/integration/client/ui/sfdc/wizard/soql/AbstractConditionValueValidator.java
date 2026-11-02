package com.etlforge.integration.client.ui.sfdc.wizard.soql;

abstract class AbstractConditionValueValidator implements ConditionValueValidator {
   private String reason;

   @Override
   public final boolean isValid(String var1, Operator var2) {
      this.reason = this.validateValue(var1, var2);
      return this.reason == null;
   }

   protected abstract String validateValue(String var1, Operator var2);

   @Override
   public final String whyNot() {
      if (this.reason == null) {
         throw new RuntimeException("I should not be called unless isValid returned false.");
      } else {
         return this.reason;
      }
   }
}
