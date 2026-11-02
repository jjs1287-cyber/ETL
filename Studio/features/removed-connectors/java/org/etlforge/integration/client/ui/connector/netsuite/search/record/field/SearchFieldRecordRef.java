package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;

public class SearchFieldRecordRef extends SearchField {
   private final List<String> types;

   public SearchFieldRecordRef(List<String> var1) {
      this.types = var1;
   }

   @Override
   public List<String> getPredefinedList() {
      return this.types;
   }

   @Override
   public int definedValueNumber() {
      return 1;
   }

   @Override
   public SearchFieldSaxHandler newSearchFieldValueSaxHandler() {
      return new SearchFieldRecordRefSaxHandler();
   }
}
