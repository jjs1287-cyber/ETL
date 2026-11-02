package com.etlforge.integration.client.sfdc.data;

import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public abstract class OperationPipelineProtector {
   public final void protectPipeline(SalesforceWizardEntity var1, IntegrationEntityLookup var2) {
      Operation var3 = var2.getEntity(var1.getImplementingOperationId(), Operation.class);
      if (var3 != null) {
         OperationPipeline var4 = var3.getPipeline();
         if (var4 != null) {
            OperationPipeline var5 = new OperationPipeline(var4);
            this.protectPipeline(var5);
            if (var5.equals(var4)) {
               var5.dispose();
            } else {
               var3.setPipeline(var5);
            }
         }
      }
   }

   protected abstract void protectPipeline(OperationPipeline var1);
}
