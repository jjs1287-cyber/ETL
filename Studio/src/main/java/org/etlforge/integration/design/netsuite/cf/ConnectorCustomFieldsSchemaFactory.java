package org.etlforge.integration.design.netsuite.cf;

import java.util.List;
import java.util.Map.Entry;
import org.etlforge.integration.design.netsuite.cf.meta.NetSuiteCustomField;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlVersion;

public class ConnectorCustomFieldsSchemaFactory {
   public static ConnectorCustomFieldsSchema convert(
      ConnectorNetSuiteCustomFields var0, boolean var1, NetSuiteCustomFieldTypeConverter var2, NetSuiteWsdlVersion var3
   ) {
      ConnectorCustomFieldsSchema var4 = new ConnectorCustomFieldsSchema(var3, var0.getCustomObjectId());

      for (Entry var6 : var0.getCustomFields().entrySet()) {
         for (Object __etlItem15 : (List)var6.getValue()) {
            ConnectorNetSuiteCustomField var8 = (ConnectorNetSuiteCustomField) __etlItem15;
            ConnectorCustomFieldSchema var9 = convert(var1, var2, var3.usingScriptIdForCustomField(), var8);
            var4.put((String)var6.getKey(), var9);
         }
      }

      return var4;
   }

   private static ConnectorCustomFieldSchema convert(boolean var0, NetSuiteCustomFieldTypeConverter var1, boolean var2, ConnectorNetSuiteCustomField var3) {
      NetSuiteCustomFieldType var4 = var3.getNetSuiteCustomFieldTypeExist(var1);
      NetSuiteCustomField var5 = var3.getNetSuiteCustomField();
      String var6 = var0 ? var4.getCustomFieldTypeSearchColumn() : var4.getCustomFieldType();
      String var7 = var2 ? var5.getScriptId() : var5.getInternalId();
      String var8 = var3.getCustomFieldXmlName();
      return new ConnectorCustomFieldSchema(var8, var2, var7, var6, var5.getFieldType());
   }
}
