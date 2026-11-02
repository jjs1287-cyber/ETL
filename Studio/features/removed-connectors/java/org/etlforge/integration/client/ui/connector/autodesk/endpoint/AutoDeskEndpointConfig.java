package org.etlforge.integration.client.ui.connector.autodesk.endpoint;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.EndpointSearchAndReplaceMetaData;
import org.etlforge.integration.client.ui.connector.TestConnectionAction;
import org.etlforge.integration.client.ui.connector.autodesk.endpoint.page.AutoDeskEndpointPage;
import org.etlforge.integration.client.ui.connector.autodesk.endpoint.page.AutoDeskEndpointPageConfig;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.data.connector.AutoDeskEndpoint;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

public final class AutoDeskEndpointConfig extends AbstractEntityConfig {
   private TestConnectionAction testConnectionAction;
   private ValueCache<AutoDeskEndpoint> valueCache = ValueCache.EMPTY;

   public AutoDeskEndpointConfig() {
      super(EntityType.AutoDeskEndpoint);
      this.setSearchAndReplaceMetaData(new EndpointSearchAndReplaceMetaData());
   }

   public void setTestConnectionAction(TestConnectionAction var1) {
      this.testConnectionAction = var1;
   }

   public void setValueCache(ValueCache<AutoDeskEndpoint> var1) {
      this.valueCache = var1;
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new AutoDeskEndpointPageConfig(var1, var3, this.testConnectionAction);
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new AutoDeskEndpointPage((AutoDeskEndpoint)var1, this.valueCache);
   }

   @Override
   public void updateValueCache(IntegrationProject var1) {
      this.valueCache.update(var1.getAllEntitiesOfClass(AutoDeskEndpoint.class));
   }
}
