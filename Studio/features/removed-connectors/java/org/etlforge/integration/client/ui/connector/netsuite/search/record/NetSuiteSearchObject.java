package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import java.io.Serializable;
import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;

public class NetSuiteSearchObject implements Serializable {
   final String searchObjectLocalName;
   private final String searchObjectNamspaceName;
   private final NetSuiteSearchObjectSearchFields searchFields;

   public NetSuiteSearchObject(String var1, String var2) {
      this.searchObjectNamspaceName = var1;
      this.searchObjectLocalName = var2;
      this.searchFields = new NetSuiteSearchObjectSearchFields(var2);
   }

   public void addField(String var1, SearchField var2) {
      this.searchFields.addField(var1, var2);
   }

   Integer getFieldIndex(String var1) {
      return this.searchFields.getFieldIndex(var1);
   }

   List<String> getFieldNames(String var1) {
      return this.searchFields.getFieldNames(var1);
   }

   SearchField getSearchField(String var1) {
      return this.searchFields.getSearchField(var1);
   }

   public String getSearchObjectNamspaceName() {
      return this.searchObjectNamspaceName;
   }
}
