package org.etlforge.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;

public abstract class NameValuePairsParser {
   static final char NameValuePairSeparator = ';';
   static final char NameValueSeparator = '=';
   static final char[] NameValuePairSeparatorSplitter = new char[]{';'};
   private final boolean ignoreEmptyEntry;
   private final Map<String, NameValuePair> nameValuePairs = Maps.newLinkedHashMap();
   protected String pairName;
   protected String pairValue;

   protected abstract String[] splitNameValuePairsImpl(String var1);

   protected abstract void parserNameValuePairImpl(String var1);

   protected abstract String convertToString(NameValuePair var1);

   public NameValuePairsParser(boolean var1) {
      this.ignoreEmptyEntry = var1;
   }

   static boolean containsSeparator(String var0) {
      return var0.indexOf(59) >= 0 ? true : var0.indexOf(61) >= 0;
   }

   static boolean containsSeparator(String var0, String var1) {
      return containsSeparator(var0) ? true : containsSeparator(var1);
   }

   public boolean containsSeparator() {
      for (Entry var2 : this.nameValuePairs.entrySet()) {
         if (containsSeparator(((NameValuePair)var2.getValue()).getName(), ((NameValuePair)var2.getValue()).getValue())) {
            return true;
         }
      }

      return false;
   }

   public void put(String var1, String var2) {
      NameValuePair var3 = new NameValuePair(var1, var2);
      this.nameValuePairs.put(var3.getKey(), var3);
   }

   public String getValue(String var1) {
      NameValuePair var2 = new NameValuePair(var1, "");
      var2 = this.nameValuePairs.get(var2.getKey());
      return var2 == null ? "" : var2.getValue();
   }

   public int size() {
      return this.nameValuePairs.size();
   }

   public void init() {
      this.nameValuePairs.clear();
   }

   protected void parserNameValuePair(String var1) {
      this.pairName = var1;
      this.pairValue = "";
      if (!var1.isEmpty()) {
         this.parserNameValuePairImpl(var1);
      }

      this.put(this.pairName, this.pairValue);
   }

   public void parseNameValuePairs(String var1) {
      this.init();
      if (var1 != null) {
         var1 = var1.trim();
         if (!var1.isEmpty()) {
            String[] var2 = this.splitNameValuePairsImpl(var1);

            for (int var3 = 0; var3 < var2.length; var3++) {
               this.parserNameValuePair(var2[var3].trim());
            }
         }
      }
   }

   public String toNameValuePairs() {
      StringBuilder var1 = new StringBuilder(256);
      boolean var2 = true;

      for (Entry var4 : this.nameValuePairs.entrySet()) {
         NameValuePair var5 = (NameValuePair)var4.getValue();
         String var6 = var5.getValue();
         if (!this.ignoreEmptyEntry || !var6.isEmpty()) {
            if (var2) {
               var2 = false;
            } else {
               var1.append(';');
            }

            var1.append(this.convertToString(var5));
         }
      }

      return var1.toString();
   }
}
