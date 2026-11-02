package org.etlforge.integration.client.ui.interchange.entity.target;

import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.client.ui.interchange.location.search.LocationSearchValuesCollector;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Target;

final class TargetSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      Target var2 = (Target)var1;
      return LocationSearchValuesCollector.collect(var2);
   }
}
