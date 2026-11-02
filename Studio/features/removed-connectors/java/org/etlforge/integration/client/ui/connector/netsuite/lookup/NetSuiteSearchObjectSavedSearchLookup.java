package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import java.util.Map;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchDesignException;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;

public interface NetSuiteSearchObjectSavedSearchLookup {
   Map<String, String> savedSearchLookup(NetSuiteEndpoint var1, String var2) throws NetSuiteSearchDesignException;
}
