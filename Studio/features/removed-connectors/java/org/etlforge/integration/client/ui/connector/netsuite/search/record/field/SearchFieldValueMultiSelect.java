package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import com.google.common.collect.Lists;
import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;

public class SearchFieldValueMultiSelect extends SearchFieldValue {
   private final List<NetSuiteRecordRef> searchValue = Lists.newArrayList();
   private final boolean isCustomField;

   public SearchFieldValueMultiSelect(NetSuiteSearchOperator var1, boolean var2) {
      super(var1);
      this.isCustomField = var2;
   }

   public void add(NetSuiteRecordRef var1) {
      this.searchValue.add(var1);
   }

   public List<NetSuiteRecordRef> getSearchValue() {
      return this.searchValue;
   }

   @Override
   protected void toXmlSearchFieldValue(StringBuilder var1) {
      for (NetSuiteRecordRef var3 : this.searchValue) {
         var3.toXml(var1, "searchValue", this.isCustomField);
      }
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append("  searchValue {");

      for (NetSuiteRecordRef var3 : this.searchValue) {
         var1.append("[").append(var3.toString()).append("] ");
      }

      var1.append("}");
      return var1.toString();
   }
}
