package org.etlforge.integration.client.ui.interchange.entity.webservice.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.mapping.serverapi.webservice.SoapSchemaProvider;
import org.etlforge.integration.client.ui.interchange.entity.webservice.xsd.SoapSchemaController;
import org.etlforge.integration.client.ui.interchange.entity.webservice.xsd.SoapSchemaDialog;
import org.etlforge.integration.client.ui.interchange.entity.webservice.xsd.SoapSchemaModel;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class GenerateSoapSchemasAction extends WebServiceCallAction {
   public static String ID = "action.ic.GenerateSoapSchema";
   private final SoapSchemaProvider serverApi;

   public GenerateSoapSchemasAction(ApplicationWindow var1, SoapSchemaProvider var2) {
      super(var1, ID);
      this.serverApi = var2;
   }

   @Override
   protected void performActionOn(WebServiceCall var1) {
      this.openUi(var1);
   }

   private void openUi(WebServiceCall var1) {
      SoapSchemaModel var2 = new SoapSchemaModel(var1.getOperationInfo());
      SoapSchemaController var3 = new SoapSchemaController(this.serverApi, var2);
      SoapSchemaDialog var4 = new SoapSchemaDialog(var2, var3);
      var4.open(this.getWindow().getFrame());
   }
}
