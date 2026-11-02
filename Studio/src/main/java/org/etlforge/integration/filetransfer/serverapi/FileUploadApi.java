package org.etlforge.integration.filetransfer.serverapi;

import java.io.File;
import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;

public interface FileUploadApi extends ServerCall {
   void uploadFile(File var1, ServerFileFolder var2, String var3, UploadServerFileCallback var4);
}
