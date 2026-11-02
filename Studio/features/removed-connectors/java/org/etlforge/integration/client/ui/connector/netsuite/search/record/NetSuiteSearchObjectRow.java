package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetSuiteSearchObjectRow implements Serializable {
   private final Set<String> fields = Sets.newHashSet();
   private final Map<String, Integer> index = Maps.newHashMap();
   final String searchObjectLocalName;
   final String searchObjectNamspaceName;

   public NetSuiteSearchObjectRow(String var1, String var2) {
      this.searchObjectNamspaceName = var1;
      this.searchObjectLocalName = var2;
   }

   public void addField(String var1) {
      if (!this.fields.contains(var1)) {
         this.index.put(var1, this.fields.size());
         this.fields.add(var1);
      }
   }

   Integer getFieldIndex(String var1) {
      return this.index.get(var1);
   }

   List<String> getFieldNames() {
      ArrayList var1 = Lists.newArrayList();
      var1.addAll(this.fields);
      return var1;
   }

   public boolean contains(String var1) {
      return this.fields.contains(var1);
   }
}
