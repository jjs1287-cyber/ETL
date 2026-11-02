package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteDefinitionQueryBuilder;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchNamespacePrefixes;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;

public class NetSuiteSearchRecord {
   private final NetSuiteConnectorObject netsuiteObject;
   private final NetSuiteDefinitionQueryBuilder searchDef;
   private final NetSuiteSearchAdvanced advanced;

   public NetSuiteSearchRecord(NetSuiteConnectorObject var1, NetSuiteSearchAdvanced var2, NetSuiteDefinitionQueryBuilder var3) {
      this.advanced = var2;
      this.searchDef = var3;
      this.netsuiteObject = var1;
   }

   public void setCustomSearchSubtypes(List<String> var1) {
      this.advanced.setCustomSearchSubtypes(var1);
   }

   public NetSuiteSearchNamespacePrefixes getNamespacePrefix() {
      return this.searchDef.getNamespacePrefix();
   }

   public String getSearchObjectLocalName() {
      return this.advanced.getSearchObjectLocalName();
   }

   public String getSearchObjectNamespace() {
      return this.advanced.getSearchObjectNamespace();
   }

   public NetSuiteSearchBasic getNetSuiteSearchBasic() {
      return this.advanced.getNetSuiteSearchBasic();
   }

   public SearchField getSearchField(String var1, String var2) {
      NetSuiteSearchBasic var3 = this.getNetSuiteSearchBasic(var1);
      return var3.getSearchField(var2);
   }

   public NetSuiteSearchBasic getNetSuiteSearchBasic(String var1) {
      return this.advanced.getNetSuiteSearchBasic(var1);
   }

   public NetSuiteSearchBasicRow getNetSuiteSearchBasicRow(String var1) {
      return this.advanced.getNetSuiteSearchBasicRow(var1);
   }

   public List<String> getNetSuiteSearchBasicKeys() {
      return this.advanced.getNetSuiteSearchBasicKeys();
   }

   public List<String> getNetSuiteSearchBasicRowKeys() {
      return this.advanced.getNetSuiteSearchBasicRowKeys();
   }

   public boolean isBasicObjectKey(String var1) {
      return var1.equals(this.getBasicObjectKey());
   }

   public String getBasicObjectKey() {
      return this.advanced.getBasicObjectKey();
   }

   public NetSuiteConnectorObject getNetSuiteConnectorObject() {
      return this.netsuiteObject;
   }

   public String getNameOfObjectType() {
      return this.advanced.getNameOfObjectType();
   }
}
