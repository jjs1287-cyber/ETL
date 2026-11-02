package org.etlforge.integration.client.project.deploy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.data.entity.BuiltInIntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntity;

public class DefaultPostDeployEntityProcessor implements PostDeployEntityProcessor {
   private final IntegrationEntity entity;

   @ExtensionObjectConstructor
   public DefaultPostDeployEntityProcessor(IntegrationEntity var1) {
      this.entity = var1;
   }

   @Override
   public final void doPostDeployProcessing(boolean var1) {
      if (!(this.entity instanceof BuiltInIntegrationEntity)) {
         if (var1) {
            this.blankOutPasswords(this.entity);
         }

         this.entity.setDeployed(true);
         this.entity.setDeployDirty(false);
      }
   }

   protected void blankOutPasswords(IntegrationEntity var1) {
   }
}
