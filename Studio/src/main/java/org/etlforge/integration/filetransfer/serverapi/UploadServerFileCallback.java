package org.etlforge.integration.filetransfer.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.filetransfer.client.ServerFileUploadResult;

public interface UploadServerFileCallback extends ServerCallback {
   void fileWasUploaded(ServerFileUploadResult var1);
}
