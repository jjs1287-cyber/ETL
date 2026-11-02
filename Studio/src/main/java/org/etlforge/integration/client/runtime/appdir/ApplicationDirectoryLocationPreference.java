package org.etlforge.integration.client.runtime.appdir;

import java.io.File;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.FilePreference;
import org.etlforge.application.prefs.PreferenceFactory;

public final class ApplicationDirectoryLocationPreference {
   private static final FilePreference pref = createPref();

   public static File get() {
      File var0 = pref.get();
      if (var0 == null) {
         var0 = OriginalAppDirLocationPreference.get();
      }

      return var0;
   }

   public static void set(File var0) {
      pref.set(var0);
   }

   private static FilePreference createPref() {
      String[] var0 = new String[]{"General"};
      String var1 = "ApplicationDirectory";
      PreferenceFactory var2 = ApplicationConfiguration.getPreferenceFactory();
      return var2.newFilePreference(var0, var1, null);
   }

   private ApplicationDirectoryLocationPreference() {
   }
}
