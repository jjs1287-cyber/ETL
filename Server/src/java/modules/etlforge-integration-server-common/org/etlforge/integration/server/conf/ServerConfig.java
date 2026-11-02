package org.etlforge.integration.server.conf;

import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.integration.common.server.ServerEdition;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.io.Folder;
import org.etlforge.lang.DoubleParser;
import org.etlforge.lang.IntParser;
import org.etlforge.lang.LongParser;
import org.etlforge.util.settings.IniFileSettingsHolder;
import org.etlforge.util.settings.IniFileSettingsParser;

public final class ServerConfig {
   private static final String ETLFORGE_CONF = "etlforge.conf";
   private static final String ETLFORGE_HOME_ENV = "ETLFORGE_HOME";
   private static final String ETLFORGE_DL_HOME_ENV = "ETLFORGE_DL_HOME";
   private static final String TRANSFORMATION_ENGINE_SECTION = "TransformationEngine";
   private static final ServerConfig conf = new ServerConfig();
   private final Object listenerLock = new Object();
   private final List<ServerConfigListener> listeners = new CopyOnWriteArrayList<>();
   private IniFileSettingsHolder settingsHolder;

   private static String getServerHomeEnv() {
      ServerEdition var0 = ServerEdition.lookupFromEnvironment();
      switch (var0) {
         case DATA_LOADER:
            return "ETLFORGE_DL_HOME";
         default:
            return "ETLFORGE_HOME";
      }
   }

   public static String getServerHome() throws ServerConfigurationException {
      String var0 = getServerHomeImpl();
      if (var0 != null && var0.length() != 0) {
         File var1 = new File(var0);
         if (!var1.exists()) {
            throw new ServerConfigurationException("The etlforge home directory does not exist. " + getServerHomeEnv() + "=\"" + var0 + "\".");
         } else if (!var1.isDirectory()) {
            throw new ServerConfigurationException(getServerHomeEnv() + " is not set to a directory. " + getServerHomeEnv() + "=\"" + var0 + "\".");
         } else {
            return var1.getAbsolutePath();
         }
      } else {
         throw new ServerConfigurationException("The environment variable " + getServerHomeEnv() + " has not been set.");
      }
   }

   public static File getServerHomeDir() throws ServerConfigurationException {
      return new File(getServerHome());
   }

   public static File getDataInterchangeDir() throws ServerConfigurationException {
      return new File(getServerHome(), "DataInterchange");
   }

   public static File getSuccessFileDir() throws ServerConfigurationException {
      File var0 = getDataInterchangeDir();
      var0 = new File(var0, "UserFiles");
      return new File(var0, "ResultFiles");
   }

   public static File getDataInterchangeTempDir() throws ServerConfigurationException {
      return new File(getDataInterchangeDir(), "Temp");
   }

   public static File getDebugLogDir() throws ServerConfigurationException {
      String var0 = getOption("TransformationEngine", "WriteLogDir", null);
      return var0 != null ? new File(var0) : new File(getDataInterchangeTempDir(), "Debug");
   }

   public static File getOperationLogDir() throws ServerConfigurationException {
      return new File(getDataInterchangeTempDir(), "LOG");
   }

   public static boolean isServerHomeDefined() {
      String var0 = getServerHomeImpl();
      if (var0 == null) {
         return false;
      }

      File var1 = new File(var0);
      return var1.exists() && var1.isDirectory();
   }

   public static File getServerConfigFile() throws ServerConfigurationException {
      String var0 = System.getProperty("etlforge.conf");
      return var0 != null ? new File(var0) : new File(getServerHome(), "etlforge.conf");
   }

   public static void replace(String var0) throws ServerConfigurationException, IOException {
      String var1 = LineBreakFixer.ensureCorrectLineBreaks(var0);
      FileUtils.writeStringToFile(getServerConfigFile(), var1, null);
      loadOptions();
   }

   public static void loadOptions() throws ServerConfigurationException {
      conf.loadOptionsImplSynchronized();
      synchronized (conf.listenerLock) {
         for (ServerConfigListener var2 : conf.listeners) {
            try {
               var2.configReloaded();
            } catch (Exception var5) {
               var5.printStackTrace();
            }
         }
      }
   }

   private synchronized void loadOptionsImplSynchronized() throws ServerConfigurationException {
      this.settingsHolder = null;

      try {
         this.loadOptionsImpl();
      } catch (IOException var2) {
         throw new ServerConfigurationException("Failed to load the server options from file. " + var2.getMessage(), var2);
      }
   }

   private void loadOptionsImpl() throws IOException, ServerConfigurationException {
      IniFileSettingsParser var1 = new IniFileSettingsParser();
      File var2 = getServerConfigFile();
      this.settingsHolder = var1.parseSettingsFile(var2);
   }

   public static String getOption(String var0, String var1, String var2) {
      String var3 = getOption(var0, var1);
      return var3 == null ? var2 : var3;
   }

   private static String getOption(String var0, String var1) {
      return conf.getOptionImplSynchronized(var0, var1);
   }

   private synchronized String getOptionImplSynchronized(String var1, String var2) {
      IniFileSettingsHolder var3 = this.getSettings();
      return var3 != null ? var3.getValue(var1, var2) : null;
   }

   private IniFileSettingsHolder getSettings() {
      if (this.settingsHolder == null) {
         try {
            this.loadOptionsImpl();
         } catch (IOException var2) {
            var2.printStackTrace();
         } catch (ServerConfigurationException var3) {
            var3.printStackTrace();
         }
      }

      return this.settingsHolder;
   }

   public static int getOption(String var0, String var1, int var2) {
      String var3 = getOption(var0, var1);
      return IntParser.parse(var3, var2);
   }

   public static long getOption(String var0, String var1, long var2) {
      String var4 = getOption(var0, var1);
      return LongParser.parse(var4, var2);
   }

   public static double getOption(String var0, String var1, double var2) {
      String var4 = getOption(var0, var1);
      return DoubleParser.parse(var4, var2);
   }

   public static boolean getOption(String var0, String var1, boolean var2) {
      String var3 = getOption(var0, var1);
      return var3 == null ? var2 : Boolean.parseBoolean(var3) || var3.equals("1") || var3.equalsIgnoreCase("yes");
   }

   public static ImmutableMap<String, String> getSection(String var0) {
      return conf.getSectionImpl(var0);
   }

   private synchronized ImmutableMap<String, String> getSectionImpl(String var1) {
      IniFileSettingsHolder var2 = this.getSettings();
      return var2 == null ? ImmutableMap.of() : var2.getSection(var1);
   }

   public static void addConfigurationListener(ServerConfigListener var0) {
      synchronized (conf.listenerLock) {
         conf.listeners.add(var0);
      }
   }

   public static void removeConfigurationListener(ServerConfigListener var0) {
      synchronized (conf.listenerLock) {
         conf.listeners.remove(var0);
      }
   }

   private static String getServerHomeImpl() {
      String var0 = getServerHomeEnv();
      String var1 = System.getProperty(var0);
      if (var1 == null) {
         var1 = System.getenv(var0);
      }

      return var1;
   }

   public static Folder getTempFolder() {
      String var0 = conf.getOptionImplSynchronized("Settings", "TempDir");
      if (var0 != null) {
         return new Folder(var0);
      }

      ServerEdition var1 = ServerEdition.lookupFromEnvironment();
      String var2 = var1 == ServerEdition.DATA_LOADER ? "etlforge_dl" : "etlforge";
      File var3 = SystemUtils.getJavaIoTmpDir();
      return new Folder(var3, var2);
   }
}
