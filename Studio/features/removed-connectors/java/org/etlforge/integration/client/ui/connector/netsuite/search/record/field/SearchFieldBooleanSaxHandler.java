package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.StringAsValue;

public class SearchFieldBooleanSaxHandler extends SearchFieldSaxHandler {
   @Override
   public void endElementImpl(boolean var1, String var2, String var3, String var4, String var5) {
      if (!var1) {
         Boolean var6 = StringAsValue.valueAsBoolean(var5);
         if (var6 != null) {
            this.fieldValue = new SearchFieldValueBoolean(var6);
         }
      }
   }
}
