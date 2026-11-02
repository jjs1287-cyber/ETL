package com.etlforge.integration.client.ui.interchange.project.backup.complete;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.etlforge.integration.client.project.restore.ProjectBackupRestorer;
import java.io.File;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.integration.client.ui.project.loading.ProjectLoader;
import org.etlforge.ui.wait.WaitService;

public final class CompleteBackupRestorer {
   private final ProjectManager projectManager;
   private final AvailableBackup backup;
   private final File destination;
   private volatile Supplier<Boolean> prompt = Suppliers.ofInstance(Boolean.TRUE);
   private volatile WaitService waitService = WaitService.NONE;

   public CompleteBackupRestorer(ProjectManager var1, AvailableBackup var2, File var3) {
      this.projectManager = Preconditions.checkNotNull(var1, "projectManager");
      this.backup = Preconditions.checkNotNull(var2, "backup");
      this.destination = Preconditions.checkNotNull(var3, "destination");
   }

   public void setPrompt(Supplier<Boolean> var1) {
      this.prompt = Preconditions.checkNotNull(var1, "prompt");
   }

   public void setWaitService(WaitService var1) {
      this.waitService = Preconditions.checkNotNull(var1, "waitService");
   }

   public void restore() throws Exception {
      boolean var1 = this.prompt.get();
      if (var1) {
         if (this.closeCurrentProject()) {
            ProjectBackupRestorer var2 = new ProjectBackupRestorer(this.backup, this.destination);
            ProjectLocation var3 = var2.restore();
            this.loadProject(var3);
         }
      }
   }

   private boolean closeCurrentProject() {
      try {
         this.projectManager.closeCurrentProject();
         return true;
      } catch (CloseProjectVetoException var2) {
         return false;
      }
   }

   private void loadProject(ProjectLocation var1) {
      ProjectLocation var2 = new ProjectLocation(this.backup.getLocation().getProjectName(), this.destination);
      ProjectLoader var3 = new ProjectLoader(this.projectManager);
      var3.setWaitService(this.waitService);
      var3.loadAndDisplay(var2);
   }
}
