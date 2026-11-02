package org.etlforge.system;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;

public final class SystemUtils {
   public static final String LINE_SEPARATOR = System.getProperty("line.separator");
   public static final boolean IS_OS_WINDOWS_VISTA = System.getProperty("os.name").toLowerCase().contains("windows vista");
   public static final boolean IS_JAVA_1_6 = SystemUtils.JavaVersion.INSTANCE.is_1_6();
   public static final File USER_HOME = new File(System.getProperty("user.home", ""));

   public static String getSystemProperties(String var0) {
      Properties var1 = System.getProperties();
      ArrayList var2 = new ArrayList<>(var1.keySet());
      Collections.sort(var2, new Comparator<Object>() {
         @Override
         public int compare(Object var1, Object var2x) {
            return var1.toString().compareToIgnoreCase(var2x.toString());
         }
      });
      StringBuilder var3 = new StringBuilder();

      for (Object var5 : var2) {
         var3.append(var5).append(" = ").append(var1.get(var5)).append(var0);
      }

      return var3.toString();
   }

   private SystemUtils() {
   }

   private static final class JavaVersion {
      private static final SystemUtils.JavaVersion INSTANCE = new SystemUtils.JavaVersion();
      private String version = System.getProperty("java.version");

      private JavaVersion() {
         this.version = System.getProperty("java.version");
         if (this.version == null) {
            this.version = "";
         }
      }

      public boolean is_1_6() {
         return this.version.startsWith("1.6");
      }
   }
}
