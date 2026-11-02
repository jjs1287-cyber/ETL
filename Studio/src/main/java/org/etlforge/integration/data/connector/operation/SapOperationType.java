package org.etlforge.integration.data.connector.operation;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.connector.SapFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.util.version.Version;

public final class SapOperationType extends FunctionCallOperationType<SapFunction, SapFunctionActivity> {
   public static final SapOperationType TYPE = new SapOperationType();

   private SapOperationType() {
      super(11, "SAP", "SAP", "SAP", SapFunction.class, SapFunctionActivityType.TYPE);
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new SapOperationTemplate().getPipeline();
   }

   @Override
   public Version getVersionIntroduced() {
      return VersionFactory.newVersion(5, 0, 0, 300);
   }

   @Override
   public String getIndefiniteArticle() {
      return "an";
   }

   @Override
   protected FunctionCallPipelineBrowserFactory<SapFunction, SapFunctionActivity> createPipelineBrowserFactory() {
      return new FunctionCallPipelineBrowserFactory<SapFunction, SapFunctionActivity>() {
         @Override
         public FunctionCallPipelineBrowser<SapFunction, SapFunctionActivity> createBrowser(OperationPipeline var1) {
            return new SapPipelineBrowser(var1);
         }
      };
   }

   @Override
   protected boolean isRecognizedFunctionCallType(EntityType var1) {
      return var1 == EntityType.SapFunction;
   }
}
