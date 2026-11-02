package org.etlforge.integration.client.ui.connector.netsuite.lookup;

import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;

public interface NetSuiteCustomFieldsDesignLookup extends NetSuiteCustomFieldsLookup, NetSuiteCustomFieldsLookupIFactory {
   void setDesignInput(NetSuiteEndpoint var1, NetSuiteFunction.FunctionType var2);

   void evict(boolean var1, String var2);
}
