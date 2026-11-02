package org.etlforge.integration.client.ui.interchange.entity.transformation.action;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.integration.client.ui.interchange.entity.TransformationToOperationFactory;
import org.etlforge.integration.data.entity.Transformation;

public final class CreateOperationAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.tx.CreateOperation";
   private final Transformation tx;
   private final TransformationToOperationFactory factory;

   public CreateOperationAction(Transformation var1, TransformationToOperationFactory var2) {
      super("action.ic.tx.CreateOperation", TransformationActionDictionary.DICTIONARY);
      this.tx = var1;
      this.factory = var2;
      boolean var3 = var2.isSupported(var1, Optional.absent());
      this.setEnabled(var3);
   }

   public TransformationToOperationFactory getFactory() {
      return this.factory;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CreateOperationAction.this.factory.doTransform(CreateOperationAction.this.tx);
         }
      });
   }
}
