package org.etlforge.integration.client.ui.interchange.entity.webservice;

import java.util.Arrays;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.WebServiceCall;

final class WebServiceCallSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      WebServiceCall var2 = (WebServiceCall)var1;
      return Arrays.asList(
         var2.getUrl(),
         var2.getWsdlFile().getLocator(),
         var2.getProperty("soapaction"),
         var2.getProperty("service"),
         var2.getProperty("port"),
         var2.getProperty("binding"),
         var2.getProperty("operation")
      );
   }
}
