package org.etlforge.integration.etlforgeconnector;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

public class CaseInsensitiveMap<V> implements Serializable {
   final Map<String, V> map = Maps.newHashMap();

   public CaseInsensitiveMap() {
   }

   public CaseInsensitiveMap(Map<String, V> var1) {
      for (Entry var3 : var1.entrySet()) {
         this.put((String)var3.getKey(), (V)var3.getValue());
      }
   }

   public V get(String var1) {
      return this.map.get(var1.toLowerCase());
   }

   public void put(String var1, V var2) {
      this.map.put(var1.toLowerCase(), (V)var2);
   }
}
