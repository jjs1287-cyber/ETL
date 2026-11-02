package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import com.google.common.base.Optional;
import org.etlforge.application.ui.action.ApplicationAction;

public interface TestActionsFactory {
   TestActionsFactory NONE = new TestActionsFactory() {
      @Override
      public Optional<ApplicationAction> newLoadSourceAction(TransformationPage var1) {
         return Optional.absent();
      }

      @Override
      public Optional<ApplicationAction> newTestTransformationAction(TransformationPage var1) {
         return Optional.absent();
      }

      @Override
      public Optional<ApplicationAction> newTestOperationAction(TransformationPage var1) {
         return Optional.absent();
      }
   };

   Optional<ApplicationAction> newLoadSourceAction(TransformationPage var1);

   Optional<ApplicationAction> newTestTransformationAction(TransformationPage var1);

   Optional<ApplicationAction> newTestOperationAction(TransformationPage var1);
}
