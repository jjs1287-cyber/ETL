package org.etlforge.integration.client.ui.connector.action;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.entity.wizard.FunctionCallOperationFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.function.Function;
import org.etlforge.integration.uidelegate.entity.EntityIcons;

public final class CreateOperationAction<T extends IntegrationEntity & Function> extends AbstractAction {
   private final T function;
   private final FunctionCallOperationFactory<T> opFactory;

   public CreateOperationAction(T var1, FunctionCallOperationFactory<T> var2) {
      super("Create Operation", EntityIcons.forType(EntityType.Operation).getDefaultIcon(EntityIcons.Size.SMALL));
      this.function = Preconditions.checkNotNull((T)var1, "function");
      this.opFactory = Preconditions.checkNotNull(var2, "opFactory");
      this.putValue("ShortDescription", "Create an Operation for calling this " + var1.getEntityType().getDisplayName());
      EntityIcons var3 = EntityIcons.forType(EntityType.Operation);
      var3.decorateAction(this);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CreateOperationAction.this.opFactory.createOperation(CreateOperationAction.this.function);
         }
      });
   }
}
