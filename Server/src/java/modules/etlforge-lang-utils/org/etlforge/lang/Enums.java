package org.etlforge.lang;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;
import java.io.Serializable;
import java.util.EnumSet;

public final class Enums {
   public static <E extends Enum> Ordering<E> alphabeticalOrdering() {
      return new Enums.AlphabeticalOrder<>();
   }

   public static <E extends Enum<E>> ImmutableList<E> alphabeticalOrder(Class<E> var0) {
      EnumSet var1 = EnumSet.allOf(var0);
      return alphabeticalOrdering().immutableSortedCopy(var1);
   }

   public static <E extends Enum<E>> Function<E, String> toNameFunction() {
      return new Enums.ToName<>();
   }

   private Enums() {
   }

   private static final class AlphabeticalOrder<E extends Enum> extends Ordering<E> implements Serializable {
      private AlphabeticalOrder() {
      }

      public int compare(E var1, E var2) {
         return var1.name().compareTo(var2.name());
      }

      @Override
      public String toString() {
         return "alphabetical-order";
      }
   }

   private static final class ToName<E extends Enum> implements Function<E, String>, Serializable {
      private ToName() {
      }

      public String apply(E var1) {
         return var1.name();
      }

      @Override
      public String toString() {
         return "to-name";
      }
   }
}
