package org.etlforge.integration.client.wsdl.serverapi;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;
import org.etlforge.integration.filetransfer.serverapi.UploadServerFileCallback;

public interface WsdlProvider extends ServerCall, FileUploadApi {
   void getWsdlFiles(GetWsdlFilesCallback var1);

   void uploadRemoteWsdl(String var1, UploadServerFileCallback var2);

   void deleteWsdl(String var1, boolean var2, DeleteWsdlCallback var3);
}
