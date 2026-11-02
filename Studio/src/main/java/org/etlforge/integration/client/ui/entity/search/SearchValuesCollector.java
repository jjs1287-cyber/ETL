package org.etlforge.integration.client.ui.entity.search;

import java.util.Collections;
import java.util.List;
import org.etlforge.integration.data.entity.IntegrationEntity;

public interface SearchValuesCollector {
   SearchValuesCollector EMPTY = new SearchValuesCollector() {
      @Override
      public List<String> getValues(IntegrationEntity var1) {
         return Collections.emptyList();
      }
   };

   List<String> getValues(IntegrationEntity var1);
}
