package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;

public class SearchFieldValueStringTwo extends SearchFieldValue {
   private final String searchValue;
   private final String searchValue2;

   public SearchFieldValueStringTwo(NetSuiteSearchOperator var1, String var2) {
      this(var1, var2, null);
   }

   public SearchFieldValueStringTwo(NetSuiteSearchOperator var1, String var2, String var3) {
      super(var1);
      this.searchValue = var2;
      this.searchValue2 = var3;
   }

   public String getSearchValue() {
      return this.searchValue;
   }

   public String getSearchValue2() {
      return this.searchValue2;
   }

   @Override
   protected void toXmlSearchFieldValue(StringBuilder var1) {
      toXmlSearchFieldValue2(var1, this.searchValue, this.searchValue2);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      toString2(var1, this.searchValue, this.searchValue2);
      return var1.toString();
   }
}
