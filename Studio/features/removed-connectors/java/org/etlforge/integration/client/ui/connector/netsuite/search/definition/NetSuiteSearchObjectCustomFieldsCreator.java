package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import java.util.List;
import java.util.logging.Logger;
import org.etlforge.integration.client.ui.connector.netsuite.search.metadata.NetSuiteSearchCustomFieldMetaData;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObjectCustomFields;

public class NetSuiteSearchObjectCustomFieldsCreator {
   private final Logger mylog = Logger.getLogger("NetSuiteSearchObjectCustomFieldCreator");
   private final NetSuiteSearchFieldDictionary searchFieldTypes;
   private final NetSuiteSearchCustomFieldMetaData searchMetaData;

   public NetSuiteSearchObjectCustomFieldsCreator(NetSuiteSearchFieldDictionary var1, NetSuiteSearchCustomFieldMetaData var2) {
      this.searchFieldTypes = var1;
      this.searchMetaData = var2;
   }

   public NetSuiteSearchCustomFields newInstance() {
      NetSuiteSearchCustomFields var1 = new NetSuiteSearchCustomFields(this.searchMetaData.getCustomObject());

      for (String var3 : this.searchMetaData.getAllCategories()) {
         List var4 = this.searchMetaData.getCustomFields(var3);
         NetSuiteSearchObjectCustomFields var5 = this.newNetSuiteSearchObjectCustomFields(var3, var4);
         var1.put(var3, var5);
      }

      return var1;
   }

   public NetSuiteSearchObjectCustomFields newNetSuiteSearchObjectCustomFields(String var1, List<NetSuiteSearchCustomField> var2) {
      NetSuiteSearchObjectCustomFields var3 = new NetSuiteSearchObjectCustomFields(var1, this.searchMetaData.getCustomObject());

      for (NetSuiteSearchCustomField var5 : var2) {
         SearchField var6 = this.searchFieldTypes.getSearchField(var5.getSearchCustomFieldType());
         if (var6 == null) {
            String var7 = "NotImplemented: No type definition for the customfield:" + var5;
            this.mylog.warning(var7);
         } else {
            var3.addCustomField(var5, var6);
         }
      }

      return var3;
   }
}
