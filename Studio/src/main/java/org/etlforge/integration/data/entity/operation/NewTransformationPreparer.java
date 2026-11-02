package org.etlforge.integration.data.entity.operation;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivity;

public interface NewTransformationPreparer {
   void prepareTransformation(Transformation var1, OperationPipeline var2, PipelineActivity var3);
}
