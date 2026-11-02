package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.event.ActionEvent;
import java.util.List;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractActiveObjectAction;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;

abstract class AbstractDeleteEntityAction extends AbstractActiveObjectAction {
   private final DeleteRequestHandler requestHandler;

   public AbstractDeleteEntityAction(String var1, ActiveObjectService var2, DeleteRequestHandler var3) {
      super(var2, var1, InterchangeActionDictionary.DICTIONARY);
      this.requestHandler = var3;
   }

   @Override
   protected void updateEnabledState(List<? extends Object> var1) {
      this.setEnabled(var1 != null && !var1.isEmpty());
   }

   @Override
   public final void execute(ActionEvent var1) {
      List var2 = this.getItemsToDelete();
      if (var2 != null && !var2.isEmpty()) {
         this.deleteItems(var2);
      }
   }

   protected abstract List<IntegrationEntity> getItemsToDelete();

   private void deleteItems(final List<IntegrationEntity> var1) {
      if (DeleteConfirmer.confirm(var1)) {
         (new KongaSwingWorker<Void, Void>() {
            protected Void doInBackground() throws Exception {
               AbstractDeleteEntityAction.this.requestHandler.deleteRequested(var1);
               return null;
            }
         }).execute();
      }
   }
}
