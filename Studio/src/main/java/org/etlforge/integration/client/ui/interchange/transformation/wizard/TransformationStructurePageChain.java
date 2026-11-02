package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.integration.data.entity.Transformation;

public interface TransformationStructurePageChain extends WizardPageChain {
   void applyTo(Transformation var1) throws WizardPageException;

   void setOpposingStructureNature(WizardType.Nature var1);
}
