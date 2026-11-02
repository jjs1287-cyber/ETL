package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.entity.operation.pipeline.FunctionCallPipelineBrowser;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.function.Function;

public final class FunctionOperationDecorator<T extends IntegrationEntity & Function> extends UiEntityFactoryCallback.Adapter {
   private final FunctionCallOperationType<? super T, ?> opType;
   private final T function;

   public FunctionOperationDecorator(FunctionCallOperationType<? super T, ?> var1, T var2) {
      this.opType = Preconditions.checkNotNull(var1, "opType");
      this.function = Preconditions.checkNotNull((T)var2, "function");
   }

   @Override
   public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
      Operation var3 = (Operation)var1;
      String var4 = EntityUtils.getAvailableChildName(var2, this.function.getName());
      var3.setName(var4);
      var3.setOperationType(this.opType);
      OperationPipeline var5 = this.opType.createNewTemplate();
      FunctionCallPipelineBrowser var6 = this.opType.createPipelineBrowser(var5);
      var6.setFunction(this.function);
      if (!this.function.hasRequestTransformation()) {
         var5.removeActivity(var6.getSourceActivity());
         var5.removeActivity(var6.getRequestActivity());
      }

      var3.setPipeline(var5);
      return Result.continueProcess(var2);
   }
}
