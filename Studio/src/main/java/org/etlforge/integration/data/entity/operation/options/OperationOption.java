package org.etlforge.integration.data.entity.operation.options;

import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;

public interface OperationOption {
   void applyTo(Operation var1);

   boolean isApplicableFor(OperationPipeline var1);
}
