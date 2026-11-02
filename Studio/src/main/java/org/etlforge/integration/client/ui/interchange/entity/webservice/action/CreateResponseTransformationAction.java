package org.etlforge.integration.client.ui.interchange.entity.webservice.action;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.entity.wizard.FunctionTransformationDecorator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.WebServiceCall;

public final class CreateResponseTransformationAction extends WebServiceCallAction {
   public static final String ID = "action.ic.CreateWsResponse";
   private final UiEntityFactory entityFactory;

   public CreateResponseTransformationAction(ApplicationWindow var1, UiEntityFactory var2) {
      super(var1, "action.ic.CreateWsResponse");
      this.entityFactory = var2;
   }

   @Override
   protected void performActionOn(WebServiceCall var1) {
      UiEntityFactoryCallback var2 = FunctionTransformationDecorator.forResponse(var1);
      this.entityFactory.createEntity(EntityType.Transformation, var2);
   }
}
