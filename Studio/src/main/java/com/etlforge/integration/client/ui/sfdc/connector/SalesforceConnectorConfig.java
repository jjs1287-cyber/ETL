package com.etlforge.integration.client.ui.sfdc.connector;

import com.etlforge.integration.client.ui.sfdc.connector.page.SalesforceConnectorPage;
import com.etlforge.integration.client.ui.sfdc.connector.page.SalesforceConnectorPageConfig;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;

public final class SalesforceConnectorConfig extends AbstractEntityConfig {
   private ApplicationAction testConnectionAction;
   private ValueCache<SalesforceConnector> valueCache = ValueCache.EMPTY;

   public SalesforceConnectorConfig() {
      super(EntityType.SalesforceConnector);
      this.setSearchValuesCollector(new ConnectorSearchValuesCollector());
      this.setSearchAndReplaceMetaData(new ConnectorSearchAndReplaceProperties());
   }

   public void setTestConnectionAction(ApplicationAction var1) {
      this.testConnectionAction = var1;
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      return new SalesforceConnectorPage((SalesforceConnector)var1);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      SalesforceConnectorPageConfig var4 = new SalesforceConnectorPageConfig(var1, var3);
      var4.setIncludeNameField(false);
      var4.setTestAction(this.testConnectionAction);
      var4.setCache(this.valueCache);
      return var4;
   }

   public void setValueCache(ValueCache<SalesforceConnector> var1) {
      this.valueCache = var1;
   }

   @Override
   public void updateValueCache(IntegrationProject var1) {
      this.valueCache.update(var1.getAllEntitiesOfClass(SalesforceConnector.class));
   }
}
