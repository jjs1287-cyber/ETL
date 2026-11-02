package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;

public class SearchFieldPostingPeriodDate extends SearchField {
   private final List<String> postingPeriodDate;

   public SearchFieldPostingPeriodDate(List<String> var1) {
      this.postingPeriodDate = var1;
   }

   @Override
   public int definedValueNumber() {
      return 1;
   }

   @Override
   public SearchFieldSaxHandler newSearchFieldValueSaxHandler() {
      return new SearchFieldPostingPeriodDateSaxHandler();
   }

   @Override
   public List<String> getPredefinedList() {
      return this.postingPeriodDate;
   }
}
