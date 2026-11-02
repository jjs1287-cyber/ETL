package org.etlforge.integration.client.main;

import java.io.IOException;
import java.util.Properties;
import org.etlforge.util.properties.PropertiesReader;

final class DebugSystemPropertiesLoader {
   public static void load() {
      try {
         Properties var0 = PropertiesReader.readResource("debug-system-properties.properties");
         transfer(var0);
      } catch (IOException var1) {
         var1.printStackTrace();
      }
   }

   private static void transfer(Properties var0) {
      for (Object var2 : var0.keySet()) {
         String var3 = (String)var2;
         if (System.getProperty(var3) == null) {
            System.setProperty(var3, var0.getProperty(var3));
         }
      }
   }

   private DebugSystemPropertiesLoader() {
   }
}
