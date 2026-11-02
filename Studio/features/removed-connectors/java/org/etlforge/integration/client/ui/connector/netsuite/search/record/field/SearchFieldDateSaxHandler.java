package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;
import org.xml.sax.Attributes;

public class SearchFieldDateSaxHandler extends SearchFieldSaxHandler {
   private String predefinedSearchValue = null;
   private String searchValue = null;
   private String searchValue2 = null;

   @Override
   public void startElementImpl(boolean var1, String var2, String var3, String var4, Attributes var5) {
      if (var1) {
         this.processOperator(var5);
      }
   }

   @Override
   public void endElementImpl(boolean var1, String var2, String var3, String var4, String var5) {
      if (var1) {
         this.fieldValue = new SearchFieldValueDate(this.selectedOperator, this.predefinedSearchValue, this.searchValue, this.searchValue2);
         this.searchValue = this.searchValue2 = null;
      } else if (var3.equals("predefinedSearchValue")) {
         this.predefinedSearchValue = var5;
      } else if (var3.equals("searchValue")) {
         this.searchValue = var5;
      } else if (var3.equals("searchValue2")) {
         this.searchValue2 = var5;
      }
   }
}
