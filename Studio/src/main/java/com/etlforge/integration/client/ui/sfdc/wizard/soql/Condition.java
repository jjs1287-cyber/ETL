package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

public final class Condition {
   public static final Function<Condition, String> TO_SOQL = new Function<Condition, String>() {
      public String apply(Condition var1) {
         return var1.toSoql();
      }
   };
   private final SObjectSchema.Field field;
   private final Operator operator;
   private final String value;

   public Condition(SObjectSchema.Field var1, Operator var2, String var3) {
      this.field = Preconditions.checkNotNull(var1);
      this.operator = Preconditions.checkNotNull(var2);
      this.value = Preconditions.checkNotNull(var3);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof Condition)) {
         return false;
      }

      Condition var2 = (Condition)var1;
      return this.field.equals(var2.field) && this.operator == var2.operator && this.value.equals(var2.value);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.field, this.operator, this.value);
   }

   public String toSoql() {
      return this.operator.applyTo(this.field, this.value);
   }

   @Override
   public String toString() {
      return this.toSoql();
   }
}
