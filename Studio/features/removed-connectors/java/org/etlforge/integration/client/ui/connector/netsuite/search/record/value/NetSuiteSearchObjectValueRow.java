package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasicRow;

public class NetSuiteSearchObjectValueRow {
   private final NetSuiteSearchBasicRow searchBasicRow;
   private final Map<String, Integer> fields = Maps.newTreeMap();

   public NetSuiteSearchObjectValueRow(NetSuiteSearchBasicRow var1) {
      this.searchBasicRow = var1;
   }

   public NetSuiteSearchBasicRow getNetSuiteSearchBasicRow() {
      return this.searchBasicRow;
   }

   public void add(String var1) {
      Integer var2 = this.searchBasicRow.getFieldIndex(var1);
      this.fields.put(var1, var2);
   }

   public Set<Entry<Integer, String>> getFiltersIndexSet() {
      TreeMap var1 = Maps.newTreeMap();

      for (Entry var3 : this.fields.entrySet()) {
         var1.put(var3.getValue(), var3.getKey());
      }

      return var1.entrySet();
   }

   public Set<String> getFilters() {
      return this.fields.keySet();
   }

   public boolean hasFilters() {
      return this.fields.size() > 0;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this.hasFilters()) {
         var1.append("columns: ");
      }

      for (String var3 : this.getFilters()) {
         var1.append(var3).append(";");
      }

      return var1.toString();
   }
}
