package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.field.SearchFieldValueStringList;
import org.etlforge.integration.design.netsuite.config.NetSuiteCustomSearchSubtype;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteCustomSearch;

public class NetSuiteCustomSearchObject {
   public static final String customSearchTypeFieldName = "type";
   private final NetSuiteCustomSearch customsearch;
   private final List<String> customsearchObjects;

   public NetSuiteCustomSearchObject(NetSuiteCustomSearch var1, String var2) {
      this.customsearch = var1;
      this.customsearchObjects = Lists.newArrayList();
      this.customsearchObjects.add(var2);
   }

   public void setCustomSearchSubtypes(List<String> var1) {
      if (var1 != null && !var1.isEmpty()) {
         this.customsearchObjects.clear();
         this.customsearchObjects.addAll(var1);
      }
   }

   static boolean isCustomSearchTypeField(String var0) {
      return "type".equals(var0);
   }

   SearchFieldValueStringList getTypeFieldValue() {
      SearchFieldValueStringList var1 = new SearchFieldValueStringList(NetSuiteSearchOperator.anyOf);

      for (String var3 : this.customsearchObjects) {
         NetSuiteCustomSearchSubtype var4 = this.customsearch.get(var3);
         String var5 = var4.getSearchNetSuiteSubType();
         var1.add(var5);
      }

      return var1;
   }
}
