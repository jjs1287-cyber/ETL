package org.etlforge.integration.design.netsuite.cf;

import com.google.common.base.Strings;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;

public class NetSuiteCustomFieldsRetriever {
   private final NetSuiteCustomFieldsLookupIFactory factory;
   private final String customObjectName;
   private final NetSuiteCustomFieldsRequest standardObjects;

   public NetSuiteCustomFieldsRetriever(NetSuiteCustomFieldsLookupIFactory var1, String var2, NetSuiteCustomFieldsRequest var3) {
      this.factory = var1;
      this.customObjectName = var2;
      this.standardObjects = var3;
   }

   private boolean isStandardObject() {
      return Strings.isNullOrEmpty(this.customObjectName);
   }

   public ConnectorNetSuiteCustomFields retrieve() {
      if (this.isStandardObject()) {
         NetSuiteCustomFieldsRetrieverObject var2 = new NetSuiteCustomFieldsRetrieverObject(this.factory, this.standardObjects);
         return var2.retrieve();
      } else {
         NetSuiteCustomFieldsRetrieverCustomObject var1 = new NetSuiteCustomFieldsRetrieverCustomObject(this.factory, this.customObjectName);
         return var1.retrieve();
      }
   }
}
