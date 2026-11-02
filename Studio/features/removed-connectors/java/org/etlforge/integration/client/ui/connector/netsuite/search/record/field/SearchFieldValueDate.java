package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import com.google.common.base.Strings;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;

public class SearchFieldValueDate extends SearchFieldValue {
   private final String predefinedSearchValue;
   private final String searchValue;
   private final String searchValue2;

   public SearchFieldValueDate(NetSuiteSearchOperator var1, String var2, String var3, String var4) {
      super(var1);
      this.predefinedSearchValue = var2;
      this.searchValue = var3;
      this.searchValue2 = var4;
   }

   public SearchFieldValueDate(NetSuiteSearchOperator var1, String var2) {
      this(var1, var2, null, null);
   }

   public SearchFieldValueDate(NetSuiteSearchOperator var1, String var2, String var3) {
      this(var1, null, var2, var3);
   }

   public String getPredefinedSearchValue() {
      return this.predefinedSearchValue;
   }

   public String getSearchValue() {
      return this.searchValue;
   }

   public String getSearchValue2() {
      return this.searchValue2;
   }

   @Override
   protected void toXmlSearchFieldValue(StringBuilder var1) {
      if (Strings.isNullOrEmpty(this.predefinedSearchValue)) {
         toXmlSearchFieldValue2(var1, this.searchValue, this.searchValue2);
      } else {
         toXmlSearchFieldValueImpl(var1, "predefinedSearchValue", this.predefinedSearchValue);
      }
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      if (Strings.isNullOrEmpty(this.predefinedSearchValue)) {
         toString2(var1, this.searchValue, this.searchValue2);
      } else {
         append(var1, "predefinedSearchValue", this.predefinedSearchValue);
      }

      return var1.toString();
   }
}
