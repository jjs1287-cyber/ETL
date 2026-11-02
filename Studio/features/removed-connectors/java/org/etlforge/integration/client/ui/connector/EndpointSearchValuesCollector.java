package org.etlforge.integration.client.ui.connector;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.entity.search.SearchValuesCollector;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;

public final class EndpointSearchValuesCollector implements SearchValuesCollector {
   @Override
   public List<String> getValues(IntegrationEntity var1) {
      ConnectorEndpoint var2 = (ConnectorEndpoint)var1;
      EndpointParameters var3 = var2.getConnectionParameters();
      ArrayList var4 = Lists.newArrayList(var3.getServer().getHost(), var3.getUserCredentials().getUsername());
      Properties var5 = var3.getAdditionalParams();

      for (String var7 : var5.stringPropertyNames()) {
         String var8 = var5.getProperty(var7);
         if (StringUtils.isNotEmpty(var8)) {
            var4.add(var8);
         }
      }

      return var4;
   }
}
