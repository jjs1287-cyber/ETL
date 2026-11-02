package org.etlforge.integration.client.project.copy;

import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;

public final class OperationCopyInstructions extends AdditionalCopyInstructions {
   @ExtensionObjectConstructor
   public OperationCopyInstructions(Operation var1) {
      super(var1);
   }

   @Override
   public void handle(IntegrationEntity var1, ManagedProject var2) {
      EntityResources var3 = var2.getEntityResources();
      OperationGraphLayoutCopier var4 = new OperationGraphLayoutCopier(var3, (Operation)this.getOriginal(), (Operation)var1);
      var4.copy();
   }
}
