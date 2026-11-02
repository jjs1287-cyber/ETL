package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;

public class SearchFieldValueStringList extends SearchFieldValue {
   private final List<String> searchValue = Lists.newArrayList();

   public SearchFieldValueStringList(NetSuiteSearchOperator var1) {
      super(var1);
   }

   public void add(String var1) {
      this.searchValue.add(var1);
   }

   public List<String> getSearchValue() {
      return this.searchValue;
   }

   @Override
   protected void toXmlSearchFieldValue(StringBuilder var1) {
      for (String var3 : this.searchValue) {
         toXmlSearchFieldValue(var1, var3);
      }
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("  searchValue [");
      boolean var2 = false;

      for (String var4 : this.searchValue) {
         if (var2) {
            var1.append(", ");
         } else {
            var2 = true;
         }

         var1.append(var4);
      }

      var1.append("]");
      return var1.toString();
   }
}
