package org.etlforge.integration.filetransfer.client.upload;

import java.io.File;
import org.etlforge.application.filestore.FileStore;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;

public interface FileUploadClient {
   FileStore getFileStore();

   void fileWasUploaded(String var1, ServerFile var2, ServerFileUploadResult var3);

   boolean isFileClearedForUpload(File var1);

   void uploadCancelled();

   void handleError(Throwable var1);

   void uploadComplete(ServerInfo var1, ServerFile var2, ServerFile[] var3);

   boolean isAborted();
}
