package org.etlforge.integration.client.ui.interchange.transformation.wizard.edi;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.xml.XmlPageChain;
import org.etlforge.integration.data.SourceTarget;

public final class EdiTargetPageChainFactory implements PageChainFactory {
   @Override
   public TransformationStructurePageChain createChain(TransformationWizard var1) {
      return XmlPageChain.createForEdi(var1, SourceTarget.Target);
   }
}
