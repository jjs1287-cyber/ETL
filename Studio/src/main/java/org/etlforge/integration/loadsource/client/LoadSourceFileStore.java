package org.etlforge.integration.loadsource.client;

import java.io.File;
import org.etlforge.io.Folder;
import org.etlforge.ui.util.file.FileUi;

public interface LoadSourceFileStore {
   Folder getRoot();

   boolean contains(File var1);

   void update(File var1);

   boolean removeFile(File var1);

   FileUi[] getFileList();

   FileUi getLatestFile();
}
