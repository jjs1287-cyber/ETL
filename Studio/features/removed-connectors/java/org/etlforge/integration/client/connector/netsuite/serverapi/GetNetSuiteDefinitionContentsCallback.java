package org.etlforge.integration.client.connector.netsuite.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.server.netsuite.wsdl.NetSuiteDefinitionContents;

public interface GetNetSuiteDefinitionContentsCallback extends ServerCallback {
   void gotNetSuiteDefinitionContents(NetSuiteDefinitionContents var1);
}
