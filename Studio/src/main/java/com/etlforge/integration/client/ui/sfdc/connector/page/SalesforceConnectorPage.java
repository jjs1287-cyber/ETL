package com.etlforge.integration.client.ui.sfdc.connector.page;

import javax.swing.Action;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.data.entity.SalesforceConnector;

public final class SalesforceConnectorPage extends IntegrationEntityPage {
   private Action testConnection;
   private ValueCache<SalesforceConnector> cache;

   public SalesforceConnectorPage(SalesforceConnector var1) {
      super(new IntegrationEntityPageModel(var1));
   }

   public void setTestConnectionAction(Action var1) {
      this.testConnection = var1;
   }

   public void setCache(ValueCache<SalesforceConnector> var1) {
      this.cache = var1;
   }

   public SalesforceConnector getObject() {
      return (SalesforceConnector)super.getObject();
   }

   protected SalesforceConnectorPanel createDataPanel() {
      SalesforceConnectorPanel var1 = new SalesforceConnectorPanel(this.getWindow(), this.getObject());
      var1.setTestConnectionAction(this.testConnection);
      var1.setCache(this.cache);
      this.getPageModel().bindProperties(var1.getBoundPropertyControls());
      return var1;
   }
}
