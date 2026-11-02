package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchNamespacePrefixes;

public class NetSuiteSearchBasicRow {
   private final NetSuiteSearchObjectRow searchObjectRow;
   private final NetSuiteSearchObjectCustomFields searchObjectCustomFields;
   private final NetSuiteSearchNamespacePrefixes namespacePrefix;

   public NetSuiteSearchBasicRow(NetSuiteSearchObjectRow var1, NetSuiteSearchObjectCustomFields var2, NetSuiteSearchNamespacePrefixes var3) {
      this.namespacePrefix = var3;
      this.searchObjectRow = var1;
      this.searchObjectCustomFields = var2;
   }

   public NetSuiteSearchNamespacePrefixes getNamespacePrefix() {
      return this.namespacePrefix;
   }

   public List<String> getFieldNames() {
      List var1 = this.searchObjectRow.getFieldNames();
      if (this.searchObjectCustomFields != null) {
         var1.addAll(this.searchObjectCustomFields.getFieldNames());
      }

      return var1;
   }

   public boolean isCustomField(String var1) {
      return !this.searchObjectRow.contains(var1);
   }

   public Integer getFieldIndex(String var1) {
      Integer var2 = this.searchObjectRow.getFieldIndex(var1);
      return var2 != null ? var2 : this.searchObjectCustomFields.getFieldIndex(var1);
   }

   public String getSearchObjectLocalName() {
      return this.searchObjectRow.searchObjectLocalName;
   }

   public String getSearchObjectNamespace() {
      return this.searchObjectRow.searchObjectNamspaceName;
   }

   public NetSuiteSearchObjectCustomFields getNetSuiteSearchObjectCustomFields() {
      return this.searchObjectCustomFields;
   }

   public NetSuiteSearchCustomField getNetSuiteSearchCustomFieldByFieldName(String var1) {
      return this.searchObjectCustomFields.getNetSuiteSearchCustomFieldByFieldName(var1);
   }
}
