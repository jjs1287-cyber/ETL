package org.etlforge.integration.client.info;

import java.util.Map;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.util.preference.KongaPreferences;
import org.etlforge.util.version.Version;

public final class ProductVersions {
   private static KongaPreferences storage = KongaPreferences.getPreferences(ProductVersions.class);
   private static final String STABLE_SERVER = "version-stable-server";
   private static final String STABLE_CLIENT = "version-stable-client";
   private static final String LATEST_SERVER = "version-latest-server";
   private static final String LATEST_CLIENT = "version-latest-client";

   public static synchronized Version getStableServerVersion() {
      return readVersionFromPrefs("version-stable-server");
   }

   public static synchronized Version getStableClient() {
      return readVersionFromPrefs("version-stable-client");
   }

   public static synchronized Version getLatestServer() {
      return readVersionFromPrefs("version-latest-server");
   }

   public static synchronized Version getLatestClient() {
      return readVersionFromPrefs("version-latest-client");
   }

   public static synchronized void update(Map<String, String> var0) {
      String[] var1 = new String[]{"version-stable-server", "version-stable-client", "version-latest-server", "version-latest-client"};

      for (String var5 : var1) {
         Version var6 = readVersion(var0, var5);
         if (var6 != null) {
            storage.putString(var5, var6.toString());
         }
      }
   }

   private static Version readVersionFromPrefs(String var0) {
      String var1 = storage.getString(var0, null);
      if (var1 != null) {
         try {
            return VersionFactory.fromString(var1);
         } catch (Exception var3) {
            ErrorLog.log(ProductVersions.class, "The Product Version store contains invalid data", var3);
         }
      }

      return Version.UNKNOWN;
   }

   private static Version readVersion(Map<String, String> var0, String var1) {
      String var2 = (String)var0.get(var1);
      if (var2 != null) {
         try {
            return VersionFactory.fromString(var2);
         } catch (Exception var4) {
            ErrorLog.log(ProductVersions.class, "Invalid version information returned by the ETLForge server", var4);
         }
      }

      return null;
   }

   private ProductVersions() {
   }
}
