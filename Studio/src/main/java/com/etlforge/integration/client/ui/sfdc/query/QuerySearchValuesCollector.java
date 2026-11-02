package com.etlforge.integration.client.ui.sfdc.query;

import java.util.Collections;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceQuery;

final class QuerySearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      SalesforceQuery var2 = (SalesforceQuery)var1;
      return Collections.singletonList(var2.getQueryString());
   }
}
