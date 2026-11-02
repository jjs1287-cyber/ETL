package org.etlforge.integration.client.ui.entity.wizard;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.operation.FunctionCallOperationType;
import org.etlforge.integration.data.function.Function;

public final class CreateFunctionCallOperationAction<T extends IntegrationEntity & Function> extends AbstractSingleActiveObjectAction<T> {
   private final FunctionCallOperationFactory<T> opFactory;

   public CreateFunctionCallOperationAction(ActiveObjectService var1, UiEntityFactory var2, FunctionCallOperationType<T, ?> var3, Class<T> var4) {
      super("action.CreateFunctionOperation", var1, "Create Operation...", null, var4);
      this.opFactory = new FunctionCallOperationFactory<>(var2, var3);
   }

   protected void runOnObject(final T var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CreateFunctionCallOperationAction.this.opFactory.createOperation((T)var1);
         }
      });
   }
}
