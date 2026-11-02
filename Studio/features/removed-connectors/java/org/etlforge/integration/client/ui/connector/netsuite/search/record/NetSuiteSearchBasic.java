package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchNamespacePrefixes;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;

public class NetSuiteSearchBasic {
   private final NetSuiteSearchObjectAllFields searchBasic;
   private final NetSuiteSearchNamespacePrefixes namespacePrefix;
   private final NetSuiteSearchObjectSpecialField objectSpecialField;

   public NetSuiteSearchBasic(NetSuiteSearchObjectAllFields var1, NetSuiteSearchNamespacePrefixes var2, NetSuiteSearchObjectSpecialField var3) {
      this.namespacePrefix = var2;
      this.searchBasic = var1;
      this.objectSpecialField = var3;
   }

   public NetSuiteSearchNamespacePrefixes getNamespacePrefix() {
      return this.namespacePrefix;
   }

   public void setCustomSearchSubtypes(List<String> var1) {
      this.objectSpecialField.setCustomSearchSubtypes(var1);
   }

   public String getSearchObjectLocalName() {
      return this.searchBasic.getSearchObjectLocalName();
   }

   public String getSearchObjectNamespace() {
      return this.searchBasic.getSearchObjectNamespace();
   }

   public boolean requireSpecialField() {
      return this.objectSpecialField != null && this.objectSpecialField.requireSpecialField();
   }

   public NetSuiteSearchObjectSpecialField getNetSuiteSearchObjectSpecialField() {
      return this.objectSpecialField;
   }

   public List<String> getFieldNames() {
      String var1 = null;
      if (this.requireSpecialField()) {
         var1 = this.objectSpecialField.getFieldName();
      }

      return this.searchBasic.getFieldNames(var1);
   }

   public SearchField getSearchField(String var1) {
      return this.searchBasic.getSearchField(var1);
   }

   public NetSuiteSearchObjectCustomFields getNetSuiteSearchObjectCustomFields() {
      return this.searchBasic.getNetSuiteSearchObjectCustomFields();
   }

   public boolean isCustomField(String var1) {
      return this.searchBasic.isCustomField(var1);
   }

   public Integer getFieldIndex(String var1) {
      return this.searchBasic.getFieldIndex(var1);
   }

   public Set<String> getNamespaces() {
      return Sets.newHashSet();
   }
}
