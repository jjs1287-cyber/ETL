package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

public class NetSuiteSearchObjectValueRowFieldsHandler extends NetSuiteSearchObjectRowFieldsHandler {
   private final NetSuiteSearchRecordValue searchObjectValueRow;
   private final String joinKey;

   NetSuiteSearchObjectValueRowFieldsHandler(int var1, String var2, NetSuiteSearchRecordValue var3) {
      super(var1, var3.getNetSuiteSearchRecord().getNetSuiteSearchBasicRow(var2));
      this.searchObjectValueRow = var3;
      this.joinKey = var2;
   }

   @Override
   protected void add(String var1) {
      this.searchObjectValueRow.putRow(this.joinKey, var1);
   }
}
