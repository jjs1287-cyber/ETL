package com.etlforge.integration.client.ui.interchange.project.backup.complete;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.project.loading.ProjectLoader;
import org.etlforge.system.callback.Receiver;

public final class BackupLoader implements Receiver<ProjectLocation> {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;

   public BackupLoader(ApplicationWindow var1, ProjectManager var2) {
      this.appWin = var1;
      this.projectManager = var2;
   }

   public void handle(ProjectLocation var1) {
      ProjectLoader var2 = new ProjectLoader(this.projectManager);
      var2.setWaitService(this.appWin);
      var2.loadAndDisplay(var1);
   }
}
