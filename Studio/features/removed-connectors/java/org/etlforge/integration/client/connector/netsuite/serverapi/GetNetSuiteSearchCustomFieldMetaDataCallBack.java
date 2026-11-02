package org.etlforge.integration.client.connector.netsuite.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.ui.connector.netsuite.search.metadata.NetSuiteSearchCustomFieldMetaData;

public interface GetNetSuiteSearchCustomFieldMetaDataCallBack extends ServerCallback {
   void gotNetSuiteSearchCustomFieldMetaData(NetSuiteSearchCustomFieldMetaData var1);
}
