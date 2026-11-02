package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.OrderedMap;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;

public class NetSuiteSearchAdvanced {
   final String searchObjectLocalName;
   final String searchObjectNamspaceName;
   private final String basicObjectKey;
   final OrderedMap<NetSuiteSearchBasic> criteria = new OrderedMap<>();
   final OrderedMap<NetSuiteSearchBasicRow> columns = new OrderedMap<>();

   public NetSuiteSearchAdvanced(String var1, String var2, String var3) {
      this.searchObjectNamspaceName = var1;
      this.searchObjectLocalName = var2;
      this.basicObjectKey = var3;
   }

   String getBasicObjectKey() {
      return this.basicObjectKey;
   }

   String getSearchObjectLocalName() {
      return this.searchObjectLocalName;
   }

   String getSearchObjectNamespace() {
      return this.searchObjectNamspaceName;
   }

   String getNameOfObjectType() {
      return NetSuiteSearchObjectName.getObjectNameFromSearchAdvanced(this.getSearchObjectLocalName());
   }

   void setCustomSearchSubtypes(List<String> var1) {
      NetSuiteSearchBasic var2 = this.getNetSuiteSearchBasic();
      var2.setCustomSearchSubtypes(var1);
   }

   NetSuiteSearchBasic getNetSuiteSearchBasic() {
      return this.getNetSuiteSearchBasic(this.basicObjectKey);
   }

   public void putNetSuiteSearchBasic(NetSuiteSearchBasic var1) {
      this.criteria.put(this.basicObjectKey, var1);
   }

   void putNetSuiteSearchBasic(String var1, NetSuiteSearchBasic var2) {
      this.criteria.put(var1, var2);
   }

   void putNetSuiteSearchBasicRow(String var1, NetSuiteSearchBasicRow var2) {
      this.columns.put(var1, var2);
   }

   public NetSuiteSearchBasic getNetSuiteSearchBasic(String var1) {
      return this.criteria.get(var1);
   }

   public NetSuiteSearchBasicRow getNetSuiteSearchBasicRow(String var1) {
      return this.columns.get(var1);
   }

   public List<String> getNetSuiteSearchBasicKeys() {
      return this.criteria.keyList();
   }

   public List<String> getNetSuiteSearchBasicRowKeys() {
      return this.columns.keyList();
   }
}
