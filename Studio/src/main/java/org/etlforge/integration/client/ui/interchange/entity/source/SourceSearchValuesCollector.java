package org.etlforge.integration.client.ui.interchange.entity.source;

import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.client.ui.interchange.location.search.LocationSearchValuesCollector;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;

final class SourceSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      Source var2 = (Source)var1;
      return LocationSearchValuesCollector.collect(var2);
   }
}
