package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;
import org.xml.sax.Attributes;

public class SearchFieldEnumMultiSelectSaxHandler extends SearchFieldSaxHandler {
   @Override
   public void startElementImpl(boolean var1, String var2, String var3, String var4, Attributes var5) {
      if (var1) {
         this.processOperator(var5);
         this.fieldValue = new SearchFieldValueStringList(this.selectedOperator);
      }
   }

   @Override
   public void endElementImpl(boolean var1, String var2, String var3, String var4, String var5) {
      if (!var1) {
         if (var3.equals("searchValue")) {
            SearchFieldValueStringList var6 = (SearchFieldValueStringList)this.fieldValue;
            var6.add(var5);
         }
      }
   }
}
