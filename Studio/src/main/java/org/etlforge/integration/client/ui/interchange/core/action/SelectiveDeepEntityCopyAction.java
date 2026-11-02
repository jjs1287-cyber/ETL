package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.project.copy.SelectiveDeepCopyUi;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.IntegrationProject;

public final class SelectiveDeepEntityCopyAction extends AbstractSingleActiveObjectAction<IntegrationEntity> {
   public static final String ID = "action.ic.SelectiveDeepCopy";
   private final ApplicationWindow appWin;
   private final DeleteRequestHandler deleteHandler;
   private final SaveAllGate saveGate;

   public SelectiveDeepEntityCopyAction(ApplicationWindow var1, DeleteRequestHandler var2, SaveAllGate var3) {
      super(var1.getActiveObjectService(), "action.ic.SelectiveDeepCopy", InterchangeActionDictionary.DICTIONARY, IntegrationEntity.class);
      this.appWin = var1;
      this.deleteHandler = var2;
      this.saveGate = var3;
   }

   protected boolean isEnabledFor(IntegrationEntity var1) {
      return var1.isDeepCopyable();
   }

   protected void runOnObject(final IntegrationEntity var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SelectiveDeepEntityCopyAction.this.copy(var1);
         }
      });
   }

   private void copy(IntegrationEntity var1) {
      this.saveGate.saveAndRun(new SelectiveDeepEntityCopyAction.Worker(var1));
   }

   private final class Worker implements Runnable {
      private final IntegrationEntity object;

      public Worker(IntegrationEntity var2) {
         this.object = var2;
      }

      @Override
      public void run() {
         IntegrationProject var1 = ProjectUtils.getProject(this.object);
         EntityResources var2 = ProjectUtils.getProjectResources(var1);
         SelectiveDeepCopyUi var3 = new SelectiveDeepCopyUi(this.object, var2);
         var3.setWaitService(SelectiveDeepEntityCopyAction.this.appWin);
         var3.setDeleteHandler(SelectiveDeepEntityCopyAction.this.deleteHandler);
         var3.start();
      }
   }
}
