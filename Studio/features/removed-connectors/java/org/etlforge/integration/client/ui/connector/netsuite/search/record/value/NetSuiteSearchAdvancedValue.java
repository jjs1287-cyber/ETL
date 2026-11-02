package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchNamespacePrefixes;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasic;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasicRow;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;

public class NetSuiteSearchAdvancedValue {
   private String savedSearchId;
   private final NetSuiteSearchRecord searchRecord;
   final Map<String, NetSuiteSearchObjectValue> criteria = Maps.newHashMap();
   final Map<String, NetSuiteSearchObjectValueRow> columns = Maps.newHashMap();

   NetSuiteSearchAdvancedValue(NetSuiteSearchRecord var1) {
      this.searchRecord = var1;
   }

   NetSuiteSearchObjectValue getNetSuiteSearchObjectValue(String var1) {
      return this.criteria.get(var1);
   }

   private NetSuiteSearchObjectValue getOrNewNetSuiteSearchObjectValue(String var1) {
      NetSuiteSearchObjectValue var2 = this.getNetSuiteSearchObjectValue(var1);
      if (var2 == null) {
         var2 = new NetSuiteSearchObjectValue(this.searchRecord.getNetSuiteSearchBasic(var1));
         this.criteria.put(var1, var2);
      }

      return var2;
   }

   final Map<String, NetSuiteSearchObjectValue> getCriteria() {
      return this.criteria;
   }

   final Map<String, NetSuiteSearchObjectValueRow> getColumns() {
      return this.columns;
   }

   boolean hasCriteria() {
      return !this.criteria.isEmpty();
   }

   boolean hasColumns() {
      return !this.columns.isEmpty();
   }

   void put(String var1, String var2, SearchFieldValue var3) {
      NetSuiteSearchObjectValue var4 = this.getOrNewNetSuiteSearchObjectValue(var1);
      var4.put(var2, var3);
   }

   NetSuiteSearchObjectValueRow getNetSuiteSearchObjectValueRow(String var1) {
      return this.columns.get(var1);
   }

   void putRow(String var1, String var2) {
      NetSuiteSearchObjectValueRow var3 = this.getNetSuiteSearchObjectValueRow(var1);
      if (var3 == null) {
         NetSuiteSearchBasicRow var4 = this.searchRecord.getNetSuiteSearchBasicRow(var1);
         var3 = new NetSuiteSearchObjectValueRow(var4);
         this.columns.put(var1, var3);
      }

      var3.add(var2);
   }

   NetSuiteSearchNamespacePrefixes getNamespacePrefix() {
      return this.searchRecord.getNamespacePrefix();
   }

   String getSavedSearchId() {
      return this.savedSearchId;
   }

   void setSavedSearchId(String var1) {
      if (var1 != null) {
         var1 = var1.trim();
      }

      this.savedSearchId = var1;
   }

   public NetSuiteSearchCategory getNetSuiteSearchTypeFromValue() {
      if (!Strings.isNullOrEmpty(this.savedSearchId)) {
         return NetSuiteSearchCategory.NetSuiteSearchAdvancedSavedSearch;
      } else if (this.hasColumns()) {
         return NetSuiteSearchCategory.NetSuiteSearchAdvanced;
      } else if (this.criteria.size() > 1) {
         return NetSuiteSearchCategory.NetSuiteSearch;
      } else {
         return !this.criteria.containsKey(this.searchRecord.getBasicObjectKey()) && this.criteria.size() > 0
            ? NetSuiteSearchCategory.NetSuiteSearch
            : NetSuiteSearchCategory.NetSuiteSearchBasic;
      }
   }

   Set<String> getNamespaces() {
      HashSet var1 = Sets.newHashSet();
      var1.add(this.searchRecord.getSearchObjectNamespace());
      var1.add(this.searchRecord.getNetSuiteSearchBasic().getSearchObjectNamespace());

      for (Entry var3 : this.columns.entrySet()) {
         NetSuiteSearchBasicRow var4 = ((NetSuiteSearchObjectValueRow)var3.getValue()).getNetSuiteSearchBasicRow();
         var1.add(var4.getSearchObjectNamespace());
      }

      for (Entry var6 : this.criteria.entrySet()) {
         NetSuiteSearchBasic var7 = ((NetSuiteSearchObjectValue)var6.getValue()).getNetSuiteSearchBasic();
         var1.add(var7.getSearchObjectNamespace());
      }

      return var1;
   }

   void setSpecialTypeField() {
      NetSuiteSearchBasic var1 = this.searchRecord.getNetSuiteSearchBasic();
      if (var1.requireSpecialField()) {
         NetSuiteSearchObjectValue var2 = this.getOrNewNetSuiteSearchObjectValue(this.searchRecord.getBasicObjectKey());
         var2.setSpecialTypeField();
      }
   }
}
