package org.etlforge.application.config;

import java.io.IOException;
import java.util.logging.Level;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.appdir.AppDirLocationProvider;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.prefs.PreferenceFactory;
import org.etlforge.util.properties.ReadOnlyProperties;
import org.etlforge.util.properties.ReadOnlyPropertiesFactory;
import org.etlforge.util.properties.SystemPropertiesDecorator;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

public final class ApplicationConfiguration {
   private static final String MAIN_RESOURCE = "Application.Config.properties";
   private static final String EDITION_SPECIFIC_RESOURCE = "Application.Config.Edition.properties";
   public static final String ABOUT_UI_CLASS = "About.Ui.Class";
   private static final ReadOnlyProperties props = load();
   private static PreferenceFactory prefFactory;

   public static String getProductName() {
      return props.getString("Product.Name", "[Product.Name]");
   }

   public static String getShortProductName() {
      String var0 = "Product.Name.Short";
      String var1 = props.getString(var0, null);
      if (var1 == null) {
         var1 = getProductName();
      }

      if (var1 == null) {
         var1 = "[" + var0 + "]";
      }

      return var1;
   }

   public static String getCompanyName() {
      return props.getString("Company.Name", "[Company.Name]");
   }

   public static String getSupportUrl() {
      return props.getString("Support.Url", null);
   }

   public static String getHelpSetName() {
      return props.getString("HelpSet.Name", null);
   }

   public static String getAboutApplicationUiClass() {
      return props.getString("About.Ui.Class", null);
   }

   public static String getMainWindowTitle() {
      return props.getString("MainWindow.Title", null);
   }

   public static synchronized PreferenceFactory getPreferenceFactory() {
      if (prefFactory == null) {
         prefFactory = createPreferenceFactory();
      }

      return prefFactory;
   }

   public static Class<?> getClass(String var0) throws ApplicationConfigurationException {
      String var1 = props.getString(var0, null);
      if (var1 != null) {
         try {
            return Class.forName(var1);
         } catch (ClassNotFoundException var3) {
            throw new ApplicationConfigurationException("Failed to load the class " + var1 + " identified by the configuration key \"" + var0 + "\".");
         }
      } else {
         return null;
      }
   }

   public static <T> Class<? extends T> getClass(String var0, Class<T> var1) throws ApplicationConfigurationException, ClassCastException {
      Class var2 = getClass(var0);
      return var2 != null ? var2.asSubclass(var1) : null;
   }

   public static AppDirLocationProvider getAppDirLocationProvider() throws ApplicationConfigurationException {
      try {
         Class var0 = getClass("ApplicationDirectory.LocationProvider", AppDirLocationProvider.class);
         return (AppDirLocationProvider)var0.newInstance();
      } catch (Exception var1) {
         throw new ApplicationConfigurationException("Invalid application configuration: missing or invalid ApplicationDirectoryLocationProvider.");
      }
   }

   public static String getString(String var0, String var1) {
      return props.getString(var0, var1);
   }

   public static boolean getBoolean(String var0, boolean var1) {
      return props.getBoolean(var0, var1);
   }

   public static int getInt(String var0, int var1) {
      return props.getInt(var0, var1);
   }

   public static Version getVersion(String var0) throws IllegalVersionException {
      String var1 = props.getString(var0, null);
      if (var1 == null) {
         throw new IllegalVersionException("No version string exists for the key \"" + var0 + "\"");
      } else {
         return VersionFactory.fromString(var1);
      }
   }

   public static Version getVersion(String var0, Version var1) {
      String var2 = props.getString(var0, null);
      if (var2 != null) {
         try {
            return VersionFactory.fromString(var2);
         } catch (IllegalVersionException var4) {
            ErrorLog.log(ApplicationConfiguration.class, "Invalid version string encountered for the key \"" + var0 + "\"", var4);
         }
      }

      return var1;
   }

   public static <E extends Enum<E>> E getEnum(String var0, Class<E> var1, E var2) {
      String var3 = props.getString(var0, null);
      if (var3 != null) {
         for (Enum var7 : (Enum[])var1.getEnumConstants()) {
            if (var7.name().equals(var3)) {
               return (E)var7;
            }
         }
      }

      return (E)var2;
   }

   public static Level getLogLevel(String var0, Level var1) {
      try {
         String var2 = props.getString(var0, null);
         if (var2 != null) {
            return Level.parse(var2);
         }
      } catch (IllegalArgumentException var3) {
         ErrorLog.log(ApplicationConfiguration.class, "Invalid log Level string encountered for the key \"" + var0 + "\".", var3);
      }

      return var1;
   }

   private static PreferenceFactory createPreferenceFactory() {
      String var0 = props.getString("Preference.Factory", null);
      if (var0 != null) {
         try {
            Class var1 = Class.forName(var0);
            return new PreferenceFactory(var1);
         } catch (ClassNotFoundException var2) {
            ErrorLog.log(ApplicationConfiguration.class, "Invalid configuration: unknown preference factory class", var2);
         }
      }

      return new PreferenceFactory(PreferenceFactory.class);
   }

   private static ReadOnlyProperties load() {
      try {
         ReadOnlyPropertiesFactory var0 = ReadOnlyPropertiesFactory.newInstance();
         return new SystemPropertiesDecorator(var0.load("Application.Config.properties", "Application.Config.Edition.properties"));
      } catch (IOException var1) {
         FatalConfigurationError.throwError(9, ApplicationConfiguration.class, var1, "Failed to load the file \"Application.Config.properties\"");
         return null;
      }
   }

   private ApplicationConfiguration() {
   }
}
