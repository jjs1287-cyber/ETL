package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.base.Optional;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.LoadSourceDataAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.TestOperationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.action.TestTransformationAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.data.OperationTesterFactory;

public final class DefaultTestActionsFactory implements TestActionsFactory {
   private final OperationTesterFactory opTesterFactory;

   public DefaultTestActionsFactory(OperationTesterFactory var1) {
      this.opTesterFactory = var1;
   }

   @Override
   public Optional<ApplicationAction> newLoadSourceAction(TransformationPage var1) {
      LoadSourceDataAction var2 = new LoadSourceDataAction(var1);
      return Optional.of(var2);
   }

   @Override
   public Optional<ApplicationAction> newTestTransformationAction(TransformationPage var1) {
      TestTransformationAction var2 = new TestTransformationAction(var1);
      return Optional.of(var2);
   }

   @Override
   public Optional<ApplicationAction> newTestOperationAction(TransformationPage var1) {
      TestOperationAction var2 = new TestOperationAction(var1, this.opTesterFactory);
      return Optional.of(var2);
   }
}
