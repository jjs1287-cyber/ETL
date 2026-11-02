package org.etlforge.integration.common.server;

import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.lang.StringUtils;

public enum ServerEdition {
   TRIAL,
   PROFESSIONAL,
   PROFESSIONAL_TRIAL,
   ENTERPRISE,
   ENTERPRISE_TRIAL,
   DATA_LOADER,
   UNKNOWN;

   public static ServerEdition lookupFromEnvironment() {
      ServerEdition var0 = lookInSystemProperties();
      if (var0 != null) {
         return var0;
      }

      try {
         InitialContext var1 = new InitialContext();
         Context var2 = (Context)var1.lookup("java:comp/env");
         String var3 = (String)var2.lookup("etlforge-edition");
         if ("DL".equalsIgnoreCase(var3)) {
            return DATA_LOADER;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return UNKNOWN;
   }

   private static ServerEdition lookInSystemProperties() {
      String var0 = System.getProperty("etlforge-edition");
      if (StringUtils.isNotEmpty(var0)) {
         try {
            return valueOf(var0);
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

      return null;
   }
}
