package org.etlforge.integration.filetransfer.client.download;

import org.etlforge.application.filestore.FileStore;

public interface FileDownloadClient {
   FileStore getFileStore();

   void downloadStarted();

   void downloadCancelled();

   void handleError(String var1, Throwable var2);

   void downloadComplete(DownloadedFiles var1);
}
