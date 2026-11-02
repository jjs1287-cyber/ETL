package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import org.etlforge.application.Application;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.action.AbstractActiveObjectAction;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.data.entity.IntegrationEntity;

public final class DeployActiveObjectsAction extends AbstractActiveObjectAction {
   public static final String ID = "action.ic.DeploySelectedObjects";
   private final DeployFacade deployFacade;

   public DeployActiveObjectsAction(ActiveObjectService var1, DeployFacade var2) {
      super(var1, "action.ic.DeploySelectedObjects", InterchangeActionDictionary.DICTIONARY);
      this.deployFacade = var2;
   }

   @Override
   public boolean accepts(Object var1) {
      return var1 instanceof IntegrationEntity;
   }

   @Override
   protected void updateEnabledState(List<? extends Object> var1) {
      boolean var2 = false;
      if (var1 != null) {
         for (Object var4 : var1) {
            if (((IntegrationEntity)var4).isEditable()) {
               var2 = true;
               break;
            }
         }
      }

      this.setEnabled(var2);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            DeployActiveObjectsAction.this.setEnabled(false);
            DeployActiveObjectsAction.this.startDeploy();
         }
      });
   }

   private void startDeploy() {
      final List var1 = this.getActiveObjects(IntegrationEntity.class);
      Application.getWorker().submitForParallel(new ApplicationWorker.SafeRunnable() {
         @Override
         public void run() {
            DeployActiveObjectsAction.this.deployFacade.deploy(var1);
            DeployActiveObjectsAction.this.setEnabledSafely(true);
         }
      });
   }
}
