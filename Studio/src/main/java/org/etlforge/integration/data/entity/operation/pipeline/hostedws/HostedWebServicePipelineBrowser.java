package org.etlforge.integration.data.entity.operation.pipeline.hostedws;

import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.TransformationActivity;
import org.etlforge.integration.data.entity.operation.pipeline.WebServiceCallActivity;

public final class HostedWebServicePipelineBrowser extends OperationPipelineBrowser {
   public HostedWebServicePipelineBrowser(OperationPipeline var1) {
      super(var1);
   }

   public WebServiceCall getWebServiceCall() {
      return this.getFirstContent(WebServiceCallActivity.class, WebServiceCall.class);
   }

   public void setWebServiceCall(WebServiceCall var1) {
      this.getWebServiceCallActivity().setContent(var1);
   }

   public WebServiceCallActivity getWebServiceCallActivity() {
      return this.getPipeline().getFirstActivityOfType(WebServiceCallActivity.class);
   }

   public Transformation getFirstTransformation() {
      return this.getFirstContent(TransformationActivity.class, Transformation.class);
   }

   public TransformationActivity getFirstTransformationActivity() {
      return this.getPipeline().getFirstActivityOfType(TransformationActivity.class);
   }

   public Transformation getSecondTransformation() {
      TransformationActivity var1 = this.getFirstTransformationActivity();
      return this.getNextContent(var1, TransformationActivity.class, Transformation.class);
   }

   public TransformationActivity getSecondTransformationActivity() {
      TransformationActivity var1 = this.getFirstTransformationActivity();
      return this.getNextActivity(var1, TransformationActivity.class);
   }
}
