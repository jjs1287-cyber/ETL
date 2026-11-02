package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.FunctionCallStructure;

public final class FunctionCallAutoCompleter<T extends IntegrationEntity & Function> extends PipelineAutoCompleter {
   private final Class<T> functionClass;
   private final FunctionCallPipelineBrowserFactory<T, ?> browserFactory;

   public FunctionCallAutoCompleter(Class<T> var1, FunctionCallPipelineBrowserFactory<T, ?> var2, IntegrationEntityLookup var3) {
      super(var3);
      this.browserFactory = var2;
      this.functionClass = var1;
   }

   @Override
   public final void complete(OperationPipeline var1, PipelineActivity var2) {
      Object var3 = var2.getContent();
      if (var3 instanceof Transformation) {
         this.completeTransformation(var1, var2, (Transformation)var3);
      } else if (var3 instanceof Function) {
         this.completeFunction(var1, (Function)var3);
      }
   }

   private void completeTransformation(OperationPipeline var1, PipelineActivity var2, Transformation var3) {
      FunctionCallPipelineBrowser var4 = this.browserFactory.createBrowser(var1);
      this.checkFunctionCall(var3, var2, var4);
      if (var2 == var4.getRequestActivity()) {
         this.checkSource(var4, var3, "Source");
      }

      if (var4.getTarget() == null && var2 == var4.getResponseActivity()) {
         this.checkTarget(var3, var4);
      }
   }

   private void checkFunctionCall(Transformation var1, PipelineActivity var2, FunctionCallPipelineBrowser<T, ?> var3) {
      IntegrationEntityId var4 = null;
      DataStructure var5 = null;
      if (var2 == var3.getRequestActivity()) {
         var5 = var1.getOutputStructure();
      } else if (var2 == var3.getResponseActivity()) {
         var5 = var1.getInputStructure();
      }

      if (var5 instanceof FunctionCallStructure) {
         var4 = ((FunctionCallStructure)var5).getFunctionCallId();
         if (var4 != null) {
            var3.setFunction(this.lookup(var4, this.functionClass));
         }
      }
   }

   private void checkTarget(Transformation var1, OperationPipelineBrowser var2) {
      TargetId var3 = var1.getTargetId();
      if (var3 != null) {
         Target var4 = this.lookupTarget(var3);
         var2.setTarget(var4);
      }
   }

   private void completeFunction(OperationPipeline var1, Function var2) {
      if (!var2.hasRequestTransformation()) {
         FunctionCallPipelineBrowser var3 = this.browserFactory.createBrowser(var1);
         TransformationActivity var4 = var3.getRequestActivity();
         if (var4 != null && var4.isEmpty()) {
            SourceActivity var5 = var3.getSourceActivity();
            var1.removeActivity(var5);
            var1.removeActivity(var4);
         }
      }
   }
}
