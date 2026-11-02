package org.etlforge.integration.client.ui.connector.ebs.endpoint.page;

import com.google.common.base.Optional;
import javax.swing.Action;
import org.etlforge.integration.client.entity.valuecache.ValueCache;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.data.connector.EBSEndpoint;

public final class EBSEndpointPage extends IntegrationEntityPage {
   private Optional<Action> testConnectionAction = Optional.absent();
   private final ValueCache<EBSEndpoint> valueCache;

   public EBSEndpointPage(EBSEndpoint var1, ValueCache<EBSEndpoint> var2) {
      super(new IntegrationEntityPageModel(var1));
      this.valueCache = var2;
   }

   public EBSEndpoint getObject() {
      return (EBSEndpoint)super.getObject();
   }

   @Override
   protected IntegrationEntityPanel createDataPanel() {
      EBSEndpointPanel var1 = new EBSEndpointPanel(this.getWindow(), this.getObject(), this.valueCache);
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
