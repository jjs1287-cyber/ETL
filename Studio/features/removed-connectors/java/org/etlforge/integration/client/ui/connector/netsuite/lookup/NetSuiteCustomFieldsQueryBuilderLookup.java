package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchCustomFields;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchDesignException;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchFieldDictionary;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;

public interface NetSuiteCustomFieldsQueryBuilderLookup {
   NetSuiteSearchCustomFields lookupCustomFields(NetSuiteEndpoint var1, String var2, NetSuiteSearchFieldDictionary var3) throws NetSuiteSearchDesignException;
}
