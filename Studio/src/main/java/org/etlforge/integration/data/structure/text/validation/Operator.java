package org.etlforge.integration.data.structure.text.validation;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMap.Builder;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.system.ArgCheck;

public abstract class Operator {
   private static final ImmutableMap<String, Operator> allOperators = buildAllOperators();
   private final String name;
   private final Class<? extends OperatorArgument> argClass;

   private static ImmutableMap<String, Operator> buildAllOperators() {
      Builder var0 = ImmutableMap.builder();

      for (Operator var2 : ComparisonOperator.allComparisonOperators()) {
         var0.put(var2.getName(), var2);
      }

      for (Operator var4 : ImmutableSet.of(
         new BetweenOperator(),
         new NotBetweenOperator(),
         new ContainsOperator(),
         new NotContainsOperator(),
         new OneOfOperator(),
         new NotOneOfOperator(),
         new RegexOperator(),
         new ScriptOperator()
      )) {
         var0.put(var4.getName(), var4);
      }

      return var0.build();
   }

   public static Operator fromName(String var0) {
      Operator var1 = allOperators.get(var0);
      if (var1 == null) {
         throw new IllegalArgumentException("Unknown validation operator: " + var0);
      } else {
         return var1;
      }
   }

   public static ImmutableList<Operator> listAll() {
      return allOperators.values().asList();
   }

   public static ImmutableList<Operator> listApplicable(final FieldDataType var0) {
      Predicate var1 = new Predicate<Operator>() {
         public boolean apply(Operator var1) {
            return var1.isApplicableToField(var0);
         }
      };
      return FluentIterable.from(allOperators.values()).filter(var1).toImmutableList();
   }

   public Operator(String var1, Class<? extends OperatorArgument> var2) {
      this.name = ArgCheck.checkNotEmpty(var1);
      this.argClass = Preconditions.checkNotNull(var2);
   }

   public final String getName() {
      return this.name;
   }

   public abstract boolean isApplicableToField(FieldDataType var1);

   public final Class<? extends OperatorArgument> getArgumentClass() {
      return this.argClass;
   }

   @Override
   public final int hashCode() {
      return this.name.hashCode();
   }

   @Override
   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 == null ? false : this.getClass().equals(var1.getClass()) && this.name.equals(((Operator)var1).name);
      }
   }

   @Override
   public String toString() {
      return this.name;
   }
}
