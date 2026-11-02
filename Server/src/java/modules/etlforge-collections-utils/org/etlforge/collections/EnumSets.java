package org.etlforge.collections;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.EnumSet;

public final class EnumSets {
   public static <E extends Enum<E>> EnumSet<E> copyOf(Collection<E> var0, Class<E> var1) {
      return var0.isEmpty() ? EnumSet.noneOf(var1) : EnumSet.copyOf(var0);
   }

   public static <E extends Enum<E>> ImmutableSet<E> filteredSet(Class<E> var0, Predicate<? super E> var1) {
      return Sets.immutableEnumSet(Iterables.filter(EnumSet.allOf(var0), var1));
   }

   private EnumSets() {
   }
}
