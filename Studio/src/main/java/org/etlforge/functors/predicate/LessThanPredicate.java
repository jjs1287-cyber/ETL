package org.etlforge.functors.predicate;

public class LessThanPredicate<T extends Comparable<T>> extends ComparisonPredicate<T> {
   public LessThanPredicate(T var1) {
      super((T)var1, -1, false);
   }

   public static <E extends Comparable<E>> LessThanPredicate<E> newInstance(E var0) {
      return new LessThanPredicate<>((E)var0);
   }
}
