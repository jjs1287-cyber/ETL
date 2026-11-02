package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.project.copy.DeepCopyUi;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.IntegrationProject;

public final class CreateDeepEntityCopyAction extends AbstractSingleActiveObjectAction<IntegrationEntity> {
   public static final String ID = "action.ic.DeepCopy";
   private final ApplicationWindow appWin;
   private final SaveAllGate saveGate;
   private final DeleteRequestHandler deleteHandler;

   public CreateDeepEntityCopyAction(ApplicationWindow var1, SaveAllGate var2, DeleteRequestHandler var3) {
      super(var1.getActiveObjectService(), "action.ic.DeepCopy", InterchangeActionDictionary.DICTIONARY, IntegrationEntity.class);
      this.appWin = var1;
      this.saveGate = var2;
      this.deleteHandler = var3;
   }

   protected boolean isEnabledFor(IntegrationEntity var1) {
      return var1.isDeepCopyable();
   }

   protected void runOnObject(final IntegrationEntity var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CreateDeepEntityCopyAction.this.saveGate.saveAndRun(CreateDeepEntityCopyAction.this.new Worker(var1));
         }
      });
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
         DeepCopyUi var3 = new DeepCopyUi(var1, var2);
         var3.setWaitService(CreateDeepEntityCopyAction.this.appWin);
         var3.setDeleteHandler(CreateDeepEntityCopyAction.this.deleteHandler);
         var3.copy(this.object);
      }
   }
}
