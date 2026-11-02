package org.etlforge.integration.activity.serverapi.files;

import org.etlforge.integration.client.server.ServerCall;

public interface OperationFileProvider extends ServerCall {
   void listAvailableFiles(OperationFileRequest var1, ListAvailableFilesCallback var2);

   void downloadFile(DownloadFileRequest var1, DownloadFileCallback var2);
}
