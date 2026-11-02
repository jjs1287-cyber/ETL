package org.etlforge.util.settings;

public interface Settings {
   boolean contains(String var1);

   void put(String var1, String var2);

   String get(String var1, String var2);

   <T> void put(String var1, T var2, SettingsValueConverter<T> var3);

   <T> T get(String var1, SettingsValueConverter<T> var2, T var3);
}
