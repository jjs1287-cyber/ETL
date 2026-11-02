package org.etlforge.integration.data.entity.operation.pipeline.transformation;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;

public final class TransformationPipelineBrowser extends OperationPipelineBrowser {
   public TransformationPipelineBrowser(OperationPipeline var1) {
      super(var1);
   }

   public TransformationActivity getTransformationActivity() {
      return this.getPipeline().getFirstActivityOfType(TransformationActivity.class);
   }

   public Transformation getTransformation() {
      return this.getFirstContent(TransformationActivity.class, Transformation.class);
   }

   public void setTransformation(Transformation var1) {
      TransformationActivity var2 = this.getPipeline().getFirstActivityOfType(TransformationActivity.class);
      var2.setContent(var1);
   }
}
