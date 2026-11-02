package org.etlforge.integration.data.connector.operation;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.connector.MSAXFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.util.version.Version;

public final class MSAXOperationType extends FunctionCallOperationType<MSAXFunction, MSAXFunctionActivity> {
   public static final MSAXOperationType TYPE = new MSAXOperationType();

   private MSAXOperationType() {
      super(15, "MSAX", "Microsoft Dynamics AX", "Call Microsoft Dynamics AX", MSAXFunction.class, MSAXFunctionActivityType.TYPE);
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new MSAXOperationTemplate().getPipeline();
   }

   @Override
   public Version getVersionIntroduced() {
      return VersionFactory.newVersion(5, 1, 0, 3);
   }

   @Override
   public String getIndefiniteArticle() {
      return "an";
   }

   @Override
   protected FunctionCallPipelineBrowserFactory<MSAXFunction, MSAXFunctionActivity> createPipelineBrowserFactory() {
      return new FunctionCallPipelineBrowserFactory<MSAXFunction, MSAXFunctionActivity>() {
         @Override
         public FunctionCallPipelineBrowser<MSAXFunction, MSAXFunctionActivity> createBrowser(OperationPipeline var1) {
            return new MSAXPipelineBrowser(var1);
         }
      };
   }

   @Override
   protected boolean isRecognizedFunctionCallType(EntityType var1) {
      return var1 == EntityType.MSAXFunction;
   }
}
