package org.etlforge.functors.predicate;

public class GreaterThanPredicate<T extends Comparable<T>> extends ComparisonPredicate<T> {
   public GreaterThanPredicate(T var1) {
      super((T)var1, 1, false);
   }

   public static <E extends Comparable<E>> GreaterThanPredicate<E> newInstance(E var0) {
      return new GreaterThanPredicate<>((E)var0);
   }
}
