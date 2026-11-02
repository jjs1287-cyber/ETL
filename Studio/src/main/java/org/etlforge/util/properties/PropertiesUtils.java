package org.etlforge.util.properties;

import java.util.Properties;

public final class PropertiesUtils {
   public static Properties create(String var0, String var1) {
      Properties var2 = new Properties();
      var2.setProperty(var0, var1);
      return var2;
   }

   public static Properties create(String var0, String var1, String var2, String var3) {
      Properties var4 = new Properties();
      var4.setProperty(var0, var1);
      var4.setProperty(var2, var3);
      return var4;
   }

   public static Properties create(String var0, String var1, String var2, String var3, String var4, String var5) {
      Properties var6 = new Properties();
      var6.setProperty(var0, var1);
      var6.setProperty(var2, var3);
      var6.setProperty(var4, var5);
      return var6;
   }

   public static Properties copy(Properties var0) {
      Properties var1 = new Properties();
      var1.putAll(var0);
      return var1;
   }

   private PropertiesUtils() {
   }
}
