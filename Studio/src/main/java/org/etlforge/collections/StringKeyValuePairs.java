package org.etlforge.collections;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import org.etlforge.lang.IntParser;
import org.etlforge.lang.LongParser;
import org.etlforge.string.StringMatcherPredicate;
import org.etlforge.string.WildcardStringMatcher;

public class StringKeyValuePairs implements Serializable {
   private final Map<String, String> data;

   public StringKeyValuePairs() {
      this.data = Collections.synchronizedMap(new TreeMap<>(String.CASE_INSENSITIVE_ORDER));
   }

   public StringKeyValuePairs(StringKeyValuePairs var1) {
      synchronized (var1) {
         HashMap var3 = Maps.newHashMapWithExpectedSize(var1.data.size());

         for (String var5 : var1.data.keySet()) {
            var3.put(var5, var1.data.get(var5));
         }

         this.data = Collections.synchronizedMap(var3);
      }
   }

   public StringKeyValuePairs(String[] var1, String[] var2) {
      if (var1.length != var2.length) {
         throw new IllegalArgumentException("Both the keys and values arrays have to be of the same size");
      }

      this.data = Collections.synchronizedMap(new HashMap<>());
      int var3 = 0;

      for (int var4 = var1.length; var3 < var4; var3++) {
         this.putString(var1[var3], var2[var3]);
      }
   }

   public int size() {
      return this.data.size();
   }

   public String get(String var1) {
      return this.getString(var1);
   }

   public String getString(String var1) {
      return var1 != null && var1.length() != 0 ? this.data.get(var1) : null;
   }

   public String get(String var1, String var2) {
      String var3 = this.getString(var1);
      return var3 != null ? var3 : var2;
   }

   public void put(String var1, String var2) {
      this.putString(var1, var2);
   }

   public void putString(String var1, String var2) {
      if (var1 != null && var1.length() != 0) {
         if (var2 == null) {
            this.remove(var1);
         } else {
            this.data.put(var1, var2);
         }
      }
   }

   public void putInt(String var1, int var2) {
      this.putString(var1, String.valueOf(var2));
   }

   public int getInt(String var1) throws NumberFormatException {
      return Integer.parseInt(this.getString(var1));
   }

   public int getInt(String var1, int var2) {
      return IntParser.parse(this.getString(var1), var2);
   }

   public void putLong(String var1, long var2) {
      this.putString(var1, String.valueOf(var2));
   }

   public long getLong(String var1) throws NumberFormatException {
      return Long.parseLong(this.getString(var1));
   }

   public long getLong(String var1, long var2) {
      return LongParser.parse(this.getString(var1), var2);
   }

   public void putBoolean(String var1, boolean var2) {
      this.putString(var1, var2 ? "1" : "0");
   }

   public boolean getBoolean(String var1) {
      String var2 = this.getString(var1);
      return isTrue(var2);
   }

   public boolean getBoolean(String var1, boolean var2) {
      String var3 = this.getString(var1);
      return var3 != null && var3.length() != 0 ? isTrue(var3) : var2;
   }

   private static boolean isTrue(String var0) {
      return "1".equals(var0) || "true".equalsIgnoreCase(var0);
   }

   public final <E extends Enum<E>> E getEnum(String var1, Class<E> var2, E var3) {
      String var4 = this.get(var1);
      if (var4 != null) {
         for (Enum var8 : (Enum[])var2.getEnumConstants()) {
            if (var8.name().equals(var4)) {
               return (E)var8;
            }
         }
      }

      return (E)var3;
   }

   public final <E extends Enum<E>> void putEnum(String var1, E var2) {
      this.put(var1, var2 == null ? null : var2.name());
   }

   public void putObject(String var1, Object var2) {
      this.putString(var1, var2 != null ? var2.toString() : null);
   }

   public void putAll(StringKeyValuePairs var1) {
      for (String var3 : var1.getKeys()) {
         this.putString(var3, var1.getString(var3));
      }
   }

   public void remove(String var1) {
      this.data.remove(var1);
   }

   public void wildcardRemove(String var1) {
      StringMatcherPredicate var2 = createWildcardFilter(var1);
      Iterables.removeIf(this.data.keySet(), var2);
   }

   private static StringMatcherPredicate createWildcardFilter(String var0) {
      WildcardStringMatcher var1 = new WildcardStringMatcher();
      var1.setLeadingMatchSufficient(false);
      return new StringMatcherPredicate(var1, var0);
   }

   public Set<String> wildcardKeyList(String var1) {
      StringMatcherPredicate var2 = createWildcardFilter(var1);
      HashSet var3 = Sets.newHashSet();

      for (String var5 : this.data.keySet()) {
         if (var2.apply(var5)) {
            var3.add(var5);
         }
      }

      return var3;
   }

   public Set<String> getKeys() {
      return new HashSet<>(this.data.keySet());
   }

   public void putKeyValuePair(String var1) {
      this.putKeyValuePair(var1, "=");
   }

   public void putKeyValuePair(String var1, String var2) {
      if (var1 != null && var2 != null) {
         int var3 = var1.indexOf(var2);
         if (var3 > 0) {
            String var4 = var1.substring(0, var3);
            String var5 = var1.substring(var3 + 1);
            this.putString(var4, var5);
         }
      }
   }

   public boolean containsKey(String var1) {
      return this.data.containsKey(var1);
   }

   public void putAll(Properties var1) {
      for (String var3 : var1.stringPropertyNames()) {
         this.data.put(var3, var1.getProperty(var3));
      }
   }

   @Override
   public String toString() {
      return this.data.toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof StringKeyValuePairs ? this.data.equals(((StringKeyValuePairs)var1).data) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.data.hashCode();
   }
}
