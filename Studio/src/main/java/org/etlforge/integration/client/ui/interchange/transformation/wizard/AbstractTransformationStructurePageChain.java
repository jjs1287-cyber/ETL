package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import org.etlforge.application.ui.wizard.WizardPage;

public abstract class AbstractTransformationStructurePageChain implements TransformationStructurePageChain {
   @Override
   public boolean canFinish(WizardPage var1) {
      return false;
   }

   @Override
   public void setOpposingStructureNature(WizardType.Nature var1) {
   }
}
