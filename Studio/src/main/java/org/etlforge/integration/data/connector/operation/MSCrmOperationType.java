package org.etlforge.integration.data.connector.operation;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.connector.MSCrmFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.util.version.Version;

public final class MSCrmOperationType extends FunctionCallOperationType<MSCrmFunction, MSCrmFunctionActivity> {
   public static final MSCrmOperationType TYPE = new MSCrmOperationType();

   private MSCrmOperationType() {
      super(12, "MSCRM", "Microsoft Dynamics CRM", "Call Microsoft Dynamics CRM", MSCrmFunction.class, MSCrmFunctionActivityType.TYPE);
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new MSCrmOperationTemplate().getPipeline();
   }

   @Override
   public Version getVersionIntroduced() {
      return VersionFactory.newVersion(5, 0, 0, 305);
   }

   @Override
   public String getIndefiniteArticle() {
      return "a";
   }

   @Override
   protected FunctionCallPipelineBrowserFactory<MSCrmFunction, MSCrmFunctionActivity> createPipelineBrowserFactory() {
      return new FunctionCallPipelineBrowserFactory<MSCrmFunction, MSCrmFunctionActivity>() {
         @Override
         public FunctionCallPipelineBrowser<MSCrmFunction, MSCrmFunctionActivity> createBrowser(OperationPipeline var1) {
            return new MSCrmPipelineBrowser(var1);
         }
      };
   }

   @Override
   protected boolean isRecognizedFunctionCallType(EntityType var1) {
      return MSCrmFunction.allTypes().contains(var1);
   }
}
