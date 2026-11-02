package org.etlforge.integration.client.ui.interchange.entity.webservice.wsdl;

import java.util.List;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.data.entity.webservicecall.WsdlFile;

interface WsdlFileDisplayer {
   void displayFiles(ServerInfo var1, List<WsdlFile> var2);
}
