package org.etlforge.integration.client.ui.interchange.transformation.wizard.db;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.PageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationStructurePageChain;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.TransformationWizard;
import org.etlforge.integration.data.SourceTarget;

public final class DatabaseTargetPageChainFactory implements PageChainFactory {
   @Override
   public TransformationStructurePageChain createChain(TransformationWizard var1) {
      return new DatabasePageChain(var1, SourceTarget.Target);
   }
}
