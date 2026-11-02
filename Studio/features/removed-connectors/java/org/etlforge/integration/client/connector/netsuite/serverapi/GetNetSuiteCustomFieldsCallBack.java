package org.etlforge.integration.client.connector.netsuite.serverapi;

import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;

public interface GetNetSuiteCustomFieldsCallBack extends ServerCallback {
   void gotNetSuiteCustomFields(NetSuiteObjectCustomFields var1);
}
