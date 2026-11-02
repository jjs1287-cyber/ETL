package org.etlforge.functors.predicate;

public class GreaterThanOrEqualToPredicate<T extends Comparable<T>> extends ComparisonPredicate<T> {
   public GreaterThanOrEqualToPredicate(T var1) {
      super((T)var1, 1, true);
   }

   public static <E extends Comparable<E>> GreaterThanOrEqualToPredicate<E> newInstance(E var0) {
      return new GreaterThanOrEqualToPredicate<>((E)var0);
   }
}
