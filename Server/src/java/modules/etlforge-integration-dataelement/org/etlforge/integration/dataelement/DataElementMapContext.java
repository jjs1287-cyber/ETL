package org.etlforge.integration.dataelement;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class DataElementMapContext implements DataElementContext {
   private final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

   public DataElementMapContext() {
   }

   public DataElementMapContext(Map<String, String> var1) {
      for (Entry var3 : var1.entrySet()) {
         this.putValue((String)var3.getKey(), (String)var3.getValue());
      }
   }

   public void putValue(String var1, String var2) {
      this.map.put(var1.toLowerCase(), var2);
   }

   @Override
   public String getValue(String var1) {
      return this.map.get(var1.toLowerCase());
   }

   @Override
   public String toString() {
      return this.map.toString();
   }
}
