package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDesignRuntimeException;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;

public interface NetSuiteDefinitionDesignLookup {
   NetSuiteDefinitionDesign lookupDefinition(NetSuiteEndpoint var1) throws ConnectorNetSuiteDesignRuntimeException;
}
