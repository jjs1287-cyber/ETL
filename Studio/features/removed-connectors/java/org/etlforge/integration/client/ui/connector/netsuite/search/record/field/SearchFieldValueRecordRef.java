package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.apache.commons.lang.NotImplementedException;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.NetSuiteSearchOperator;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchCustomField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchCustomFieldId;

public class SearchFieldValueRecordRef extends SearchFieldValue {
   private final NetSuiteRecordRef searchValue;

   public SearchFieldValueRecordRef(NetSuiteSearchOperator var1, NetSuiteRecordRef var2) {
      super(var1);
      this.searchValue = var2;
   }

   public NetSuiteRecordRef getSearchValue() {
      return this.searchValue;
   }

   @Override
   public void toXml(StringBuilder var1, String var2, NetSuiteSearchCustomField var3, NetSuiteSearchCustomFieldId var4) {
      this.searchValue.toXml(var1, var2, var3 != null);
   }

   @Override
   protected void toXmlSearchFieldValue(StringBuilder var1) {
      throw new NotImplementedException();
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(super.toString());
      var1.append(this.searchValue.toString());
      return var1.toString();
   }
}
