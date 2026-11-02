package org.etlforge.integration.data.connector.operation;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.connector.EBSFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.util.version.Version;

public final class EBSOperationType extends FunctionCallOperationType<EBSFunction, EBSFunctionActivity> {
   public static final EBSOperationType TYPE = new EBSOperationType();

   private EBSOperationType() {
      super(16, "EBS", "Oracle EBS", "Oracle EBS", EBSFunction.class, EBSFunctionActivityType.TYPE);
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new EBSOperationTemplate().getPipeline();
   }

   @Override
   public Version getVersionIntroduced() {
      return VersionFactory.newVersion(5, 2, 0, 0);
   }

   @Override
   public String getIndefiniteArticle() {
      return "an";
   }

   @Override
   protected FunctionCallPipelineBrowserFactory<EBSFunction, EBSFunctionActivity> createPipelineBrowserFactory() {
      return new FunctionCallPipelineBrowserFactory<EBSFunction, EBSFunctionActivity>() {
         @Override
         public FunctionCallPipelineBrowser<EBSFunction, EBSFunctionActivity> createBrowser(OperationPipeline var1) {
            return new EBSPipelineBrowser(var1);
         }
      };
   }

   @Override
   protected boolean isRecognizedFunctionCallType(EntityType var1) {
      return var1 == EntityType.EBSFunction;
   }
}
