package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;

public class NetSuiteSearchObjectValueFieldsAdvancedHandler extends NetSuiteSearchObjectFieldsHandler {
   private final NetSuiteSearchRecordValue searchObjectValue;
   private final String joinKey;

   NetSuiteSearchObjectValueFieldsAdvancedHandler(int var1, String var2, NetSuiteSearchRecordValue var3) {
      super(var1, var3.getNetSuiteSearchRecord().getNetSuiteSearchBasic(var2));
      this.searchObjectValue = var3;
      this.joinKey = var2;
   }

   @Override
   protected void put(String var1, SearchFieldValue var2) {
      this.searchObjectValue.put(this.joinKey, var1, var2);
   }
}
