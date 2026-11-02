package org.etlforge.integration.client.project.deploy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class WebServiceCallPostDeployProcessor extends DefaultPostDeployEntityProcessor {
   @ExtensionObjectConstructor
   public WebServiceCallPostDeployProcessor(IntegrationEntity var1) {
      super(var1);
   }

   @Override
   protected void blankOutPasswords(IntegrationEntity var1) {
      WebServiceCall var2 = (WebServiceCall)var1;
      var2.setProperty("httppassword", null);
   }
}
