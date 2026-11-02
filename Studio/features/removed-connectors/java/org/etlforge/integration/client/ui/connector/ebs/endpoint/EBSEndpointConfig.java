package org.etlforge.integration.client.ui.connector.ebs.endpoint;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.connector.TestConnectionAction;
import org.etlforge.integration.client.ui.connector.ebs.endpoint.page.EBSEndpointPage;
import org.etlforge.integration.client.ui.connector.ebs.endpoint.page.EBSEndpointPageConfig;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.data.connector.EBSEndpoint;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

public final class EBSEndpointConfig extends AbstractEntityConfig {
   private TestConnectionAction testConnectionAction;
   private ValueCache<EBSEndpoint> valueCache = ValueCache.EMPTY;

   public EBSEndpointConfig() {
      super(EntityType.EBSEndpoint);
      this.setSearchAndReplaceMetaData(new EBSEndpointSearchAndReplaceProperties());
   }

   public void setTestConnectionAction(TestConnectionAction var1) {
      this.testConnectionAction = var1;
   }

   public void setValueCache(ValueCache<EBSEndpoint> var1) {
      this.valueCache = var1;
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new EBSEndpointPageConfig(var1, var3, this.testConnectionAction);
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new EBSEndpointPage((EBSEndpoint)var1, this.valueCache);
   }

   @Override
   public void updateValueCache(IntegrationProject var1) {
      this.valueCache.update(var1.getAllEntitiesOfClass(EBSEndpoint.class));
   }
}
