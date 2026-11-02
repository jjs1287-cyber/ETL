package org.etlforge.application.ui.window.editor;

import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.application.ui.window.WindowElement;

public final class EditorIdGenerator {
   private static int sharedCounter = 0;
   private static final Map<Class<? extends WindowElement>, Integer> counters = Maps.newHashMap();

   private EditorIdGenerator() {
   }

   public static synchronized String get() {
      return String.valueOf(++sharedCounter);
   }

   public static synchronized String get(Class<? extends WindowElement> var0) {
      return var0.getName() + getNumber(var0);
   }

   private static int getNumber(Class<? extends WindowElement> var0) {
      Integer var1 = counters.get(var0);
      if (var1 == null) {
         var1 = 1;
      } else {
         var1 = var1 + 1;
      }

      counters.put(var0, var1);
      return var1;
   }
}
