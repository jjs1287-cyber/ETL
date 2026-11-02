package org.etlforge.integration.client.project.deploy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;

public final class TargetPostDeployProcessor extends DefaultPostDeployEntityProcessor {
   @ExtensionObjectConstructor
   public TargetPostDeployProcessor(Target var1) {
      super(var1);
   }

   @Override
   protected void blankOutPasswords(IntegrationEntity var1) {
      var1.setProperty("password", null);
      DataLocation var2 = ((Target)var1).getLocation();
      PasswordEraser.eraseFromLocation(var2);
   }
}
