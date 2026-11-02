package org.etlforge.integration.client.project.copy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class WebServiceCallCopyInstructions extends AdditionalCopyInstructions {
   @ExtensionObjectConstructor
   public WebServiceCallCopyInstructions(WebServiceCall var1) {
      super(var1);
   }

   @Override
   public void handle(IntegrationEntity var1, ManagedProject var2) {
      InputAndOutputMappingTreeStructureCache var3 = var2.getWebServiceMappingStructureCache();
      this.copyCachedStructures(var3, var1);
   }
}
