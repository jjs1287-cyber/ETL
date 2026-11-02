package org.etlforge.integration.loadsource.client;

import java.io.File;
import org.etlforge.application.filestore.FileStoreException;
import org.etlforge.ui.util.file.FileUi;
import org.etlforge.ui.util.file.RecentFileStore;

public final class RecentSourceDataFiles implements RecentFileStore {
   private final LoadSourceFileStore delegate;

   public RecentSourceDataFiles(LoadSourceFileStore var1) throws FileStoreException {
      this.delegate = var1;
   }

   @Override
   public FileUi latestFile() {
      return this.delegate.getLatestFile();
   }

   @Override
   public FileUi[] recentFiles() {
      return this.delegate.getFileList();
   }

   @Override
   public boolean remove(File var1) {
      return this.delegate.removeFile(var1);
   }

   @Override
   public void update(File var1) {
      this.delegate.update(var1);
   }
}
