package org.etlforge.integration.server.engine.connector.netsuite.adapter;

import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookupIFactory;
import org.etlforge.integration.server.engine.connector.netsuite.cf.NetSuiteCustomFieldMetaDefault;
import org.etlforge.integration.server.engine.connector.netsuite.customobject.NetSuiteCustomFieldMetadataCustomObject;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;

public class NetSuiteCustomFieldsLookupFactory implements NetSuiteCustomFieldsLookupIFactory {
   private final NetSuitePassport passport;

   public NetSuiteCustomFieldsLookupFactory(NetSuitePassport var1) {
      this.passport = var1;
   }

   @Override
   public NetSuiteCustomFieldsLookup getNetSuiteCustomFieldsLookup(boolean var1, String var2) {
      return var1 ? this.newCustomNetSuiteCustomFieldsLookup(var2) : this.newStandardNetSuiteCustomFieldsLookup(var2);
   }

   private NetSuiteCustomFieldsLookup newStandardNetSuiteCustomFieldsLookup(String var1) {
      return new NetSuiteCustomFieldMetaDefault(this.passport, var1);
   }

   private NetSuiteCustomFieldsLookup newCustomNetSuiteCustomFieldsLookup(String var1) {
      return new NetSuiteCustomFieldMetadataCustomObject(this.passport, var1);
   }
}
