package org.etlforge.integration.client.ui.interchange.transformation.wizard.json;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.data.SourceTarget;

public final class JsonSourcePageChainFactory implements PageChainFactory {
   @Override
   public TransformationStructurePageChain createChain(TransformationWizard var1) {
      return new PageChainImpl(var1, SourceTarget.Source);
   }
}
