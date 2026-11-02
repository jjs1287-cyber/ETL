package org.etlforge.integration.project.upgrade.v_3_0_0_21;

import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUiState;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DatabaseStructure;

final class MappingStructureCacheCleaner {
   private final ManagedProject project;

   public MappingStructureCacheCleaner(ManagedProject var1) {
      this.project = var1;
   }

   public void run() {
      this.cleanCache();
      this.resetUi();
   }

   private void cleanCache() {
      InputAndOutputMappingTreeStructureCache var1 = this.project.getTransformationMappingStructureCache();

      for (Transformation var3 : this.project.getProject().getAllEntitiesOfClass(Transformation.class)) {
         if (this.isDbTrans(var3)) {
            var1.delete(var3.getID());
         }
      }
   }

   private boolean isDbTrans(Transformation var1) {
      return var1.getInputStructure() instanceof DatabaseStructure || var1.getOutputStructure() instanceof DatabaseStructure;
   }

   private void resetUi() {
      ProjectUiState var1 = this.project.getUiState();
      var1.setEntitiesToDisplayOnStartUp(null);
      var1.setActiveEntityOnStartUp(null);
   }
}
