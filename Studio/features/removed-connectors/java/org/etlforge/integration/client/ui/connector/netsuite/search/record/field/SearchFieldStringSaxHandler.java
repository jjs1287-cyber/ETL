package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;
import org.xml.sax.Attributes;

public class SearchFieldStringSaxHandler extends SearchFieldSaxHandler {
   @Override
   public void startElementImpl(boolean var1, String var2, String var3, String var4, Attributes var5) {
      if (var1) {
         this.processOperator(var5);
      }
   }

   @Override
   public void endElementImpl(boolean var1, String var2, String var3, String var4, String var5) {
      if (var1) {
         if (this.fieldValue == null) {
            this.fieldValue = new SearchFieldValueString(this.selectedOperator, null);
         }
      } else {
         this.setSearchFieldValueString(var3, var5);
      }
   }
}
