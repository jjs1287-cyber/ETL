package com.etlforge.integration.license;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.common.server.license.OptionsParser;
import org.etlforge.util.time.DateUtils;
import org.etlforge.util.version.IllegalVersionException;

public final class LicenseInfo {
   public static final String EXPIRATION_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   private final ServerInfo serverInfo;
   private final LicenseType type;
   private final long remainingSeconds;
   private final String expirationDate;
   private final Map<String, String> options;
   private static final ServerGuid localhostGuid = new ServerGuid(UUID.randomUUID().toString());

   public LicenseInfo(ServerInfo var1, Map<String, String> var2) throws IntegrationServerException {
      Preconditions.checkNotNull(var1, "serverInfo");
      this.serverInfo = var1;
      this.type = this.extractLicenseType(var2);
      this.remainingSeconds = this.getRemainingSeconds(var2);
      this.expirationDate = this.getExpirationDate(var2);
      this.options = this.parseOptions(var2);
   }

   private LicenseType extractLicenseType(Map<String, String> var1) throws IntegrationServerException {
      String var2 = (String)var1.get("LicenseType");

      try {
         return LicenseType.valueOf(var2.toUpperCase());
      } catch (Exception var5) {
         String var4 = "The server did not return information about its license type. This probably means that you are attempting to log in to a server than runs an older version of ETLForge. "
            + this.getMinimumServerVersionMessage()
            + "\n\nIf the server is in fact running a compatible version, this error is most likely caused by a bug. If so, please contact ETLForge support.";
         throw new IntegrationServerException(var4);
      }
   }

   private String getMinimumServerVersionMessage() {
      try {
         return "The server needs to run version "
            + ApplicationConfiguration.getVersion("RequiredServerVersion")
            + " or later to be used with this version of the client. ";
      } catch (IllegalVersionException var2) {
         return "";
      }
   }

   private long getRemainingSeconds(Map<String, String> var1) throws IntegrationServerException {
      if (this.isPerpetual()) {
         return Long.MAX_VALUE;
      }

      try {
         String var2 = (String)var1.get("RemainingSeconds");
         return Long.parseLong(var2);
      } catch (Exception var3) {
         throwMissingOrInvalidValue("RemainingSeconds");
         return -1L;
      }
   }

   private String getExpirationDate(Map<String, String> var1) throws IntegrationServerException {
      if (this.type == LicenseType.PERPETUAL) {
         return null;
      }

      String var2 = (String)var1.get("ExpirationDate");
      if (StringUtils.isEmpty(var2) && this.isTrial()) {
         if (this.hasExpired()) {
            return null;
         }

         throwMissingOrInvalidValue("ExpirationDate");
      }

      try {
         preParseDateString(var2);
         SimpleDateFormat var3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         var3.setLenient(false);
         var3.parse(var2);
         return var2;
      } catch (Exception var4) {
         throwMissingOrInvalidValue("ExpirationDate");
         return null;
      }
   }

   private static void preParseDateString(String var0) throws Exception {
      if (var0.length() == "yyyy-MM-dd HH:mm:ss".length()) {
         String[] var1 = var0.split(" ");
         if (var1.length == 2 && validateDatePart(var1[0]) && validateTimePart(var1[1])) {
            return;
         }
      }

      throw new Exception("Invalid date format.");
   }

   private static boolean validateDatePart(String var0) {
      String[] var1 = var0.split("-");
      return var1.length == 3 && var1[0].length() == 4 && var1[1].length() == 2 && var1[2].length() == 2;
   }

   private static boolean validateTimePart(String var0) {
      String[] var1 = var0.split(":");
      return var1.length == 3 && var1[0].length() == 2 && var1[1].length() == 2 && var1[2].length() == 2;
   }

   private static void throwMissingOrInvalidValue(String var0) throws IntegrationServerException {
      throw new IntegrationServerException("Missing or invalid value for the key " + var0 + " returned from the server. Please contact ETLForge support.");
   }

   private Map<String, String> parseOptions(Map<String, String> var1) throws IntegrationServerException {
      String var2 = (String)var1.get("LicenseOptions");

      try {
         return new OptionsParser().parse(var2);
      } catch (Exception var4) {
         ErrorLog.log(this.getClass(), "Invalid license options string returned from the server: " + var2, var4);
         return Maps.newHashMap();
      }
   }

   public LicenseType getType() {
      return this.type;
   }

   public ServerInfo getServerInfo() {
      return this.serverInfo;
   }

   public SortedMap<String, String> getOptions() {
      TreeMap var1 = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
      var1.putAll(this.options);
      return var1;
   }

   public boolean isTrial() {
      return this.type == LicenseType.TRIAL;
   }

   public String getExpirationDate() {
      return this.expirationDate;
   }

   public int getRemainingDays() {
      if (this.hasExpired()) {
         return -1;
      }

      if (this.isPerpetual()) {
         return Integer.MAX_VALUE;
      }

      assert this.remainingSeconds >= 0L;
      Date var1 = new Date();
      Date var2 = new Date(var1.getTime() + 1000L * this.remainingSeconds);
      return DateUtils.getDaysBetween(var1, var2);
   }

   public boolean hasExpired() {
      return this.remainingSeconds < 0L;
   }

   public boolean isPerpetual() {
      return this.type == LicenseType.PERPETUAL;
   }

   LicenseInfo(Map<String, String> var1) throws IntegrationServerException {
      this(new ServerInfo(localhostGuid, "localhost"), var1);
   }
}
