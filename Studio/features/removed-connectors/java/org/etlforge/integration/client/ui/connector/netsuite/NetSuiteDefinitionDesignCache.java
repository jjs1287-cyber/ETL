package org.etlforge.integration.client.ui.connector.netsuite;

import com.google.common.base.Optional;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;

public interface NetSuiteDefinitionDesignCache {
   Optional<NetSuiteDefinitionDesign> getNetSuiteDefinitionDesign(NetSuiteEndpoint var1);

   void storeNetSuiteDefinitionDesign(NetSuiteEndpoint var1, NetSuiteDefinitionDesign var2);
}
