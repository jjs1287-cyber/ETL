package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;

public class SearchFieldEnumMultiSelect extends SearchField {
   @Override
   public int definedValueNumber() {
      return 3;
   }

   @Override
   public SearchFieldSaxHandler newSearchFieldValueSaxHandler() {
      return new SearchFieldEnumMultiSelectSaxHandler();
   }
}
