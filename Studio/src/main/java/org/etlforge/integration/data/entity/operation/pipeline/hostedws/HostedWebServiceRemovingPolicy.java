package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import org.etlforge.integration.data.entity.operation.pipeline.ActivityRemovingPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.integration.data.entity.operation.pipeline.RemoveActivityVetoException;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;

public final class HostedWebServiceRemovingPolicy implements ActivityRemovingPolicy {
   private final OperationPipeline pipeline;

   public HostedWebServiceRemovingPolicy(OperationPipeline var1) {
      this.pipeline = var1;
   }

   @Override
   public void checkRemovalAllowed(PipelineActivity var1) throws RemoveActivityVetoException {
      if (var1 instanceof WebServiceCallActivity) {
         throw new RemoveActivityVetoException("The Web Service Call cannot be removed from a hosted web service operation");
      }

      if (var1 instanceof TransformationActivity) {
         PipelineActivity var2 = this.pipeline.getPreviousActivity(var1);
         if (var2.getType() == PipelineActivityType.WEBSERVICECALL) {
            throw new RemoveActivityVetoException("The first Transformation cannot be removed");
         }
      }
   }
}
