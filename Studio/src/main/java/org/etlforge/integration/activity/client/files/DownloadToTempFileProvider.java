package org.etlforge.integration.activity.client.files;

import java.io.File;
import org.etlforge.application.appdir.ApplicationTempFolder;
import org.etlforge.integration.common.server.file.OperationFile;

public final class DownloadToTempFileProvider implements DownloadDestinationProvider {
   private final File root = ApplicationTempFolder.newFolder("sf_files", true);

   @Override
   public File getDestination(OperationFile var1) {
      File var2 = new File(this.root, var1.getGuid().toString());
      var2.mkdir();
      return new File(var2, var1.asLocalFile().getName());
   }
}
