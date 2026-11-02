package org.etlforge.util.settings;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class SettingsMap implements Settings {
   private final Map<String, String> data = Maps.newHashMap();

   @Override
   public boolean contains(String var1) {
      return this.data.containsKey(var1);
   }

   @Override
   public String get(String var1, String var2) {
      return this.contains(var1) ? this.data.get(var1) : var2;
   }

   @Override
   public <T> T get(String var1, SettingsValueConverter<T> var2, T var3) {
      return (T)(this.contains(var1) ? var2.fromString(this.data.get(var1)) : var3);
   }

   @Override
   public void put(String var1, String var2) {
      Preconditions.checkNotNull(var1, "key");
      this.data.put(var1, var2);
   }

   @Override
   public <T> void put(String var1, T var2, SettingsValueConverter<T> var3) {
      Preconditions.checkNotNull(var1, "key");
      this.data.put(var1, var3.toString(var2));
   }

   public int size() {
      return this.data.size();
   }

   public boolean isEmpty() {
      return this.data.isEmpty();
   }

   public Iterable<Entry<String, String>> entrySet() {
      return new Iterable<Entry<String, String>>() {
         @Override
         public Iterator<Entry<String, String>> iterator() {
            return SettingsMap.this.data.entrySet().iterator();
         }
      };
   }
}
