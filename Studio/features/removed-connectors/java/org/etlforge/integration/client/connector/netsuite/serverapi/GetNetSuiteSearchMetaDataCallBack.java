package org.etlforge.integration.client.connector.netsuite.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.ui.connector.netsuite.search.metadata.NetSuiteSearchMetaData;

public interface GetNetSuiteSearchMetaDataCallBack extends ServerCallback {
   void gotNetSuiteSearchMetaData(NetSuiteSearchMetaData var1);
}
