package org.etlforge.integration.activity.serverapi.files;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.common.server.file.OperationFile;

public final class DownloadFileRequest {
   public final OperationFile serverFile;
   public final File localFile;

   public DownloadFileRequest(OperationFile var1, File var2) {
      this.serverFile = Preconditions.checkNotNull(var1, "serverFile");
      this.localFile = Preconditions.checkNotNull(var2, "localFile");
      Preconditions.checkArgument(!var2.isDirectory(), var2.getAbsolutePath() + " is a directory");
   }
}
