package org.etlforge.util.properties;

public interface ReadOnlyProperties {
   String getString(String var1, String var2);

   String getString(Class<?> var1, String var2, String var3);

   int getInt(String var1, int var2);

   int getInt(Class<?> var1, String var2, int var3);

   boolean getBoolean(String var1, boolean var2);

   boolean getBoolean(Class<?> var1, String var2, boolean var3);
}
