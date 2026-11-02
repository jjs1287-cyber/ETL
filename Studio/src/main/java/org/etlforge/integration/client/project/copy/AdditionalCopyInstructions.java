package org.etlforge.integration.client.project.copy;

import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.IntegrationEntity;

public abstract class AdditionalCopyInstructions {
   private final IntegrationEntity original;

   protected AdditionalCopyInstructions(IntegrationEntity var1) {
      this.original = var1;
   }

   public abstract void handle(IntegrationEntity var1, ManagedProject var2);

   protected final IntegrationEntity getOriginal() {
      return this.original;
   }

   protected final void copyCachedStructures(InputAndOutputMappingTreeStructureCache var1, IntegrationEntity var2) {
      try {
         var1.copy(this.original.getID(), var2.getID());
      } catch (DataCacheException var4) {
         ErrorLog.log(
            EntityCopyMachine.class,
            "Failed to create copy of cached mapping structures for the " + var2.getEntityType().getDisplayName() + " \"" + var2.getName(),
            var4
         );
      }
   }
}
