package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.function.Function;

public final class FunctionCallOperationFactory<T extends IntegrationEntity & Function> {
   private final UiEntityFactory entityFactory;
   private final FunctionCallOperationType<? super T, ?> opType;

   public FunctionCallOperationFactory(UiEntityFactory var1, FunctionCallOperationType<? super T, ?> var2) {
      this.entityFactory = Preconditions.checkNotNull(var1, "entityFactory");
      this.opType = Preconditions.checkNotNull(var2, "opType");
   }

   public void createOperation(T var1) {
      FunctionOperationDecorator var2 = new FunctionOperationDecorator<>(this.opType, (T)var1);
      this.entityFactory.createEntity(EntityType.Operation, var2);
   }
}
