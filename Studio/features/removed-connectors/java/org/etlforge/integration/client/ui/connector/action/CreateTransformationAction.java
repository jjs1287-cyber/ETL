package org.etlforge.integration.client.ui.connector.action;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactory;
import org.etlforge.integration.client.ui.entity.wizard.FunctionCallRequestFactory;
import org.etlforge.integration.client.ui.entity.wizard.FunctionCallResponseFactory;
import org.etlforge.integration.client.ui.entity.wizard.FunctionCallTransformationFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public final class CreateTransformationAction extends AbstractAction {
   private final Function function;
   private final FunctionCallTransformationFactory txFactory;

   public static CreateTransformationAction forRequest(Function var0, UiEntityFactory var1) {
      return new CreateTransformationAction("Request", var0, new FunctionCallRequestFactory(var1));
   }

   public static CreateTransformationAction forResponse(Function var0, UiEntityFactory var1) {
      return new CreateTransformationAction("Response", var0, new FunctionCallResponseFactory(var1));
   }

   private CreateTransformationAction(String var1, Function var2, FunctionCallTransformationFactory var3) {
      super("Create " + var1);
      this.function = Preconditions.checkNotNull(var2, "function");
      this.txFactory = Preconditions.checkNotNull(var3, "txFactory");
      this.setEnabled(var3.isEnabledFor(var2));
      this.putValue("ShortDescription", this.createToolTip(var1));
      EntityIcons var4 = EntityIcons.forType(EntityType.Transformation);
      var4.decorateAction(this);
   }

   private String createToolTip(String var1) {
      return this.isEnabled()
         ? "Create a " + var1.toLowerCase() + " Transformation for this " + this.function.getEntityType().getDisplayName()
         : this.function.getEntityType().getPlural() + " do not use request Transformations";
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (CreateTransformationAction.this.isEnabled()) {
               CreateTransformationAction.this.txFactory.createTransformation(CreateTransformationAction.this.function);
            }
         }
      });
   }
}
