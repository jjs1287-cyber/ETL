package org.etlforge.integration.client.ui.connector.netsuite.search.record.field;

import java.util.List;
import org.etlforge.integration.client.ui.connector.netsuite.search.definition.SearchField;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.SearchFieldSaxHandler;

public class SearchFieldMultiSelect extends SearchField {
   private final List<String> types;
   private final boolean isCustomField;

   public SearchFieldMultiSelect(boolean var1) {
      this.types = null;
      this.isCustomField = var1;
   }

   public SearchFieldMultiSelect(List<String> var1) {
      this.types = var1;
      this.isCustomField = false;
   }

   @Override
   public List<String> getPredefinedList() {
      return this.types;
   }

   @Override
   public int definedValueNumber() {
      return 3;
   }

   @Override
   public SearchFieldSaxHandler newSearchFieldValueSaxHandler() {
      return new SearchFieldMultiSelectSaxHandler(this.isCustomField());
   }

   public boolean isCustomField() {
      return this.isCustomField;
   }
}
