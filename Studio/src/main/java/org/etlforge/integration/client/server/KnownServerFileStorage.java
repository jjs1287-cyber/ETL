package org.etlforge.integration.client.server;

import java.io.File;
import java.util.Date;
import java.util.Properties;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.util.properties.PropertiesReader;
import org.etlforge.util.properties.PropertiesWriter;
import org.etlforge.util.version.Version;

public final class KnownServerFileStorage {
   private static final String FILE_NAME = "server.properties";
   private static final String NAME = "Name";
   private static final String GUID = "GUID";
   private static final String VERSION = "Version";
   private static final String FIRST_LOGIN = "FirstLogin";
   private static final String LATEST_LOGIN = "LatestLogin";
   private static final String LATEST_USER = "LatestUser";
   private static final String PASSWORD = "Password";
   private static final String USE_HTTPS = "UseHttps";
   private static final String OS = "OS";
   private static final String SYS_INFO = "SysInfo";
   private final ServerInfo serverInfo;
   private final File file;
   private final Properties properties;
   private final ServerPasswordCrypto pwdCrypto;

   public KnownServerFileStorage(File var1) {
      this.file = this.getFile(var1);
      this.properties = this.createPropertiesObject();
      this.serverInfo = this.createServerInfo();
      this.pwdCrypto = new ServerPasswordCrypto();
   }

   public KnownServerFileStorage(File var1, ServerInfo var2) {
      this.serverInfo = var2;
      this.file = this.getFile(var1);
      this.properties = this.createPropertiesObject();
      this.pwdCrypto = new ServerPasswordCrypto();
   }

   private File getFile(File var1) {
      return new File(var1, "server.properties");
   }

   private Properties createPropertiesObject() {
      return this.file.exists() ? readPropertiesFromFile(this.file) : new Properties();
   }

   private static Properties readPropertiesFromFile(File var0) {
      try {
         return PropertiesReader.read(var0);
      } catch (Exception var2) {
         ErrorLog.log(KnownServerFileStorage.class, "Failed to read local server information: " + var2.getMessage(), var2);
         return new Properties();
      }
   }

   private ServerInfo createServerInfo() {
      return new ServerInfo(this.getGuid(), this.getName(), this.getVersion(), this.getPlatform());
   }

   private ServerGuid getGuid() {
      String var1 = this.properties.getProperty("GUID");
      if (var1 == null) {
         var1 = this.file.getParentFile().getName();
      }

      return new ServerGuid(var1);
   }

   private String getName() {
      String var1 = this.properties.getProperty("Name");
      return var1 == null ? "" : var1;
   }

   private Version getVersion() {
      String var1 = this.properties.getProperty("Version");
      if (var1 != null && var1.length() > 0) {
         try {
            return VersionFactory.fromString(var1);
         } catch (Exception var3) {
            ErrorLog.log(this.getClass(), "Server info store contains invalid version: " + var1, var3);
         }
      }

      return null;
   }

   private ServerPlatform getPlatform() {
      String var1 = this.properties.getProperty("OS");
      String var2 = this.getSysInfo();
      return ServerPlatform.fromOSAndSystemInfo(var1, var2);
   }

   private String getSysInfo() {
      String var1 = this.properties.getProperty("SysInfo");
      return var1 == null ? "" : var1;
   }

   public KnownServer buildKnownServer() {
      return new KnownServer(
         this.serverInfo, this.getDate("FirstLogin"), this.getDate("LatestLogin"), this.getLatestUser(), this.getPassword(), this.isUsingHttps()
      );
   }

   private Date getDate(String var1) {
      String var2 = this.properties.getProperty(var1);
      return var2 == null ? null : new Date(Long.valueOf(var2));
   }

   private String getLatestUser() {
      String var1 = this.properties.getProperty("LatestUser");
      return var1 == null ? "" : var1;
   }

   private String getPassword() {
      String var1 = this.properties.getProperty("Password");
      return var1 != null ? this.pwdCrypto.decrypt(var1) : null;
   }

   private boolean isUsingHttps() {
      String var1 = this.properties.getProperty("UseHttps");
      return var1 != null ? Boolean.parseBoolean(var1) : ServerHttpsLookup.isUsingHttps(this.serverInfo.getName());
   }

   public void updateOnLogin(String var1, String var2, boolean var3) {
      this.setCurrentProperties(var1, var2, var3);
      this.storeProperties();
   }

   private void setCurrentProperties(String var1, String var2, boolean var3) {
      this.properties.put("Name", this.serverInfo.getName());
      this.properties.put("LatestUser", var1 != null ? var1 : "");
      if (var2 != null) {
         this.storePassword(var2);
      } else {
         this.properties.remove("Password");
      }

      this.properties.put("UseHttps", Boolean.toString(var3));
      this.properties.put("GUID", this.serverInfo.getGuid().toString());
      Version var4 = this.serverInfo.getVersion();
      if (var4 == null) {
         this.properties.put("Version", "");
      } else {
         this.properties.put("Version", var4.toString());
      }

      this.properties.put("OS", this.serverInfo.getPlatform().getOS());
      String var5 = String.valueOf(System.currentTimeMillis());
      this.properties.put("LatestLogin", var5);
      String var6 = this.properties.getProperty("FirstLogin");
      if (var6 == null || var6.length() == 0) {
         this.properties.put("FirstLogin", var5);
      }
   }

   private void storePassword(String var1) {
      assert var1 != null;
      String var2 = this.pwdCrypto.encrypt(var1);
      this.properties.setProperty("Password", var2);
   }

   private void storeProperties() {
      try {
         PropertiesWriter.write(this.properties, this.file);
      } catch (Exception var2) {
         ErrorLog.log(this.getClass(), "Failed to store local information about the ETLForge server: " + var2.getMessage(), var2);
      }
   }
}
