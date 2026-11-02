package org.etlforge.integration.client.ui.interchange.entity;

import com.google.common.collect.ImmutableSet;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.folder.FolderStructureDuplicator;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.wait.WaitService;

public final class DuplicateFolderStructureWorkflow {
   private final RootFolder rootToDuplicate;
   private final ProjectPersistor persistor;
   private ProjectContentViewer projectViewer;
   private WaitService waitService = WaitService.NONE;

   public DuplicateFolderStructureWorkflow(RootFolder var1, ProjectPersistor var2) {
      this.rootToDuplicate = var1;
      this.persistor = var2;
   }

   public void setProjectViewer(ProjectContentViewer var1) {
      this.projectViewer = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   public void duplicateTo(ImmutableSet<EntityType> var1) {
      IntegrationProject var2 = ProjectUtils.getProject(this.rootToDuplicate);
      DuplicateFolderStructureWorkflow.DuplicationWorker var3 = new DuplicateFolderStructureWorkflow.DuplicationWorker(var2, var1);
      DuplicateFolderStructureWorkflow.ResultConsumer var4 = new DuplicateFolderStructureWorkflow.ResultConsumer(var2, var1);
      Computation var5 = Computation.create(var3, var4);
      var5.swingWorkerBuilder().withWaitService(this.waitService).withErrorLog(ErrorLevel.ATTENTION, this.getClass()).buildAndExecute();
   }

   private class DuplicationWorker implements Callable<Integer> {
      private final IntegrationProject project;
      private final ImmutableSet<EntityType> destinationTypes;

      public DuplicationWorker(IntegrationProject var2, ImmutableSet<EntityType> var3) {
         this.project = var2;
         this.destinationTypes = var3;
      }

      public Integer call() throws Exception {
         boolean var1 = IntegrationProjectLock.acquireWithin(5L, TimeUnit.SECONDS);
         if (var1) {
            try {
               EntityType var2 = DuplicateFolderStructureWorkflow.this.rootToDuplicate.getItemType();
               FolderStructureDuplicator var3 = new FolderStructureDuplicator(this.project, var2);
               var3.duplicateTo(this.destinationTypes);
               int var4 = var3.getNumberOfNewFolders();
               var3.save(DuplicateFolderStructureWorkflow.this.persistor);
               return var4;
            } finally {
               IntegrationProjectLock.release();
            }
         } else {
            throw new Exception("The action could not be completed because a background process did not complete in time. Please try again!");
         }
      }
   }

   private class ResultConsumer implements Receiver<Integer> {
      private final IntegrationProject project;
      private final ImmutableSet<EntityType> destinationTypes;

      public ResultConsumer(IntegrationProject var2, ImmutableSet<EntityType> var3) {
         this.project = var2;
         this.destinationTypes = var3;
      }

      public void handle(Integer var1) {
         boolean var2 = var1 > 0;
         this.updateUi(var2);
      }

      private void updateUi(boolean var1) {
         if (DuplicateFolderStructureWorkflow.this.projectViewer != null) {
            for (EntityType var3 : this.destinationTypes) {
               RootFolder var4 = this.project.getRootFolder(var3);
               if (var1) {
                  DuplicateFolderStructureWorkflow.this.projectViewer.refresh(var4);
               }

               DuplicateFolderStructureWorkflow.this.projectViewer.expand(var4);
            }
         }
      }
   }
}
