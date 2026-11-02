package org.etlforge.integration.client.ui.connector.netsuite.endpoint.page;

import com.google.common.base.Optional;
import javax.swing.Action;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;

public final class NetSuiteEndpointPage extends IntegrationEntityPage {
   private Optional<Action> testConnectionAction = Optional.absent();
   private final ValueCache<NetSuiteEndpoint> valueCache;

   public NetSuiteEndpointPage(NetSuiteEndpoint var1, ValueCache<NetSuiteEndpoint> var2) {
      super(new IntegrationEntityPageModel(var1));
      this.valueCache = var2;
   }

   public NetSuiteEndpoint getObject() {
      return (NetSuiteEndpoint)super.getObject();
   }

   @Override
   protected IntegrationEntityPanel createDataPanel() {
      NetSuiteEndpointPanel var1 = new NetSuiteEndpointPanel(this.getWindow(), this.getObject(), this.valueCache);
      if (this.testConnectionAction.isPresent()) {
         var1.setTestConnectionAction(this.testConnectionAction.get());
      }

      this.getPageModel().bindProperties(var1.getBoundProperties());
      return var1;
   }

   public void setTestConnectionAction(Action var1) {
      this.testConnectionAction = Optional.of(var1);
   }

   private NetSuiteEndpointPanel getNetSuiteEndpointPanel() {
      IntegrationEntityPanel var1 = this.getDetailsPanel();
      return (NetSuiteEndpointPanel)var1;
   }

   public String getNewAccount() {
      return this.getNetSuiteEndpointPanel().getNewAccount();
   }

   public String getNewWsdlDownloadUrl() {
      return this.getNetSuiteEndpointPanel().getNewWsdlDownloadUrl();
   }
}
