package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import java.util.Map;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasic;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;

public class NetSuiteSearchObjectValue {
   final NetSuiteSearchBasic searchObjectFields;
   final NetSuiteSearchObjectValueFields searchObjectValueFields;

   public NetSuiteSearchObjectValue(NetSuiteSearchBasic var1) {
      this.searchObjectFields = var1;
      this.searchObjectValueFields = new NetSuiteSearchObjectValueFields(var1);
   }

   public NetSuiteSearchBasic getNetSuiteSearchBasic() {
      return this.searchObjectFields;
   }

   public NetSuiteSearchObjectValueFields getNetSuiteSearchObjectValueFields() {
      return this.searchObjectValueFields;
   }

   public NetSuiteSearchBasic getNetSuiteSearchObjectAllFields() {
      return this.searchObjectFields;
   }

   public void put(String var1, SearchFieldValue var2) {
      this.searchObjectValueFields.put(var1, var2);
   }

   public final Map<String, SearchFieldValue> getFilters() {
      return this.searchObjectValueFields.getFilters();
   }

   public String getSearchObjectLocalName() {
      return this.searchObjectFields.getSearchObjectLocalName();
   }

   public String getSearchObjectNamespace() {
      return this.searchObjectFields.getSearchObjectNamespace();
   }

   public boolean hasFilters() {
      return this.searchObjectValueFields.hasFilters();
   }

   void setSpecialTypeField() {
      this.searchObjectValueFields.setSpecialTypeField();
   }

   @Override
   public String toString() {
      return this.searchObjectValueFields.toString();
   }
}
