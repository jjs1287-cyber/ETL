package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteDefinitionQueryBuilder;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchDesignException;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;

public interface NetSuiteDefinitionQueryBuilderLookup {
   NetSuiteDefinitionQueryBuilder lookupDefinition(NetSuiteEndpoint var1) throws NetSuiteSearchDesignException;
}
