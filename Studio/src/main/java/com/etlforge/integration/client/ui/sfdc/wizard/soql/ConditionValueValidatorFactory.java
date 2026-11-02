package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

final class ConditionValueValidatorFactory {
   private static final boolean SUPPORTED = ApplicationConfiguration.getBoolean("SFDC.Soql.ValidateConditionValues", false);

   public static ConditionValueValidator forField(SObjectSchema.Field var0) {
      if (!SUPPORTED) {
         return new ConditionValueValidatorFactory.EverythingIsValid();
      }

      SObjectSchema.Field.Type var1 = var0.getType();
      return forType(var1);
   }

   public static ConditionValueValidator forType(SObjectSchema.Field.Type var0) {
      switch (var0) {
         case BOOLEAN:
            return new ConditionValueValidatorFactory.BooleanValidator();
         case ID:
            return new ConditionValueValidatorFactory.IdValidator();
         case INT:
            return new ConditionValueValidatorFactory.IntegerValidator();
         case DOUBLE:
            return new ConditionValueValidatorFactory.DoubleValidator();
         default:
            return new ConditionValueValidatorFactory.EverythingIsValid();
      }
   }

   private ConditionValueValidatorFactory() {
   }

   private static class BooleanValidator extends AbstractConditionValueValidator {
      private BooleanValidator() {
      }

      @Override
      protected String validateValue(String var1, Operator var2) {
         return !"true".equalsIgnoreCase(var1) && !"1".equals(var1) && !"false".equalsIgnoreCase(var1) && !"0".equals(var1)
            ? "The value must be either true (or 1), or false (or 0)."
            : null;
      }
   }

   private static class DoubleValidator extends AbstractConditionValueValidator {
      private DoubleValidator() {
      }

      @Override
      protected String validateValue(String var1, Operator var2) {
         try {
            Double.parseDouble(var1);
            return null;
         } catch (NumberFormatException var4) {
            return "'" + var1 + "' is not a valid double.";
         }
      }
   }

   private static class EverythingIsValid implements ConditionValueValidator {
      private EverythingIsValid() {
      }

      @Override
      public boolean isValid(String var1, Operator var2) {
         return true;
      }

      @Override
      public String whyNot() {
         throw new UnsupportedOperationException();
      }
   }

   private static class IdValidator extends AbstractConditionValueValidator {
      private IdValidator() {
      }

      @Override
      protected String validateValue(String var1, Operator var2) {
         int var3 = var1.length();
         switch (var2) {
            case EQUALS:
            case NOT_EQUALS:
               if (var3 != 15 && var3 != 18) {
                  return "An ID must be 15 or 18 characters long.";
               }

               return null;
            default:
               return var3 > 18 ? "An ID cannot be longer than 18 characters." : null;
         }
      }
   }

   private static class IntegerValidator extends AbstractConditionValueValidator {
      private IntegerValidator() {
      }

      @Override
      protected String validateValue(String var1, Operator var2) {
         try {
            Long.parseLong(var1);
            return null;
         } catch (NumberFormatException var4) {
            return "'" + var1 + "' is not a valid integer.";
         }
      }
   }
}
