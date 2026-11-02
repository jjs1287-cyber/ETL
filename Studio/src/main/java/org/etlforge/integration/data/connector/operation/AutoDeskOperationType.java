package org.etlforge.integration.data.connector.operation;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.connector.AutoDeskFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.util.version.Version;

public final class AutoDeskOperationType extends FunctionCallOperationType<AutoDeskFunction, AutoDeskFunctionActivity> {
   public static final AutoDeskOperationType TYPE = new AutoDeskOperationType();

   private AutoDeskOperationType() {
      super(14, "AUTODESK", "Autodesk PLM 360", "Call Autodesk PLM 360", AutoDeskFunction.class, AutoDeskFunctionActivityType.TYPE);
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new AutoDeskOperationTemplate().getPipeline();
   }

   @Override
   public Version getVersionIntroduced() {
      return VersionFactory.newVersion(5, 1, 0, 1);
   }

   @Override
   public String getIndefiniteArticle() {
      return "a";
   }

   @Override
   protected FunctionCallPipelineBrowserFactory<AutoDeskFunction, AutoDeskFunctionActivity> createPipelineBrowserFactory() {
      return new FunctionCallPipelineBrowserFactory<AutoDeskFunction, AutoDeskFunctionActivity>() {
         @Override
         public FunctionCallPipelineBrowser<AutoDeskFunction, AutoDeskFunctionActivity> createBrowser(OperationPipeline var1) {
            return new AutoDeskPipelineBrowser(var1);
         }
      };
   }

   @Override
   protected boolean isRecognizedFunctionCallType(EntityType var1) {
      return AutoDeskFunction.allTypes().contains(var1);
   }
}
