package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class OrderedMap<V> implements Serializable {
   private final SortedMap<String, V> map = Maps.newTreeMap();
   private final Map<String, Integer> index = Maps.newHashMap();
   private final List<String> keyList = Lists.newArrayList();

   public void put(String var1, V var2) {
      if (!this.containsKey(var1)) {
         this.index.put(var1, this.map.size());
         this.keyList.add(var1);
      }

      this.map.put(var1, (V)var2);
   }

   public boolean containsKey(String var1) {
      return this.map.containsKey(var1);
   }

   public Integer getIndexOfKey(String var1) {
      return this.index.get(var1);
   }

   public List<String> keyList() {
      return this.keyList;
   }

   public V get(String var1) {
      return this.map.get(var1);
   }

   public int size() {
      return this.map.size();
   }

   public boolean isEmpty() {
      return this.map.isEmpty();
   }
}
