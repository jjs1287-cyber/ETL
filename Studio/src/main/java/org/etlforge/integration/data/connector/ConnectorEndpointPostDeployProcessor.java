package org.etlforge.integration.data.connector;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.deploy.DefaultPostDeployEntityProcessor;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class ConnectorEndpointPostDeployProcessor extends DefaultPostDeployEntityProcessor {
   @ExtensionObjectConstructor
   public ConnectorEndpointPostDeployProcessor(ConnectorEndpoint var1) {
      super(var1);
   }

   @Override
   protected void blankOutPasswords(IntegrationEntity var1) {
      var1.setProperty("password", null);
   }
}
