package org.etlforge.integration.client.ui.connector.msax.endpoint.page;

import com.google.common.base.Optional;
import javax.swing.Action;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.data.connector.MSAXEndpoint;

public final class MSAXEndpointPage extends IntegrationEntityPage {
   private Optional<Action> testConnectionAction = Optional.absent();
   private final ValueCache<MSAXEndpoint> valueCache;

   public MSAXEndpointPage(MSAXEndpoint var1, ValueCache<MSAXEndpoint> var2) {
      super(new IntegrationEntityPageModel(var1));
      this.valueCache = var2;
   }

   public MSAXEndpoint getObject() {
      return (MSAXEndpoint)super.getObject();
   }

   @Override
   protected IntegrationEntityPanel createDataPanel() {
      MSAXEndpointPanel var1 = new MSAXEndpointPanel(this.getWindow(), this.getObject(), this.valueCache);
      if (this.testConnectionAction.isPresent()) {
         var1.setTestConnectionAction(this.testConnectionAction.get());
      }

      this.getPageModel().bindProperties(var1.getBoundProperties());
      return var1;
   }

   public void setTestConnectionAction(Action var1) {
      this.testConnectionAction = Optional.of(var1);
   }
}
