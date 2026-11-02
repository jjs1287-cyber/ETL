package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import java.util.Map;
import java.util.Set;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchAdvancedProperties;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;

public class NetSuiteSearchRecordValue {
   private final NetSuiteSearchRecord searchRecord;
   private final NetSuiteSearchAdvancedValue searchRecordValue;

   public NetSuiteSearchRecordValue(NetSuiteSearchRecord var1) {
      this.searchRecord = var1;
      this.searchRecordValue = new NetSuiteSearchAdvancedValue(var1);
   }

   public NetSuiteSearchRecord getNetSuiteSearchRecord() {
      return this.searchRecord;
   }

   NetSuiteSearchObjectValue getNetSuiteSearchObjectValueBasic() {
      return this.searchRecordValue.getNetSuiteSearchObjectValue(this.searchRecord.getBasicObjectKey());
   }

   public final Map<String, NetSuiteSearchObjectValue> getCriteria() {
      return this.searchRecordValue.getCriteria();
   }

   public final Map<String, NetSuiteSearchObjectValueRow> getColumns() {
      return this.searchRecordValue.getColumns();
   }

   NetSuiteSearchObjectValue getNetSuiteSearchObjectValue(String var1) {
      return this.searchRecordValue.getNetSuiteSearchObjectValue(var1);
   }

   public void put(String var1, String var2, SearchFieldValue var3) {
      this.searchRecordValue.put(var1, var2, var3);
   }

   public NetSuiteSearchObjectValueRow getNetSuiteSearchObjectValueRow(String var1) {
      return this.searchRecordValue.getNetSuiteSearchObjectValueRow(var1);
   }

   public void putRow(String var1, String var2) {
      this.searchRecordValue.putRow(var1, var2);
   }

   public NetSuiteSearchCategory getNetSuiteSearchTypeFromValue() {
      return this.searchRecordValue.getNetSuiteSearchTypeFromValue();
   }

   public String toXml(NetSuiteSearchAdvancedProperties var1) {
      NetSuiteSearchCategory var2 = this.getNetSuiteSearchTypeFromValue();
      if (!var2.isSavedSearch()) {
         this.searchRecordValue.setSpecialTypeField();
      }

      NetSuiteSearchRecordValueFormatter var3 = new NetSuiteSearchRecordValueFormatter(this);
      return var3.toXml(var1);
   }

   public String getSavedSearchId() {
      return this.searchRecordValue.getSavedSearchId();
   }

   public void setSavedSearchId(String var1) {
      this.searchRecordValue.setSavedSearchId(var1);
   }

   Set<String> getNamespaces() {
      return this.searchRecordValue.getNamespaces();
   }

   boolean hasCriteria() {
      return this.searchRecordValue.hasCriteria();
   }

   boolean hasColumns() {
      return this.searchRecordValue.hasColumns();
   }
}
