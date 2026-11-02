package org.etlforge.util.preference;

import org.etlforge.system.KeyValue;

public final class BooleanKeyValue implements KeyValue<String, Boolean> {
   private final KongaPreferences prefs;

   public BooleanKeyValue(Class<?> var1) {
      this.prefs = KongaPreferences.getPreferences(var1);
   }

   public Boolean get(String var1, Boolean var2) {
      return this.prefs.getBoolean(var1, var2);
   }

   public void set(String var1, Boolean var2) {
      if (var2 != null) {
         this.prefs.putBoolean(var1, var2);
      } else {
         this.prefs.removeKey(var1);
      }
   }
}
