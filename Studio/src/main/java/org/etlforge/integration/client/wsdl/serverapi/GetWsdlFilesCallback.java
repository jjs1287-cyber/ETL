package org.etlforge.integration.client.wsdl.serverapi;

import java.util.List;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

public interface GetWsdlFilesCallback extends ServerCallback {
   void gotWsdlFiles(ServerInfo var1, List<WsdlFile> var2);
}
