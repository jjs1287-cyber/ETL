package org.etlforge.integration.client.ui.connector.msgp.endpoint;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.TestConnectionAction;
import org.etlforge.integration.client.ui.connector.msgp.endpoint.page.MSGPEndpointPage;
import org.etlforge.integration.client.ui.connector.msgp.endpoint.page.MSGPEndpointPageConfig;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.data.connector.MSGPEndpoint;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

public final class MSGPEndpointConfig extends AbstractEntityConfig {
   private TestConnectionAction testConnectionAction;
   private ValueCache<MSGPEndpoint> valueCache = ValueCache.EMPTY;

   public MSGPEndpointConfig() {
      super(EntityType.MSGPEndpoint);
      this.setSearchAndReplaceMetaData(new MSGPEndpointSearchAndReplaceProperties());
   }

   public void setTestConnectionAction(TestConnectionAction var1) {
      this.testConnectionAction = var1;
   }

   public void setValueCache(ValueCache<MSGPEndpoint> var1) {
      this.valueCache = var1;
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new MSGPEndpointPageConfig(var1, var3, this.testConnectionAction);
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new MSGPEndpointPage((MSGPEndpoint)var1, this.valueCache);
   }

   @Override
   public void updateValueCache(IntegrationProject var1) {
      this.valueCache.update(var1.getAllEntitiesOfClass(MSGPEndpoint.class));
   }
}
