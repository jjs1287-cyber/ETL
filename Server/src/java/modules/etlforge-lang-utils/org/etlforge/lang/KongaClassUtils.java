package org.etlforge.lang;

import java.lang.reflect.Constructor;

public final class KongaClassUtils {
   public static <T> Constructor<T> getSingleArgumentConstructor(Class<T> var0, Class<?> var1) {
      for (Constructor var5 : var0.getConstructors()) {
         Class[] var6 = var5.getParameterTypes();
         if (var6.length == 1 && var1.isAssignableFrom(var6[0])) {
            return var5;
         }
      }

      return null;
   }

   public static <T> Constructor<T> getNoArgumentConstuctor(Class<T> var0) {
      for (Constructor var4 : var0.getConstructors()) {
         if (var4.getParameterTypes().length == 0) {
            return var4;
         }
      }

      return null;
   }

   private KongaClassUtils() {
   }
}
