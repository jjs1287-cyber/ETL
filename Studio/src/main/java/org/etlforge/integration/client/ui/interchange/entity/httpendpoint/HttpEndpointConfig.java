package org.etlforge.integration.client.ui.interchange.entity.httpendpoint;

import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page.HttpEndpointPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class HttpEndpointConfig extends AbstractEntityConfig {
   public HttpEndpointConfig() {
      super(EntityType.HttpEndpoint);
      this.setSearchValuesCollector(new HttpEndpointSearchValuesCollector());
      this.setSearchAndReplaceMetaData(new HttpEndpointSearchAndReplaceProperties());
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new HttpEndpointPage((HttpEndpoint)var1);
   }
}
