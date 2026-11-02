package org.etlforge.util.properties;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

final class DefaultReadOnlyProperties implements ReadOnlyProperties {
   private final Properties properties;

   public DefaultReadOnlyProperties(String var1) throws IOException {
      this.properties = PropertiesReader.readResource(var1);
   }

   public DefaultReadOnlyProperties(File var1) throws IOException {
      this.properties = PropertiesReader.read(var1);
   }

   private DefaultReadOnlyProperties(Properties var1) {
      this.properties = var1;
   }

   public DefaultReadOnlyProperties merge(DefaultReadOnlyProperties var1) {
      Properties var2 = new Properties();
      var2.putAll(this.properties);
      var2.putAll(var1.properties);
      return new DefaultReadOnlyProperties(var2);
   }

   @Override
   public final String getString(String var1, String var2) {
      return this.properties.containsKey(var1) ? this.properties.getProperty(var1) : var2;
   }

   @Override
   public final String getString(Class<?> var1, String var2, String var3) {
      return this.getString(getClassKey(var1, var2), var3);
   }

   @Override
   public final int getInt(String var1, int var2) {
      return this.properties.containsKey(var1) ? Integer.parseInt(this.properties.getProperty(var1)) : var2;
   }

   @Override
   public final int getInt(Class<?> var1, String var2, int var3) {
      return this.getInt(getClassKey(var1, var2), var3);
   }

   @Override
   public final boolean getBoolean(String var1, boolean var2) {
      return this.properties.containsKey(var1) ? "true".equalsIgnoreCase(this.properties.getProperty(var1)) : var2;
   }

   @Override
   public final boolean getBoolean(Class<?> var1, String var2, boolean var3) {
      return this.getBoolean(getClassKey(var1, var2), var3);
   }

   private static String getClassKey(Class<?> var0, String var1) {
      return var0.getName() + "." + var1;
   }
}
