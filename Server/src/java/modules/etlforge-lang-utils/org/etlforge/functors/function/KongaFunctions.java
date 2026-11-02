package org.etlforge.functors.function;

import com.google.common.base.Function;
import java.io.Serializable;

public final class KongaFunctions {
   public static <T> Function<Object, T> cast(Class<T> var0) {
      return new KongaFunctions.Cast<>(var0);
   }

   public static <T> Function<Object, T> strictCast(Class<T> var0) {
      return new KongaFunctions.StrictCast<>(var0);
   }

   public static <E extends Enum<E>> Function<E, String> enumName() {
      return new KongaFunctions.EnumNameFunction<>();
   }

   public static Function<Object, Boolean> notNull() {
      return KongaFunctions.NotNullFunction.INSTANCE;
   }

   public static Function<Boolean, Boolean> reverseBoolean() {
      return KongaFunctions.ReverseBooleanFunction.INSTANCE;
   }

   private KongaFunctions() {
   }

   private static class Cast<T> implements Function<Object, T>, Serializable {
      private final Class<T> clazz;

      public Cast(Class<T> var1) {
         this.clazz = var1;
      }

      @Override
      public T apply(Object var1) {
         return this.clazz.isInstance(var1) ? this.clazz.cast(var1) : null;
      }
   }

   private static class EnumNameFunction<E extends Enum<E>> implements Function<E, String>, Serializable {
      private EnumNameFunction() {
      }

      public String apply(E var1) {
         return var1.name();
      }
   }

   private enum NotNullFunction implements Function<Object, Boolean> {
      INSTANCE;

      public Boolean apply(Object var1) {
         return var1 != null;
      }
   }

   private enum ReverseBooleanFunction implements Function<Boolean, Boolean> {
      INSTANCE;

      public Boolean apply(Boolean var1) {
         return !var1;
      }
   }

   private static class StrictCast<T> implements Function<Object, T>, Serializable {
      private final Class<T> clazz;

      public StrictCast(Class<T> var1) {
         this.clazz = var1;
      }

      @Override
      public T apply(Object var1) {
         return this.clazz.cast(var1);
      }
   }
}

