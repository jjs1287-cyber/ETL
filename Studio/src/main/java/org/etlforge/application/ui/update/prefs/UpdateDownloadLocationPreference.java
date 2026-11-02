package org.etlforge.application.ui.update.prefs;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.application.config.ApplicationConfiguration;
import org.etlforge.application.prefs.FilePreference;
import org.etlforge.system.SystemUtils;

public class UpdateDownloadLocationPreference extends FilePreference {
   public static final UpdateDownloadLocationPreference INSTANCE = new UpdateDownloadLocationPreference(
      ApplicationConfiguration.getPreferenceFactory().getApplicationClass()
   );

   public UpdateDownloadLocationPreference(Class<?> var1) {
      super(var1, new String[]{"Check for Updates"}, "DownloadLocation", SystemUtils.USER_HOME);
   }

   @Override
   public void set(File var1) {
      Preconditions.checkNotNull(var1);
      Preconditions.checkArgument(var1.isDirectory(), var1 + " is not a directory");
      super.set(var1);
   }
}
