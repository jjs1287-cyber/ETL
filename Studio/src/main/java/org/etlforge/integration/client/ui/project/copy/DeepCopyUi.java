package org.etlforge.integration.client.ui.project.copy;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.Callable;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.entity.settings.EntityResources;
import org.etlforge.integration.client.project.copy.DeepCopyResult;
import org.etlforge.integration.client.project.copy.DeepEntityCopyMachine;
import org.etlforge.integration.client.project.copy.EntityCopyMachine;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.project.DeleteRequestHandler;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public final class DeepCopyUi {
   private final IntegrationProject project;
   private final EntityResources resources;
   private DeleteRequestHandler deleteHandler;
   private WaitService waitService = WaitService.NONE;
   private Set<IntegrationEntity> itemsToExclude = Sets.newHashSet();

   public DeepCopyUi(IntegrationProject var1, EntityResources var2) {
      this.project = var1;
      this.resources = var2;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void setDeleteHandler(DeleteRequestHandler var1) {
      this.deleteHandler = var1;
   }

   public void setItemsToExclude(Set<IntegrationEntity> var1) {
      this.itemsToExclude = Sets.newHashSet(var1);
   }

   public void copy(IntegrationEntity var1) {
      DeepCopyUi.CopyWorker var2 = new DeepCopyUi.CopyWorker(var1);
      DeepCopyUi.ResultDisplayer var3 = new DeepCopyUi.ResultDisplayer();
      Computation var4 = Computation.create(var2, var3);
      var4.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, this.getClass()).withWaitService(this.waitService).buildAndExecute();
   }

   private class CopyWorker implements Callable<DeepCopyResult> {
      private final IntegrationEntity item;

      public CopyWorker(IntegrationEntity var2) {
         this.item = var2;
      }

      public DeepCopyResult call() throws Exception {
         EntityCopyMachine var1 = new EntityCopyMachine();
         DeepEntityCopyMachine var2 = new DeepEntityCopyMachine(var1, DeepCopyUi.this.resources, DeepCopyUi.this.project.getItemLookup());
         var2.setItemsToExclude(DeepCopyUi.this.itemsToExclude);
         var2.setDeleteHandler(DeepCopyUi.this.deleteHandler);
         return var2.clone(this.item);
      }
   }

   private class ResultDisplayer implements Receiver<DeepCopyResult> {
      private ResultDisplayer() {
      }

      public void handle(DeepCopyResult var1) {
         IntegrationEntity var2 = var1.getStartingPoint();
         UiCompanion.of(var2).openPage();
         this.showSummary(var1);
      }

      private void showSummary(DeepCopyResult var1) {
         ResultUi var2 = new ResultUi(var1);
         var2.setWaitService(DeepCopyUi.this.waitService);
         var2.show();
      }
   }
}
