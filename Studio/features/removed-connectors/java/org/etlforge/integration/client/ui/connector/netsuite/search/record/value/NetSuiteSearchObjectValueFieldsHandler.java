package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;

public class NetSuiteSearchObjectValueFieldsHandler extends NetSuiteSearchObjectFieldsHandler {
   private final NetSuiteSearchObjectValue searchObjectValue;

   NetSuiteSearchObjectValueFieldsHandler(int var1, NetSuiteSearchObjectValue var2) {
      super(var1, var2.getNetSuiteSearchBasic());
      this.searchObjectValue = var2;
   }

   @Override
   protected void put(String var1, SearchFieldValue var2) {
      this.searchObjectValue.put(var1, var2);
   }
}
