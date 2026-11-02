package org.etlforge.integration.design.netsuite.cf;

import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;

public class NetSuiteCustomFieldsRetrieverObject {
   private final NetSuiteCustomFieldsLookupIFactory factory;
   private final NetSuiteCustomFieldsRequest customFieldObjects;

   public NetSuiteCustomFieldsRetrieverObject(NetSuiteCustomFieldsLookupIFactory var1, NetSuiteCustomFieldsRequest var2) {
      this.factory = var1;
      this.customFieldObjects = var2;
   }

   public ConnectorNetSuiteCustomFields retrieve() {
      NetSuiteCustomFieldsProcessor var1 = new NetSuiteCustomFieldsProcessor(this.factory, this.customFieldObjects);
      var1.process();
      NetSuiteCustomFieldsResult var2 = var1.getNetSuiteCustomFieldsResult();
      NetSuiteObjectCustomFields var3 = var2.getNetSuiteObjectsCustomFields();
      return ConnectorNetSuiteCustomFieldsFactory.getNetSuiteCustomFields(var3);
   }
}
