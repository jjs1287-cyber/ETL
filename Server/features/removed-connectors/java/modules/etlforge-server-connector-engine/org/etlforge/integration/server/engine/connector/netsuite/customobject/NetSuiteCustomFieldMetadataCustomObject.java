package org.etlforge.integration.server.engine.connector.netsuite.customobject;

import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.server.engine.connector.netsuite.endpoint.NetSuitePassport;
import org.etlforge.integration.server.engine.connector.netsuite.operation.NetSuiteCustomizationRef;

public class NetSuiteCustomFieldMetadataCustomObject implements NetSuiteCustomFieldsLookup {
   private final NetSuitePassport passport;
   private final String objectElementName;

   public NetSuiteCustomFieldMetadataCustomObject(NetSuitePassport var1, String var2) {
      this.passport = var1;
      this.objectElementName = var2;
   }

   @Override
   public NetSuiteObjectCustomFields getNetSuiteObjectsCustomFields() {
      return this.getCustomFields();
   }

   public NetSuiteObjectCustomFields getCustomFields() {
      NetSuiteCustomObjects var1 = NetSuiteCustomObjects.retrieve(this.passport);
      NetSuiteCustomizationRef var2 = var1.getNetSuiteCustomizationRefExists(this.objectElementName);
      NetSuiteCustomFieldMetadataCustomObjectId var3 = new NetSuiteCustomFieldMetadataCustomObjectId(this.passport, var2.getInternalId());
      return var3.getNetSuiteObjectsCustomFields();
   }

   public static void main(String[] var0) {
   }
}
