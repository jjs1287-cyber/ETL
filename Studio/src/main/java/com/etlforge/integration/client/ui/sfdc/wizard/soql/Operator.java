package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import java.util.EnumSet;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public enum Operator {
   EQUALS("equals", "=", "%s") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return true;
      }
   },
   NOT_EQUALS("not equals", "!=", "%s") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return true;
      }
   },
   LIKE("like", "like", "%s") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return Operator.isStringBased(var1);
      }
   },
   STARTS_WITH("starts with", "like", "%s%%") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return Operator.isStringBased(var1);
      }
   },
   ENDS_WITH("ends with", "like", "%%%s") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return Operator.isStringBased(var1);
      }
   },
   CONTAINS("contains", "like", "%%%s%%") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return Operator.isStringBased(var1);
      }
   },
   LESS_THAN("less than", "<", "%s") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return true;
      }
   },
   GREATER_THAN("greater than", ">", "%s") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return true;
      }
   },
   LESS_THAN_OR_EQUALS("less than or equals", "<=", "%s") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return true;
      }
   },
   GREATER_THAN_OR_EQUALS("greater than or equals", ">=", "%s") {
      @Override
      public boolean isApplicableTo(SObjectSchema.Field var1) {
         return true;
      }
   };

   private final String display;
   private final String op;
   private final String valueFormat;

   Operator(String var3, String var4, String var5) {
      this.display = var3;
      this.op = var4;
      this.valueFormat = var5;
   }

   public final String applyTo(SObjectSchema.Field var1, String var2) {
      String var3 = String.format(this.valueFormat, var2);
      if (isStringBased(var1)) {
         var3 = "'" + var3 + "'";
      }

      return var1.getName() + " " + this.op + " " + var3;
   }

   public abstract boolean isApplicableTo(SObjectSchema.Field var1);

   private static boolean isStringBased(SObjectSchema.Field var0) {
      switch (var0.getSoapType()) {
         case ID:
         case STRING:
            return true;
         default:
            return false;
      }
   }

   @Override
   public String toString() {
      return this.display;
   }

   public static EnumSet<Operator> getApplicableOperators(SObjectSchema.Field var0) {
      EnumSet var1 = EnumSet.noneOf(Operator.class);

      for (Operator var5 : values()) {
         if (var5.isApplicableTo(var0)) {
            var1.add(var5);
         }
      }

      return var1;
   }
}
