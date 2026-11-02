package org.etlforge.util.preference;

import org.etlforge.system.KeyValue;

public final class StringKeyValue implements KeyValue<String, String> {
   private final KongaPreferences prefs;

   public StringKeyValue(Class<?> var1) {
      this.prefs = KongaPreferences.getPreferences(var1);
   }

   public String get(String var1, String var2) {
      return this.prefs.getString(var1, var2);
   }

   public void set(String var1, String var2) {
      if (var2 != null) {
         this.prefs.putString(var1, var2);
      } else {
         this.prefs.removeKey(var1);
      }
   }
}
