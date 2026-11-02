package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Supplier;
import org.etlforge.ui.UiThreadSupplier;

final class Utils {
   public static Object getValue(final PropertySearchField var0) {
      return UiThreadSupplier.call(new Supplier<Object>() {
         @Override
         public Object get() {
            return var0.getValue();
         }
      });
   }

   public static Object getValue(final ReplaceField var0) {
      return UiThreadSupplier.call(new Supplier<Object>() {
         @Override
         public Object get() {
            return var0.getValue();
         }
      });
   }

   private Utils() {
   }
}
