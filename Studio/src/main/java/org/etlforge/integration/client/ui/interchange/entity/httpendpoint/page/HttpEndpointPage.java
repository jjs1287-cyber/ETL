package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.data.entity.HttpEndpoint;

public final class HttpEndpointPage extends IntegrationEntityPage {
   public HttpEndpointPage(HttpEndpoint var1) {
      super(new IntegrationEntityPageModel(var1));
      this.setHelpId("integration.httpendpoint.main");
   }

   public HttpEndpoint getObject() {
      return (HttpEndpoint)super.getObject();
   }

   protected HttpEndpointPanel createDataPanel() {
      HttpEndpointPanel var1 = new HttpEndpointPanel(this.getWindow(), this.getObject());
      this.getPageModel().bindProperties(var1.getBoundProperties());
      var1.setEntityExplorerSupport(this.getEntityExplorerSupport());
      return var1;
   }
}
