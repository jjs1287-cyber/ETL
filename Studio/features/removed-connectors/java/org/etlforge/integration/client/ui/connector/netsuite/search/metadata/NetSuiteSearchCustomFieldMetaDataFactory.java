package org.etlforge.integration.client.ui.connector.netsuite.search.metadata;

import com.google.common.base.Strings;
import java.util.List;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchCustomObject;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.design.netsuite.cf.ConnectorNetSuiteCustomField;
import org.etlforge.integration.design.netsuite.cf.ConnectorNetSuiteCustomFields;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldType;
import org.etlforge.integration.design.netsuite.cf.NetSuiteCustomFieldTypeConverter;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;

public class NetSuiteSearchCustomFieldMetaDataFactory {
   public static NetSuiteSearchCustomFieldMetaData create(String var0, ConnectorNetSuiteCustomFields var1, NetSuiteDefinitionDesign var2) {
      NetSuiteCustomFieldTypeConverter var3 = var2.getNetSuiteConfig().getNSCustomFieldTypeConverter();
      return create(var0, var1, var3);
   }

   public static NetSuiteSearchCustomFieldMetaData create(String var0, ConnectorNetSuiteCustomFields var1, NetSuiteCustomFieldTypeConverter var2) {
      String var3 = var1.getCustomObjectId();
      NetSuiteSearchCustomObject var4 = Strings.isNullOrEmpty(var3) ? null : new NetSuiteSearchCustomObject(var3);
      NetSuiteSearchCustomFieldMetaData var5 = new NetSuiteSearchCustomFieldMetaData(var0, var4);

      for (Entry var7 : var1.getCustomFields().entrySet()) {
         for (Object __etlItem26 : (List)var7.getValue()) {
            ConnectorNetSuiteCustomField var9 = (ConnectorNetSuiteCustomField) __etlItem26;
            NetSuiteCustomFieldType var10 = var9.getNetSuiteCustomFieldTypeExist(var2);
            var5.add(
               (String)var7.getKey(),
               NetSuiteSearchCustomField.getNameWithPrefix(var9.getCustomFieldXmlName()),
               var9.getNetSuiteCustomField().getScriptId(),
               var10.getCustomFieldTypeSearch(),
               var10.getCustomFieldTypeSearchColumn()
            );
         }
      }

      return var5;
   }
}
