package org.etlforge.integration.client.info;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.etlforge.application.VersionFactory;
import org.etlforge.application.config.FatalConfigurationError;
import org.etlforge.system.annotations.InternalMethod;
import org.etlforge.util.properties.ReadOnlyProperties;
import org.etlforge.util.properties.ReadOnlyPropertiesFactory;
import org.etlforge.util.version.IllegalVersionException;
import org.etlforge.util.version.Version;

public final class BuildProperties {
   private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
   private static final String INVALID_VERSION_FORMAT = "{0}.{1}.{2}.{4}";
   private static BuildProperties INSTANCE;
   private static boolean initHasBeenCalled;
   private final ReadOnlyProperties delegate;
   private final Version version;
   private boolean hasParsedDate;
   private Date date;

   @InternalMethod
   public static synchronized void init() {
      if (!initHasBeenCalled) {
         try {
            INSTANCE = new BuildProperties();
         } catch (IOException var1) {
            FatalConfigurationError.throwError(8, BuildProperties.class, var1, "Failed to load the build properties: ");
         } catch (IllegalVersionException var2) {
            FatalConfigurationError.throwError(0, INSTANCE, var2, null);
         }

         initHasBeenCalled = true;
      }
   }

   private BuildProperties() throws IOException, IllegalVersionException {
      ReadOnlyPropertiesFactory var1 = ReadOnlyPropertiesFactory.newInstance();
      this.delegate = var1.load("org/etlforge/integration/client/info/build.properties");
      this.version = this.loadCurrentVersion();
   }

   static Version getCurrentVersion() {
      return INSTANCE != null ? INSTANCE.version : Version.UNKNOWN;
   }

   static Date getBuildTime() {
      return INSTANCE != null ? INSTANCE.getBuildTimeImpl() : null;
   }

   private synchronized Date getBuildTimeImpl() {
      if (!this.hasParsedDate) {
         this.hasParsedDate = true;
         String var1 = this.delegate.getString("date", null);
         if (var1 != null) {
            try {
               SimpleDateFormat var2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
               this.date = var2.parse(var1);
            } catch (ParseException var3) {
               var3.printStackTrace();
            }
         }
      }

      return this.date;
   }

   private Version loadCurrentVersion() throws IllegalVersionException {
      Version var1 = this.readOriginalVersion();
      return this.decorateVersionIfNeeded(var1);
   }

   private Version readOriginalVersion() throws IllegalVersionException {
      int var1 = this.delegate.getInt("major", -1);
      int var2 = this.delegate.getInt("minor", -1);
      int var3 = this.delegate.getInt("patch", -1);
      int var4 = this.delegate.getInt("build", -1);
      String var5 = this.delegate.getString("comment", null);

      try {
         return VersionFactory.newVersion(var1, var2, var3, var4, var5);
      } catch (IllegalArgumentException var8) {
         String var7 = MessageFormat.format("{0}.{1}.{2}.{4}", new int[]{var1, var2, var3, var4});
         if (var5 != null && var5.length() > 0) {
            var7 = var7 + "-" + var5;
         }

         throw new IllegalVersionException("build.properties contains invalid version information: " + var7);
      }
   }

   private Version decorateVersionIfNeeded(Version var1) {
      URL var2 = this.getClass().getClassLoader().getResource("VersionDecorator.properties");
      if (var2 == null) {
         return var1;
      }

      try {
         PropertiesVersionDecorator var3 = PropertiesVersionDecorator.fromResource(var2);
         return var3.apply(var1);
      } catch (IOException var4) {
         var4.printStackTrace();
         return var1;
      }
   }
}
