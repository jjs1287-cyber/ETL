package com.etlforge.integration.client.ui.sfdc.connector;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.salesforce.LoginCredentials;

final class ConnectorSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      SalesforceConnector var2 = (SalesforceConnector)var1;
      LoginCredentials var3 = var2.getLoginCredentials();
      ArrayList var4 = Lists.newArrayList();
      if (var3 != null) {
         var4.add(var3.username);
      }

      var4.add(var2.getServerHost().getUrl());
      return var4;
   }
}
