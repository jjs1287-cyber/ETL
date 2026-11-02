package org.etlforge.integration.client.connector.netsuite.serverapi;

import java.util.Map;
import org.etlforge.integration.client.server.ServerCallback;

public interface GetNetSuiteSearchObjectSavedSearchCallBack extends ServerCallback {
   void gotNetSuiteSearchObjectSavedSearch(Map<String, String> var1);
}
