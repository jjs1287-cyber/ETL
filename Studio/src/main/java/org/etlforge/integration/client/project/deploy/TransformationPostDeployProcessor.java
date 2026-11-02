package org.etlforge.integration.client.project.deploy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;

public class TransformationPostDeployProcessor extends DefaultPostDeployEntityProcessor {
   @ExtensionObjectConstructor
   public TransformationPostDeployProcessor(Transformation var1) {
      super(var1);
   }

   @Override
   protected void blankOutPasswords(IntegrationEntity var1) {
      Transformation var2 = (Transformation)var1;
      DataStructure var3 = var2.getInputStructure();
      PasswordEraser.eraseFromStructure(var3);
      DataStructure var4 = var2.getOutputStructure();
      PasswordEraser.eraseFromStructure(var4);
   }
}
