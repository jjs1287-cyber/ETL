package org.etlforge.integration.client.ui.connector.sap.endpoint;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.TestConnectionAction;
import org.etlforge.integration.client.ui.connector.sap.SapEndpointToolTip;
import org.etlforge.integration.client.ui.connector.sap.endpoint.page.SapEndpointPage;
import org.etlforge.integration.client.ui.connector.sap.endpoint.page.SapEndpointPageConfig;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.data.connector.SapEndpoint;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class SapEndpointConfig extends AbstractEntityConfig {
   private TestConnectionAction testConnectionAction;
   private ValueCache<SapEndpoint> valueCache = ValueCache.EMPTY;

   public SapEndpointConfig() {
      super(EntityType.SapEndpoint);
      this.setSearchAndReplaceMetaData(new SapEndpointSearchAndReplaceProperties());
   }

   public void setTestConnectionAction(TestConnectionAction var1) {
      this.testConnectionAction = var1;
   }

   public void setValueCache(ValueCache<SapEndpoint> var1) {
      this.valueCache = var1;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new SapEndpointPage((SapEndpoint)var1, this.valueCache);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new SapEndpointPageConfig(var1, var3, this.testConnectionAction);
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new SapEndpointToolTip((SapEndpoint)var1);
   }

   @Override
   public void updateValueCache(IntegrationProject var1) {
      this.valueCache.update(var1.getAllEntitiesOfClass(SapEndpoint.class));
   }
}
