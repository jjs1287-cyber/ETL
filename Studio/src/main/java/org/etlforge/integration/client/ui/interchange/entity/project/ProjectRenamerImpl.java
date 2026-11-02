package org.etlforge.integration.client.ui.interchange.entity.project;

import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.Application;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.application.worker.ApplicationWorker;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.ui.interchange.InterchangeUiState;
import org.etlforge.integration.client.ui.interchange.core.action.SaveAllGate;
import org.etlforge.integration.client.ui.interchange.resources.InterchangeResources;
import org.etlforge.integration.client.ui.project.ProjectNameInput;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.IntegrationProjectLock;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;

public final class ProjectRenamerImpl implements ProjectRenamer {
   private final SaveAllGate saveGate;
   private volatile WaitService waitService = WaitService.NONE;
   private volatile WaitLock waitLock;

   public ProjectRenamerImpl(SaveAllGate var1) {
      this.saveGate = var1;
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   @Override
   public void renameProject(IntegrationProject var1) {
      ProjectRenamerImpl.NewNameProcessor var2 = new ProjectRenamerImpl.NewNameProcessor(var1);
      this.saveGate.saveAndRun(new ProjectRenamerImpl.NameCollector(var1, var2));
   }

   private class NameCollector implements Runnable {
      private final IntegrationProject project;
      private final Receiver<String> callback;

      public NameCollector(IntegrationProject var2, Receiver<String> var3) {
         this.project = var2;
         this.callback = var3;
      }

      @Override
      public void run() {
         ProjectRenamerImpl.this.waitLock = ProjectRenamerImpl.this.waitService.startWait();

         try {
            ProjectNameInput var1 = new ProjectNameInput(
               InterchangeResources.getString("Project.Rename.NewName.Title"), InterchangeResources.getString("Project.Rename.NewName.Label")
            );
            var1.setInitialValue(this.project.getName());
            String var2 = var1.showInDialog(ApplicationUi.getAppWinFrame());
            this.callback.handle(var2);
         } catch (Exception var3) {
            ProjectRenamerImpl.this.waitLock.release();
            throw Throwables.propagate(var3);
         }
      }
   }

   private class NewNameProcessor implements Receiver<String> {
      private final IntegrationProject project;

      public NewNameProcessor(IntegrationProject var2) {
         this.project = var2;
      }

      public void handle(String var1) {
         if (this.isNewName(var1)) {
            ProjectRenamerImpl.RenameTask var2 = ProjectRenamerImpl.this.new RenameTask(this.project, var1, ProjectRenamerImpl.this.waitLock);
            Application.getWorker().submitForParallel(var2);
         } else {
            if (var1 != null && this.project.getName().equals(var1)) {
               NonBlockingAlert.info(
                  InterchangeResources.getString("Project.Rename.NoChange.Message"), InterchangeResources.getString("Project.Rename.NoChange.Title")
               );
            }

            ProjectRenamerImpl.this.waitLock.release();
         }
      }

      private boolean isNewName(String var1) {
         if (var1 == null) {
            return false;
         }

         boolean var2 = !var1.equals(this.project.getName());
         if (var2 && SystemUtils.IS_OS_WINDOWS) {
            boolean var3 = var1.equalsIgnoreCase(this.project.getName());
            if (var3) {
               Alert.info(InterchangeResources.getString("Project.Rename.CaseOnly.Message"), InterchangeResources.getString("Project.Rename.CaseOnly.Title"));
               return false;
            }
         }

         return var2;
      }
   }

   private class RenameTask implements ApplicationWorker.SafeRunnable {
      private final IntegrationProject project;
      private final String newName;
      private final WaitLock waitLock;

      public RenameTask(IntegrationProject var2, String var3, WaitLock var4) {
         this.project = var2;
         this.newName = var3;
         this.waitLock = var4;
      }

      @Override
      public void run() {
         IntegrationProjectLock.acquire();

         try {
            ManagedProject var1 = this.project.getExtensionObject(ManagedProject.class);
            this.saveUiState(var1);
            var1.renameProject(this.newName);
         } finally {
            IntegrationProjectLock.release();
            this.waitLock.release();
         }
      }

      private void saveUiState(final ManagedProject var1) {
         UiThreadSupplier.call(new Supplier<Void>() {
            public Void get() {
               InterchangeUiState.resetState(var1);
               return null;
            }
         });
      }
   }
}
