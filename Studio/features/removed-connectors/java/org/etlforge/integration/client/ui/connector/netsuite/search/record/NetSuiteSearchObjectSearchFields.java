package org.etlforge.integration.client.ui.connector.netsuite.search.record;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.ConnectorNetSuiteWsdlError;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.OrderedMap;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;

public class NetSuiteSearchObjectSearchFields implements Serializable {
   private final OrderedMap<SearchField> fields = new OrderedMap<>();
   private final String searchObjectLocalName;

   NetSuiteSearchObjectSearchFields(String var1) {
      this.searchObjectLocalName = var1;
   }

   void addField(String var1, SearchField var2) {
      if (this.fields.containsKey(var1)) {
         throw new ConnectorNetSuiteWsdlError("duplicate field names " + var1 + "for search object " + this.searchObjectLocalName);
      }

      this.fields.put(var1, var2);
   }

   Integer getFieldIndex(String var1) {
      return this.fields.getIndexOfKey(var1);
   }

   List<String> getFieldNames(String var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.addAll(this.fields.keyList());
      if (!Strings.isNullOrEmpty(var1)) {
         var2.remove(var1);
      }

      Collections.sort(var2);
      return var2;
   }

   SearchField getSearchField(String var1) {
      return this.fields.get(var1);
   }
}
