package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchCustomFieldId;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.XmlUtils;

public class SearchFieldValueString extends SearchFieldValue {
   private final boolean isSearchFieldPostingPeriodDate;
   private final String searchValue;

   public SearchFieldValueString(NetSuiteSearchOperator var1, String var2, boolean var3) {
      super(var1);
      this.searchValue = var2;
      this.isSearchFieldPostingPeriodDate = var3;
   }

   public SearchFieldValueString(NetSuiteSearchOperator var1, String var2) {
      this(var1, var2, false);
   }

   public String getSearchValue() {
      return this.searchValue;
   }

   @Override
   public void toXml(StringBuilder var1, String var2, NetSuiteSearchCustomField var3, NetSuiteSearchCustomFieldId var4) {
      if (this.isSearchFieldPostingPeriodDate) {
         XmlUtils.ElementValue(var1, var2, this.searchValue);
      } else {
         super.toXml(var1, var2, var3, var4);
      }
   }

   @Override
   protected void toXmlSearchFieldValue(StringBuilder var1) {
      toXmlSearchFieldValue(var1, this.searchValue);
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      toString(var1, this.searchValue);
      return var1.toString();
   }
}
