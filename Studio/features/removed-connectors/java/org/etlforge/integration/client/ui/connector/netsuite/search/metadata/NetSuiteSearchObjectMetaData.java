package org.etlforge.integration.client.ui.connector.netsuite.search.metadata;

import com.google.common.collect.Lists;
import java.util.List;

public class NetSuiteSearchObjectMetaData {
   private final String searchObjectLocalName;
   private final String searchObjectNamspaceName;
   private final List<NetSuiteSearchObjectFieldMetaData> searchFields = Lists.newArrayList();

   public NetSuiteSearchObjectMetaData(String var1, String var2) {
      this.searchObjectLocalName = var1;
      this.searchObjectNamspaceName = var2;
   }

   public void addSearchField(String var1, String var2) {
      this.searchFields.add(new NetSuiteSearchObjectFieldMetaData(var1, var2));
   }

   public String getSearchObjectLocalName() {
      return this.searchObjectLocalName;
   }

   public String getSearchObjectNamspaceName() {
      return this.searchObjectNamspaceName;
   }

   public List<NetSuiteSearchObjectFieldMetaData> getSearchFields() {
      return this.searchFields;
   }
}
