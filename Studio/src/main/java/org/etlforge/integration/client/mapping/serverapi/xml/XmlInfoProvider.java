package org.etlforge.integration.client.mapping.serverapi.xml;

import org.etlforge.integration.client.server.ServerCall;
import org.etlforge.integration.filetransfer.client.ServerFile;
import org.etlforge.integration.filetransfer.serverapi.FileUploadApi;

public interface XmlInfoProvider extends ServerCall, FileUploadApi {
   void getXmlStructureFiles(GetXmlStructureFilesCallback var1);

   void getPossibleRootNames(ServerFile var1, GetRootNamesCallback var2);
}
