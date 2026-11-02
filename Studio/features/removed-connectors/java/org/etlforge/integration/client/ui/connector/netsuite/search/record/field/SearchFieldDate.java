package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;

public class SearchFieldDate extends SearchField {
   private final List<String> predefinedSearchValues;

   public SearchFieldDate(List<String> var1) {
      this.predefinedSearchValues = var1;
   }

   public List<String> getPredefinedSearchValues() {
      return this.predefinedSearchValues;
   }

   @Override
   public List<String> getPredefinedList() {
      return this.predefinedSearchValues;
   }

   @Override
   public int definedValueNumber() {
      return 2;
   }

   @Override
   public SearchFieldSaxHandler newSearchFieldValueSaxHandler() {
      return new SearchFieldDateSaxHandler();
   }
}
