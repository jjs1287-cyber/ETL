package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.operation.WebServiceOperationType;

public class WebServiceCallTemplate extends OperationTemplate {
   public static final String REQUEST = "Request";
   public static final String RESPONSE = "Response";
   public static final String WEBSERVICECALL = "Web Service Call";

   public WebServiceCallTemplate() {
      super(WebServiceOperationType.TYPE);
   }

   @Override
   protected PipelineActivity[] createChain() {
      return new PipelineActivity[]{
         new SourceActivity("Source"),
         new TransformationActivity("Request"),
         new WebServiceCallActivity("Web Service Call"),
         new TransformationActivity("Response"),
         new TargetActivity("Target")
      };
   }
}
