package org.etlforge.integration.client.ui.entity.wizard;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.data.function.Function;

public final class CreateFunctionCallTransformationAction extends AbstractSingleActiveObjectAction<Function> {
   private static final String REQUEST_ID = "action.CreateFunctionRequest";
   private static final String RESPONSE_ID = "action.CreateFunctionResponse";
   private final FunctionCallTransformationFactory txFactory;

   public static CreateFunctionCallTransformationAction forRequest(ActiveObjectService var0, UiEntityFactory var1) {
      return new CreateFunctionCallTransformationAction(
         "action.CreateFunctionRequest", var0, "Create Request Transformation...", new FunctionCallRequestFactory(var1)
      );
   }

   public static CreateFunctionCallTransformationAction forResponse(ActiveObjectService var0, UiEntityFactory var1) {
      return new CreateFunctionCallTransformationAction(
         "action.CreateFunctionResponse", var0, "Create Response Transformation...", new FunctionCallResponseFactory(var1)
      );
   }

   public CreateFunctionCallTransformationAction(String var1, ActiveObjectService var2, String var3, FunctionCallTransformationFactory var4) {
      super(var1, var2, var3, null, Function.class);
      this.txFactory = var4;
   }

   protected boolean isEnabledFor(Function var1) {
      return this.txFactory.isEnabledFor(var1);
   }

   protected final void runOnObject(final Function var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CreateFunctionCallTransformationAction.this.txFactory.createTransformation(var1);
         }
      });
   }
}
