package org.etlforge.integration.client.connector.netsuite.serverapi;

import javax.annotation.ParametersAreNonnullByDefault;
import org.etlforge.integration.client.connector.serverapi.ConnectorCallInfoProvider;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;

@ParametersAreNonnullByDefault
public interface NetSuiteDesignInfoProvider extends ConnectorCallInfoProvider {
   void getSearchMetaData(GetNetSuiteSearchMetaDataCallBack var1);

   void getNetSuiteSearchCustomFieldMetaData(NetSuiteEndpoint var1, String var2, GetNetSuiteSearchCustomFieldMetaDataCallBack var3);

   void getNetSuiteSearchObjectSavedSearch(NetSuiteEndpoint var1, String var2, GetNetSuiteSearchObjectSavedSearchCallBack var3);

   void getNetSuiteDefinitionContents(NetSuiteEndpoint var1, GetNetSuiteDefinitionContentsCallback var2);

   void getNetSuiteCustomFieldsNew(NetSuiteEndpoint var1, boolean var2, String var3, GetNetSuiteCustomFieldsCallBack var4);
}
