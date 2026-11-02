package org.etlforge.integration.client.mapping.serverapi.xml;

import java.util.Collection;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.filetransfer.client.ServerFileFolder;

public interface GetXmlStructureFilesCallback extends ServerCallback {
   void structureFilesRetrieved(ServerInfo var1, Collection<ServerFileFolder> var2);
}
