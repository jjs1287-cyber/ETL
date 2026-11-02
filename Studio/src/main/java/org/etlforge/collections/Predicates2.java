package org.etlforge.collections;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import java.io.Serializable;

public final class Predicates2 {
   public static <E> Predicate<E> isNot(Object var0) {
      return new Predicates2.IsNot<>(var0);
   }

   private Predicates2() {
   }

   private static class IsNot<E> implements Predicate<E>, Serializable {
      private final Object object;

      public IsNot(Object var1) {
         this.object = var1;
      }

      @Override
      public boolean apply(E var1) {
         return var1 != this.object;
      }

      @Override
      public String toString() {
         return Objects.toStringHelper(this).addValue(this.object).toString();
      }
   }
}
