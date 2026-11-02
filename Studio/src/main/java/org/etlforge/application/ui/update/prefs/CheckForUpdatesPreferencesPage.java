package org.etlforge.application.ui.update.prefs;

import java.io.File;
import org.etlforge.application.ui.prefs.AbstractPreferencesPage;
import org.etlforge.application.ui.prefs.BooleanPreferenceUi;
import org.etlforge.application.ui.prefs.FilePreferenceUi;
import org.etlforge.ui.util.file.FileChooser;

public final class CheckForUpdatesPreferencesPage extends AbstractPreferencesPage {
   public static final String PATH = "Check for Updates";

   public CheckForUpdatesPreferencesPage() {
      super(new String[]{"Check for Updates"});
      this.addControl(new BooleanPreferenceUi(AutoCheckForUpdatesPreference.INSTANCE, "Automatically check for updates on startup"));
      this.addControl(new CheckForUpdatesPreferencesPage.DownloadLocationSelector());
   }

   private static class DownloadLocationSelector extends FilePreferenceUi {
      public DownloadLocationSelector() {
         super(UpdateDownloadLocationPreference.INSTANCE, "Download updates to");
         this.setSelectionMode(FileChooser.SelectionMode.DIRECTORIES_ONLY);
      }

      @Override
      protected boolean isValid(File var1) {
         return var1 != null && var1.isDirectory() && var1.exists();
      }
   }
}
