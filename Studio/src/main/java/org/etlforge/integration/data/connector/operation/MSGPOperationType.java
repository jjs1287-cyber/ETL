package org.etlforge.integration.data.connector.operation;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.connector.MSGPFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.util.version.Version;

public final class MSGPOperationType extends FunctionCallOperationType<MSGPFunction, MSGPFunctionActivity> {
   public static final MSGPOperationType TYPE = new MSGPOperationType();

   private MSGPOperationType() {
      super(13, "MSGP", "Microsoft Dynamics GP", "Call Microsoft Dynamics GP", MSGPFunction.class, MSGPFunctionActivityType.TYPE);
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new MSGPOperationTemplate().getPipeline();
   }

   @Override
   public Version getVersionIntroduced() {
      return VersionFactory.newVersion(5, 0, 0, 500);
   }

   @Override
   public String getIndefiniteArticle() {
      return "a";
   }

   @Override
   protected FunctionCallPipelineBrowserFactory<MSGPFunction, MSGPFunctionActivity> createPipelineBrowserFactory() {
      return new FunctionCallPipelineBrowserFactory<MSGPFunction, MSGPFunctionActivity>() {
         @Override
         public FunctionCallPipelineBrowser<MSGPFunction, MSGPFunctionActivity> createBrowser(OperationPipeline var1) {
            return new MSGPPipelineBrowser(var1);
         }
      };
   }

   @Override
   protected boolean isRecognizedFunctionCallType(EntityType var1) {
      return MSGPFunction.allTypes().contains(var1);
   }
}
