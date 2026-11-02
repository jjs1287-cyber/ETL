package org.etlforge.integration.loadsource.client;

import java.io.File;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.util.preference.PreferencesKey;

final class TestCenterFileStore implements RecentFileStore {
   private final RecentFileStore delegate;

   public TestCenterFileStore() {
      PreferencesKey var1 = new PreferencesKey(TestCenterFileStore.class, "LastSourceFile");
      this.delegate = new PreferencesFileStore(var1, 1);
   }

   @Override
   public FileUi latestFile() {
      return this.delegate.latestFile();
   }

   @Override
   public FileUi[] recentFiles() {
      return this.delegate.recentFiles();
   }

   @Override
   public void update(File var1) {
      this.delegate.update(var1);
   }

   @Override
   public boolean remove(File var1) {
      return this.delegate.remove(var1);
   }
}
