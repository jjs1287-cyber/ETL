package org.etlforge.integration.client.project.copy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;

public final class TransformationCopyInstructions extends AdditionalCopyInstructions {
   @ExtensionObjectConstructor
   public TransformationCopyInstructions(Transformation var1) {
      super(var1);
   }

   @Override
   public void handle(IntegrationEntity var1, ManagedProject var2) {
      InputAndOutputMappingTreeStructureCache var3 = var2.getTransformationMappingStructureCache();
      this.copyCachedStructures(var3, var1);
   }
}
