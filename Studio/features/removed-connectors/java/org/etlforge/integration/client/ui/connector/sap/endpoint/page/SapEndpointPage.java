package org.etlforge.integration.client.ui.connector.sap.endpoint.page;

import com.google.common.base.Optional;
import javax.swing.Action;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.data.connector.SapEndpoint;

public final class SapEndpointPage extends IntegrationEntityPage {
   private Optional<Action> testConnectionAction = Optional.absent();
   private final ValueCache<SapEndpoint> valueCache;

   public SapEndpointPage(SapEndpoint var1, ValueCache<SapEndpoint> var2) {
      super(new IntegrationEntityPageModel(var1));
      this.valueCache = var2;
   }

   public void setTestConnectionAction(Action var1) {
      this.testConnectionAction = Optional.of(var1);
   }

   public SapEndpoint getObject() {
      return (SapEndpoint)super.getObject();
   }

   @Override
   protected IntegrationEntityPanel createDataPanel() {
      SapEndpointPanel var1 = new SapEndpointPanel(this.getWindow(), this.getObject(), this.valueCache);
      if (this.testConnectionAction.isPresent()) {
         var1.setTestConnectionAction(this.testConnectionAction.get());
      }

      this.getPageModel().bindProperties(var1.getBoundProperties());
      return var1;
   }
}
