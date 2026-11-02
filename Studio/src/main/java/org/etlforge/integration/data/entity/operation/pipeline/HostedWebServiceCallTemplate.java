package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.operation.HostedWebServiceOperationType;

public class HostedWebServiceCallTemplate extends OperationTemplate {
   public static final String WEBSERVICECALL = "Web Service Call";
   public static final String FIRST_TRANSFORMATION = "Transformation";
   public static final String SECOND_TRANSFORMATION = "Transformation";

   public HostedWebServiceCallTemplate() {
      super(HostedWebServiceOperationType.TYPE);
   }

   @Override
   protected PipelineActivity[] createChain() {
      return new PipelineActivity[]{
         new WebServiceCallActivity("Web Service Call"), new TransformationActivity("Transformation"), new TransformationActivity("Transformation")
      };
   }
}
