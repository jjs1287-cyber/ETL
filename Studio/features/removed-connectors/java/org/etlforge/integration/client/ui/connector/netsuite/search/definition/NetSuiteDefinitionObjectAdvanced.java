package org.etlforge.integration.client.ui.connector.netsuite.search.definition;

import java.io.Serializable;
import java.util.List;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteDefinitionObjectAdvanced implements Serializable {
   private static final String unsupportedCustomSearchJoin = "customSearchJoin";
   private final String searchObjectLocalName;
   private final String searchObjectNamspaceName;
   private String basicObjectKey;
   private final OrderedMap<String> searches = new OrderedMap<>();
   private final OrderedMap<String> searchRows = new OrderedMap<>();

   public NetSuiteDefinitionObjectAdvanced(String var1, String var2) {
      this.searchObjectLocalName = var2;
      this.searchObjectNamspaceName = var1;
      this.basicObjectKey = null;
   }

   private static final boolean isNotSupported(String var0) {
      return "customSearchJoin".equalsIgnoreCase(var0);
   }

   public void add(String var1, String var2) {
      if (!isNotSupported(var1)) {
         if (this.basicObjectKey == null) {
            String var3 = NetSuiteSearchObjectName.getObjectNameFromSearchAdvanced(this.searchObjectLocalName);
            String var4 = NetSuiteSearchObjectName.getObjectNameFromSearchBasic(var2);
            if (var3.equals(var4)) {
               this.basicObjectKey = var1;
            }
         }

         this.searches.put(var1, var2);
      }
   }

   public void addRow(String var1, String var2) {
      if (!isNotSupported(var1)) {
         this.searchRows.put(var1, var2);
      }
   }

   public String get(String var1) {
      return this.searches.get(var1);
   }

   public String getRow(String var1) {
      return this.searchRows.get(var1);
   }

   public boolean isBasicObjectKey(String var1) {
      return this.basicObjectKey.equals(var1);
   }

   public String getBasicObjectKey() {
      return this.basicObjectKey;
   }

   public String getSearchObjectNamspaceName() {
      return this.searchObjectNamspaceName;
   }

   public String getSearchObjectLocalName() {
      return this.searchObjectLocalName;
   }

   public List<String> getKeyList() {
      return this.searches.keyList();
   }

   public List<String> getRowKeyList() {
      return this.searchRows.keyList();
   }
}
