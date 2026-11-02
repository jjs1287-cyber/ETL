package org.etlforge.integration.activity.client.files;

import com.google.common.base.Preconditions;
import java.io.File;
import org.etlforge.integration.activity.serverapi.files.DownloadFileCallback;
import org.etlforge.integration.activity.serverapi.files.DownloadFileRequest;
import org.etlforge.integration.activity.serverapi.files.OperationFileProvider;
import org.etlforge.integration.common.server.file.OperationFile;

public final class RemoteFileSupport {
   private final OperationFileProvider serverApi;
   private final DownloadDestinationProvider destinationProvider;

   public RemoteFileSupport(OperationFileProvider var1, DownloadDestinationProvider var2) {
      this.serverApi = Preconditions.checkNotNull(var1);
      this.destinationProvider = Preconditions.checkNotNull(var2);
   }

   public File getDestination(OperationFile var1) {
      return this.destinationProvider.getDestination(var1);
   }

   public void downloadRemoteFile(DownloadFileRequest var1, DownloadFileCallback var2) {
      this.serverApi.downloadFile(var1, var2);
   }
}
