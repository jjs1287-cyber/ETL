package org.etlforge.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.util.persist.Persistor;

public final class StringKeyValuePairsPersistor {
   private static final String ITEM = "Item";
   private static final String KEY = "key";
   private static final String VALUE = "value";

   public static void save(StringKeyValuePairs var0, Persistor var1) {
      save(var0, var1, Predicates.alwaysTrue());
   }

   public static void save(StringKeyValuePairs var0, Persistor var1, Predicate<String> var2) {
      for (String var4 : var0.getKeys()) {
         if (var2.apply(var4)) {
            Persistor var5 = var1.createChild("Item");
            var5.putString("key", var4);
            String var6 = var0.get(var4);
            if (var6 != null) {
               var5.putString("value", var6);
            }
         }
      }
   }

   public static StringKeyValuePairs restoreFrom(Persistor var0) {
      StringKeyValuePairs var1 = new StringKeyValuePairs();

      for (Persistor var3 : var0.getChildren("Item")) {
         var1.put(var3.getString("key"), var3.getString("value"));
      }

      return var1;
   }

   private StringKeyValuePairsPersistor() {
   }
}
