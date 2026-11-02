package org.etlforge.integration.design.netsuite.cf;

import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;

public class NetSuiteCustomFieldsRetrieverCustomObject {
   private final NetSuiteCustomFieldsLookupIFactory factory;
   private final String objectName;
   private final boolean isCustomObject = true;

   public NetSuiteCustomFieldsRetrieverCustomObject(NetSuiteCustomFieldsLookupIFactory var1, String var2) {
      this.factory = var1;
      this.objectName = var2;
   }

   public ConnectorNetSuiteCustomFields retrieve() {
      NetSuiteCustomFieldsLookup var1 = this.factory.getNetSuiteCustomFieldsLookup(true, this.objectName);
      return ConnectorNetSuiteCustomFieldsFactory.getNetSuiteCustomFields(var1);
   }
}
