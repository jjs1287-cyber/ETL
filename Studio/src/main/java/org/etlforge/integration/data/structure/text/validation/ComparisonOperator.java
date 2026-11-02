package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import org.etlforge.integration.data.structure.text.FieldDataType;

final class ComparisonOperator extends Operator {
   private static final ImmutableSet<String> NAMES = ImmutableSet.of("<", "<=", ">", ">=", "=", "!=");

   static ImmutableSet<ComparisonOperator> allComparisonOperators() {
      Builder var0 = ImmutableSet.builder();

      for (String var2 : NAMES) {
         var0.add(new ComparisonOperator(var2));
      }

      return var0.build();
   }

   public ComparisonOperator(String var1) {
      super(var1, StringArgument.class);
      Preconditions.checkArgument(NAMES.contains(var1), "Unknown comparison operator: " + var1);
   }

   @Override
   public boolean isApplicableToField(FieldDataType var1) {
      return !this.getName().equals("=") && !this.getName().equals("!=") ? var1.isNumeric() || var1 == FieldDataType.Date : true;
   }
}
