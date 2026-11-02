package org.etlforge.ui.util;

import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.lang.reflect.Method;

public final class AWTUtilities {
   private static final Class<?> impl = loadImpl();

   private static Class<?> loadImpl() {
      try {
         return Class.forName("com.sun.awt.AWTUtilities");
      } catch (Exception var1) {
         return null;
      }
   }

   public static void setWindowOpaque(Window var0, boolean var1) {
      if (impl != null) {
         try {
            Method var2 = impl.getMethod("setWindowOpaque", Window.class, boolean.class);
            if (var2 != null) {
               var2.invoke(null, var0, var1);
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }
   }

   public static boolean isTranslucencyCapable(GraphicsConfiguration var0) {
      if (impl == null) {
         return false;
      }

      try {
         Method var1 = impl.getMethod("isTranslucencyCapable", GraphicsConfiguration.class);
         if (var1 != null) {
            Object var2 = var1.invoke(null, var0);
            if (var2 instanceof Boolean) {
               return (Boolean)var2;
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return false;
   }

   private AWTUtilities() {
   }
}
