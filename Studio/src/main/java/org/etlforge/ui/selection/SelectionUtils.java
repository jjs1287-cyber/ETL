package org.etlforge.ui.selection;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;

public final class SelectionUtils {
   public static <T> T getSingleSelection(Selection var0, Class<T> var1) {
      Object var2 = null;

      for (Object var4 : var0.items()) {
         if (var2 != null || !var1.isInstance(var4)) {
            return null;
         }

         var2 = var1.cast(var4);
      }

      return (T)var2;
   }

   public static <T> Collection<T> getSelectedItemsOfType(Selection var0, Class<T> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (Object var4 : var0.items()) {
         if (var1.isInstance(var4)) {
            var2.add(var1.cast(var4));
         }
      }

      return var2;
   }

   public static <T> boolean contains(Selection var0, Class<T> var1) {
      for (Object var3 : var0.items()) {
         if (var1.isInstance(var3)) {
            return true;
         }
      }

      return false;
   }

   public static <T> T head(OrderedSelection var0, Class<T> var1) {
      Object var2 = var0.head();
      return (T)(var1.isInstance(var2) ? var1.cast(var2) : null);
   }

   private SelectionUtils() {
   }
}
