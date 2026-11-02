package org.etlforge.functors.predicate;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.io.Serializable;
import java.util.EnumSet;

public abstract class EnumPredicate<E extends Enum<E>> implements Predicate<E>, Serializable {
   public static <T extends Enum<T>> Predicate<T> is(T var0) {
      return new EnumPredicate.Is<>((T)var0);
   }

   public static <T extends Enum<T>> Predicate<T> isNot(T var0) {
      return Predicates.not(is((T)var0));
   }

   public static <T extends Enum<T>> Predicate<T> isIn(EnumSet<T> var0) {
      return new EnumPredicate.IsIn<>(var0);
   }

   public static <T extends Enum<T>> Predicate<T> isNotIn(EnumSet<T> var0) {
      return Predicates.not(isIn(var0));
   }

   private static final class Is<T extends Enum<T>> extends EnumPredicate<T> {
      private final T value;

      public Is(T var1) {
         this.value = (T)var1;
      }

      public boolean apply(T var1) {
         return var1 == this.value;
      }
   }

   private static final class IsIn<T extends Enum<T>> extends EnumPredicate<T> {
      private final EnumSet<T> values;

      public IsIn(EnumSet<T> var1) {
         this.values = var1;
      }

      public boolean apply(T var1) {
         return this.values.contains(var1);
      }
   }
}
