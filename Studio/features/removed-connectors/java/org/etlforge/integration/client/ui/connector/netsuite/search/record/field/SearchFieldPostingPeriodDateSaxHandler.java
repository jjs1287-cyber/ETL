package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;

public class SearchFieldPostingPeriodDateSaxHandler extends SearchFieldSaxHandler {
   @Override
   public void endElementImpl(boolean var1, String var2, String var3, String var4, String var5) {
      this.fieldValue = new SearchFieldValueString(this.selectedOperator, var5, true);
   }
}
