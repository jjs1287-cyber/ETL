package org.etlforge.system;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public final class EnumWithIdLookup {
   public static <T, E extends Enum<E> & EnumWithId<T>> E lookup(Class<E> var0, T var1) {
      Optional var2 = tryLookup(var0, var1);
      if (var2.isPresent()) {
         return (E)var2.get();
      } else {
         throw new IllegalArgumentException("Unknown ID: " + var1);
      }
   }

   public static <T, E extends Enum<E> & EnumWithId<T>> Optional<E> tryLookup(Class<E> var0, T var1) {
      Preconditions.checkNotNull(var1);

      for (E var5 : var0.getEnumConstants()) {
         if (((EnumWithId)var5).getId().equals(var1)) {
            return Optional.of(var5);
         }
      }

      return Optional.absent();
   }

   public static <T, E extends Enum<E> & EnumWithId<T>> E lookupOr(T var0, E var1) {
      return tryLookup(var1.getDeclaringClass(), var0).or(var1);
   }

   private EnumWithIdLookup() {
   }
}
