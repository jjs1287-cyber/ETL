package org.etlforge.integration.client.ui.interchange.entity.httpendpoint;

import java.util.Arrays;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;

final class HttpEndpointSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      HttpEndpoint var2 = (HttpEndpoint)var1;
      return Arrays.asList(var2.getUrl(), var2.getRequestVariable(), var2.getResponseVariable());
   }
}
