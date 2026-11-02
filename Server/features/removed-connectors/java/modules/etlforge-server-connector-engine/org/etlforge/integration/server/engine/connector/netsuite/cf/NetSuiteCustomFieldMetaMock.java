package org.etlforge.integration.server.engine.connector.netsuite.cf;

import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldTypeConverter;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteObjectCustomFields;
import org.etlforge.integration.design.netsuite.lookup.NetSuiteCustomFieldsLookup;
import org.etlforge.integration.server.engine.connector.netsuite.definition.NetSuiteConfigInstance;

public class NetSuiteCustomFieldMetaMock implements NetSuiteCustomFieldsLookup {
   private final String objectName;
   private final String customizationType;
   private final NetSuiteObjectCustomFields customfields = new NetSuiteObjectCustomFields();

   public NetSuiteCustomFieldMetaMock(String var1, String var2) {
      this.objectName = var1;
      this.customizationType = var2;
   }

   @Override
   public NetSuiteObjectCustomFields getNetSuiteObjectsCustomFields() {
      return this.customfields;
   }

   public static NetSuiteCustomFieldMetaMock mock1(String var0) {
      NetSuiteCustomFieldMetaMock var1 = new NetSuiteCustomFieldMetaMock("objectName", "customizationType");
      NetSuiteCustomField var2 = new NetSuiteCustomField("Tax Rate", "internalId1", "scriptlId1", "_freeFormText");
      var1.customfields.add(var0, var2);
      return var1;
   }

   public static NetSuiteCustomFieldMetaMock mockAllType(String var0) {
      NetSuiteCustomFieldMetaMock var1 = new NetSuiteCustomFieldMetaMock("objectName", "customizationType");
      int var2 = 0;
      NetSuiteCustomFieldTypeConverter var3 = NetSuiteConfigInstance.getNetSuiteConfig().getNSCustomFieldTypeConverter();

      for (String var5 : var3.getAllFieldTypes()) {
         NetSuiteCustomField var6 = new NetSuiteCustomField(var5 + ++var2, "Id_" + var2, "scriptId_" + var2, var5);
         var1.customfields.add(var0, var6);
      }

      return var1;
   }
}
