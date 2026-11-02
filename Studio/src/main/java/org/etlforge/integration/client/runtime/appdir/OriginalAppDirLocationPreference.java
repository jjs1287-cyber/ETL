package org.etlforge.integration.client.runtime.appdir;

import java.io.File;
import org.etlforge.util.preference.KongaPreferences;

final class OriginalAppDirLocationPreference {
   private static final String PATH_PREFERENCE_KEY = "Path";

   static File get() {
      KongaPreferences var0 = KongaPreferences.getPreferences(ClientApplicationDirectory.class);
      return var0.getFile("Path", null);
   }

   private OriginalAppDirLocationPreference() {
   }
}
