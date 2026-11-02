package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.project.copy.EntityCopyMachine;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class CreateEntityCopyAction extends AbstractSingleActiveObjectAction<IntegrationEntity> {
   public static final String ID = "action.ic.Copy";

   public CreateEntityCopyAction(ActiveObjectService var1) {
      super(var1, "action.ic.Copy", InterchangeActionDictionary.DICTIONARY, IntegrationEntity.class);
   }

   protected boolean isEnabledFor(IntegrationEntity var1) {
      return var1.isCopyable();
   }

   protected void runOnObject(final IntegrationEntity var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CreateEntityCopyAction.this.copy(var1);
         }
      });
   }

   private void copy(IntegrationEntity var1) {
      CreateEntityCopyAction.Worker var2 = new CreateEntityCopyAction.Worker(var1);
      var2.execute();
   }

   private final class Worker extends KongaSwingWorker<IntegrationEntity, Void> {
      private final IntegrationEntity original;

      public Worker(IntegrationEntity var2) {
         this.original = var2;
      }

      protected IntegrationEntity doInBackground() throws Exception {
         EntityCopyMachine var1 = new EntityCopyMachine();
         return var1.createAndInsertCopy(this.original);
      }

      protected void onSuccess(IntegrationEntity var1) {
         UiCompanion.of(var1).openPage();
      }
   }
}
