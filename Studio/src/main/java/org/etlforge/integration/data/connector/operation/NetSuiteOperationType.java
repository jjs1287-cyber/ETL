package org.etlforge.integration.data.connector.operation;

import org.etlforge.application.VersionFactory;
import org.etlforge.integration.data.connector.NetSuiteFunction;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowserFactory;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.util.version.Version;

public final class NetSuiteOperationType extends FunctionCallOperationType<NetSuiteFunction, NetSuiteFunctionActivity> {
   public static final NetSuiteOperationType TYPE = new NetSuiteOperationType();

   private NetSuiteOperationType() {
      super(18, "NETSUITE", "NetSuite", "Call NetSuite", NetSuiteFunction.class, NetSuiteFunctionActivityType.TYPE);
   }

   @Override
   public OperationPipeline createNewTemplate() {
      return new NetSuiteOperationTemplate().getPipeline();
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
   protected FunctionCallPipelineBrowserFactory<NetSuiteFunction, NetSuiteFunctionActivity> createPipelineBrowserFactory() {
      return new FunctionCallPipelineBrowserFactory<NetSuiteFunction, NetSuiteFunctionActivity>() {
         @Override
         public FunctionCallPipelineBrowser<NetSuiteFunction, NetSuiteFunctionActivity> createBrowser(OperationPipeline var1) {
            return new NetSuitePipelineBrowser(var1);
         }
      };
   }

   @Override
   protected boolean isRecognizedFunctionCallType(EntityType var1) {
      return NetSuiteFunction.allTypes().contains(var1);
   }
}
