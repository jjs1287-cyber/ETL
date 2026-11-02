package org.etlforge.util.settings;

public interface SettingsValueConverter<T> {
   SettingsValueConverter<Boolean> BOOLEAN = new SettingsValueConverter<Boolean>() {
      public Boolean fromString(String var1) throws IllegalArgumentException {
         return var1 != null && var1.length() != 0 ? Boolean.valueOf(var1) : null;
      }

      public String toString(Boolean var1) {
         return var1 != null ? var1.toString() : null;
      }
   };

   String toString(T var1);

   T fromString(String var1) throws IllegalArgumentException;
}
