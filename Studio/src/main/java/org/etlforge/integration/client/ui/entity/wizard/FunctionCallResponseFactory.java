package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.function.Function;

public final class FunctionCallResponseFactory implements FunctionCallTransformationFactory {
   private final UiEntityFactory entityFactory;

   public FunctionCallResponseFactory(UiEntityFactory var1) {
      this.entityFactory = Preconditions.checkNotNull(var1, "entityFactory");
   }

   @Override
   public boolean isEnabledFor(Function var1) {
      return true;
   }

   @Override
   public void createTransformation(Function var1) {
      UiEntityFactoryCallback var2 = FunctionTransformationDecorator.forResponse(var1);
      this.entityFactory.createEntity(EntityType.Transformation, var2);
   }
}
