package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

public class SearchFieldValueBoolean extends SearchFieldValue {
   private final Boolean searchValue;

   public SearchFieldValueBoolean(boolean var1) {
      super(null);
      this.searchValue = var1;
   }

   public boolean getSearchValue() {
      return this.searchValue;
   }

   @Override
   protected void toXmlSearchFieldValue(StringBuilder var1) {
      toXmlSearchFieldValue(var1, this.searchValue.toString());
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder();
      toString(var1, this.searchValue.toString());
      return var1.toString();
   }
}
