package org.etlforge.ui.util.file;

import java.io.File;

public interface RecentFileStore {
   RecentFileStore EMPTY = new RecentFileStore() {
      @Override
      public void update(File var1) {
      }

      @Override
      public boolean remove(File var1) {
         return false;
      }

      @Override
      public FileUi[] recentFiles() {
         return new FileUi[0];
      }

      @Override
      public FileUi latestFile() {
         return null;
      }
   };

   FileUi[] recentFiles();

   FileUi latestFile();

   void update(File var1);

   boolean remove(File var1);
}
