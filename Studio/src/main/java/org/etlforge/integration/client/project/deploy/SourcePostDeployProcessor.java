package org.etlforge.integration.client.project.deploy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.location.DataLocation;

public final class SourcePostDeployProcessor extends DefaultPostDeployEntityProcessor {
   @ExtensionObjectConstructor
   public SourcePostDeployProcessor(Source var1) {
      super(var1);
   }

   @Override
   protected void blankOutPasswords(IntegrationEntity var1) {
      var1.setProperty("password", null);
      DataLocation var2 = ((Source)var1).getLocation();
      PasswordEraser.eraseFromLocation(var2);
   }
}
