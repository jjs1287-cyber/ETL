package org.etlforge.integration.data.entity.operation.pipeline;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.integration.data.function.Function;

public final class FunctionCallRemovingPolicy implements ActivityRemovingPolicy {
   private final OperationType operationType;
   private final FunctionCallPipelineBrowser<?, ?> browser;

   public FunctionCallRemovingPolicy(OperationType var1, FunctionCallPipelineBrowser<?, ?> var2) {
      this.operationType = var1;
      this.browser = var2;
   }

   @Override
   public void checkRemovalAllowed(PipelineActivity var1) throws RemoveActivityVetoException {
      if (var1 == this.browser.getFunctionActivity()) {
         this.veto("function call activity");
      }

      if (var1 == this.browser.getRequestActivity()) {
         Function var2 = (Function)this.browser.getFunction();
         if (var2 == null || var2.hasRequestTransformation()) {
            this.veto("request " + EntityType.Transformation.getDisplayName());
         }
      }

      if (var1 == this.browser.getResponseActivity()) {
         this.veto("response " + EntityType.Transformation.getDisplayName());
      }
   }

   private void veto(String var1) throws RemoveActivityVetoException {
      throw new RemoveActivityVetoException(
         String.format("The %s cannot be removed from %s %s operation.", var1, this.operationType.getIndefiniteArticle(), this.operationType)
      );
   }
}
