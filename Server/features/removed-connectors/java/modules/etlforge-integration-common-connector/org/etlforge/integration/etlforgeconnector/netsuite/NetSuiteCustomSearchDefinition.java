package org.etlforge.integration.etlforgeconnector.netsuite;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NetSuiteCustomSearchDefinition implements Serializable {
   public static String ITEM = "Item";
   public static String TRANSACTION = "Transaction";
   private final Map<String, NetSuiteCustomSearch> customSearches = Maps.newHashMap();
   private final Map<String, String> genericCustomSearchNames = Maps.newHashMap();
   private final Map<String, String> nameToCustomSearchObjectName = Maps.newHashMap();
   private final Map<String, String> connectorObjectList = Maps.newTreeMap();

   public NetSuiteCustomSearchDefinition() {
      this.genericCustomSearchNames.put(TRANSACTION, TRANSACTION);
      this.genericCustomSearchNames.put(ITEM, ITEM);
   }

   public Map<String, NetSuiteCustomSearch> getNetSuiteCustomSearchObjects() {
      return this.customSearches;
   }

   public Map<String, String> getGenericCustomSearchNames() {
      return this.genericCustomSearchNames;
   }

   public void put(String var1, String var2, String var3) {
      this.put(var1, var2, var3, null);
   }

   public boolean isGenericCustomSearch(String var1) {
      return this.genericCustomSearchNames.containsKey(var1);
   }

   public void put(String var1, String var2, String var3, List<String> var4) {
      this.nameToCustomSearchObjectName.put(var2.toLowerCase(), var1);
      if (Strings.isNullOrEmpty(var3)) {
         this.genericCustomSearchNames.put(var2, var1);
         this.connectorObjectList.put(var2, var1);
      } else {
         this.connectorObjectList.put(var2, var1);
         NetSuiteCustomSearch var5 = this.customSearches.get(var1);
         if (var5 == null) {
            var5 = new NetSuiteCustomSearch(var1);
            this.customSearches.put(var1, var5);
         }

         var5.put(var2, var3, var4);
      }
   }

   private String getCustomSearchObjectName(String var1) {
      return this.nameToCustomSearchObjectName.get(var1.toLowerCase());
   }

   public final NetSuiteCustomSearch getGenericCustomSearch(String var1) {
      String var2 = this.genericCustomSearchNames.get(var1);
      return var2 == null ? null : this.get(var2);
   }

   public final NetSuiteCustomSearch getNetSuiteCustomSearch(NetSuiteConnectorObject var1) {
      if (!var1.isCustomSearch()) {
         return null;
      } else {
         NetSuiteCustomSearch var2 = this.getNetSuiteCustomSearch(var1.getKey());
         if (var2 == null) {
            throw new RuntimeException("No custom search defintion found for " + var1.getKey());
         } else {
            return var2;
         }
      }
   }

   public final NetSuiteCustomSearch getNetSuiteCustomSearch(String var1) {
      String var2 = this.getCustomSearchObjectName(var1);
      return var2 == null ? null : this.get(var2);
   }

   public final NetSuiteCustomSearch get(String var1) {
      return this.customSearches.get(var1);
   }

   public boolean isCustomSearchObject(String var1) {
      return this.nameToCustomSearchObjectName.containsKey(var1.toLowerCase());
   }

   public Map<String, String> getCustomSearchObjects() {
      return this.connectorObjectList;
   }
}
