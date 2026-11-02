package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasic;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchObjectSpecialField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValue;

public class NetSuiteSearchObjectValueFields {
   private final NetSuiteSearchBasic searchObjectFields;
   private final Map<String, SearchFieldValue> filters = Maps.newHashMap();
   private final SortedMap<Integer, String> filtersIndex = Maps.newTreeMap();

   public NetSuiteSearchObjectValueFields(NetSuiteSearchBasic var1) {
      this.searchObjectFields = var1;
   }

   public NetSuiteSearchBasic getNetSuiteSearchObjectAllFields() {
      return this.searchObjectFields;
   }

   public void setSpecialTypeField() {
      if (this.searchObjectFields.requireSpecialField()) {
         NetSuiteSearchObjectSpecialField var1 = this.searchObjectFields.getNetSuiteSearchObjectSpecialField();
         this.put(var1.getFieldName(), var1.getSearchFieldValue());
      }
   }

   public void put(String var1, SearchFieldValue var2) {
      Integer var3 = this.searchObjectFields.getFieldIndex(var1);
      this.filtersIndex.put(var3, var1);
      this.filters.put(var1, var2);
   }

   SearchFieldValue get(String var1) {
      return this.filters.get(var1);
   }

   public final Map<String, SearchFieldValue> getFilters() {
      return this.filters;
   }

   public boolean hasFilters() {
      return this.filters.size() > 0;
   }

   final SortedMap<Integer, String> getFiltersIndex() {
      return this.filtersIndex;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();

      for (Entry var3 : this.filters.entrySet()) {
         var1.append((String)var3.getKey()).append(":").append(var3.toString());
      }

      return var1.toString();
   }
}
