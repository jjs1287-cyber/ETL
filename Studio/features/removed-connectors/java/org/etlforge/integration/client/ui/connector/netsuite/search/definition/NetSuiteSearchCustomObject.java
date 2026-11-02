package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.NetSuiteRecordRef;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueRecordRef;

public class NetSuiteSearchCustomObject {
   public static final String customObjectSearchType = "CustomRecord";
   public static final String customObjectFieldName = "recType";
   private final String customObjectInternalId;

   public NetSuiteSearchCustomObject(String var1) {
      this.customObjectInternalId = var1;
   }

   String getCustomObjectInternalId() {
      return this.customObjectInternalId;
   }

   public SearchFieldValueRecordRef getCustomObjectValue() {
      Object var1 = null;
      NetSuiteRecordRef var2 = new NetSuiteRecordRef();
      var2.setInternalId(this.customObjectInternalId);
      return new SearchFieldValueRecordRef((NetSuiteSearchOperator)var1, var2);
   }

   public static boolean isCustomObjectTypeField(String var0) {
      return "recType".equals(var0);
   }
}
