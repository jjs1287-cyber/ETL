package org.etlforge.functors.predicate;

public class LessThanOrEqualToPredicate<T extends Comparable<T>> extends ComparisonPredicate<T> {
   public LessThanOrEqualToPredicate(T var1) {
      super((T)var1, -1, true);
   }

   public static <E extends Comparable<E>> LessThanOrEqualToPredicate<E> newInstance(E var0) {
      return new LessThanOrEqualToPredicate<>((E)var0);
   }
}
