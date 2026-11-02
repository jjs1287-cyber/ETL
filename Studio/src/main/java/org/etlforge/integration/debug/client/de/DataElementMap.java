package org.etlforge.integration.debug.client.de;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.etlforge.system.SystemUtils;

public final class DataElementMap {
   private static final Comparator<DataElement> DISPLAY_ORDER = new DataElementMap.DisplayOrder();
   private final Map<DataElementMap.Key, DataElement> serverDataElements;
   private final Map<DataElementMap.Key, DataElement> scannedDataElements;

   public DataElementMap() {
      this.serverDataElements = Maps.newHashMap();
      this.scannedDataElements = Maps.newHashMap();
   }

   public DataElementMap(DataElementMap var1) {
      this.serverDataElements = Maps.newHashMap(var1.serverDataElements);
      this.scannedDataElements = Maps.newHashMap(var1.scannedDataElements);
   }

   public void reset() {
      for (DataElement var2 : this.serverDataElements.values()) {
         var2.setValue(null);
      }

      for (DataElement var4 : this.scannedDataElements.values()) {
         if (var4.getScope() == DataElementScope.GLOBAL) {
            var4.setValue(null);
         }
      }
   }

   public void stopTest() {
      for (Entry var2 : this.serverDataElements.entrySet()) {
         DataElement var3 = this.scannedDataElements.get(var2.getKey());
         if (var3 != null && var3.getScope() == DataElementScope.GLOBAL) {
            var3.setValue(((DataElement)var2.getValue()).getValue());
            var3.setTypeName(((DataElement)var2.getValue()).getTypeName());
         }
      }

      this.serverDataElements.clear();
   }

   public void processDataElementsReturnedFromServer(Iterable<DataElement> var1) {
      boolean var2 = false;
      replace(this.serverDataElements, var1, var2);
      this.updateValuesOfScannedDataElements(var1);
   }

   private static void replace(Map<DataElementMap.Key, DataElement> var0, Iterable<DataElement> var1, boolean var2) {
      HashMap var3 = null;
      if (var2) {
         var3 = Maps.newHashMap(var0);
      }

      var0.clear();

      for (DataElement var5 : var1) {
         DataElementMap.Key var6 = new DataElementMap.Key(var5);
         if (StringUtils.isEmpty(var5.getValue()) && var3 != null) {
            DataElement var7 = (DataElement)var3.get(var6);
            if (var7 != null) {
               var5.setValue(var7.getValue());
            }
         }

         var0.put(var6, var5);
      }
   }

   private void updateValuesOfScannedDataElements(Iterable<DataElement> var1) {
      for (DataElement var3 : var1) {
         DataElement var4 = this.scannedDataElements.get(new DataElementMap.Key(var3));
         if (var4 != null) {
            var4.setValue(var3.getValue());
            var4.setTypeName(var3.getTypeName());
            var4.setArrValues(var3.getArrValues());
         }
      }
   }

   public void addScannedDataElements(Iterable<DataElement> var1, boolean var2) {
      for (DataElement var4 : var1) {
         DataElementMap.Key var5 = new DataElementMap.Key(var4);
         DataElement var6 = this.scannedDataElements.get(var5);
         if (var6 == null) {
            this.scannedDataElements.put(var5, var4);
         } else if (var2) {
            var6.setValue(var4.getValue());
         }
      }
   }

   public void replaceScannedDataElements(Iterable<DataElement> var1) {
      boolean var2 = true;
      replace(this.scannedDataElements, var1, var2);
   }

   public int size() {
      return this.getVisibleDataElements().size();
   }

   public void removeAll() {
      this.serverDataElements.clear();
      this.scannedDataElements.clear();
   }

   public void addDataElementFromServer(DataElement var1) {
      this.serverDataElements.put(new DataElementMap.Key(var1), var1);
   }

   public void addDataElementScannedFromScript(DataElement var1) {
      this.scannedDataElements.put(new DataElementMap.Key(var1), var1);
   }

   public Collection<DataElement> getVisibleDataElements() {
      ArrayList var1 = Lists.newArrayList();

      for (Entry var3 : this.serverDataElements.entrySet()) {
         DataElement var4 = (DataElement)var3.getValue();
         if (var4.getScope() == DataElementScope.GLOBAL || var4.getScope() == DataElementScope.LOCAL || this.scannedDataElements.containsKey(var3.getKey())) {
            var1.add(var4);
         }
      }

      for (Entry var6 : this.scannedDataElements.entrySet()) {
         if (!this.serverDataElements.containsKey(var6.getKey())) {
            var1.add(var6.getValue());
         }
      }

      Collections.sort(var1, DISPLAY_ORDER);
      return var1;
   }

   public Collection<DataElement> getDataElements(Predicate<DataElement> var1) {
      return Collections2.filter(this.getVisibleDataElements(), var1);
   }

   public DataElement lookup(String var1, DataElementScope var2) {
      DataElementMap.Key var3 = new DataElementMap.Key(var1, var2);
      DataElement var4 = this.serverDataElements.get(var3);
      if (var4 == null) {
         var4 = this.scannedDataElements.get(var3);
      }

      return var4;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();

      for (DataElement var3 : this.getVisibleDataElements()) {
         var1.append(var3.getName()).append(" [").append(var3.getScope()).append("] = ").append(var3.getValue()).append(SystemUtils.LINE_SEPARATOR);
      }

      return var1.toString();
   }

   private static final class DisplayOrder implements Comparator<DataElement>, Serializable {
      private DisplayOrder() {
      }

      public int compare(DataElement var1, DataElement var2) {
         return ComparisonChain.start()
            .compare(var1.getName(), var2.getName(), String.CASE_INSENSITIVE_ORDER)
            .compare(var1.getScope(), var2.getScope())
            .result();
      }
   }

   private static final class Key {
      private final String name;
      private final DataElementScope scope;

      public Key(DataElement var1) {
         this(var1.getName(), var1.getScope());
      }

      public Key(String var1, DataElementScope var2) {
         this.name = var1.toLowerCase();
         this.scope = var2;
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof DataElementMap.Key)) {
            return false;
         }

         DataElementMap.Key var2 = (DataElementMap.Key)var1;
         return this.scope == var2.scope && this.name.equals(var2.name);
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + this.scope.hashCode();
         return 31 * var1 + this.name.hashCode();
      }
   }
}
