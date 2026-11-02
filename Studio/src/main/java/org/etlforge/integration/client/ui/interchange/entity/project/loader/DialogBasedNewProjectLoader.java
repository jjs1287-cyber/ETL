package org.etlforge.integration.client.ui.interchange.entity.project.loader;

import org.etlforge.application.Application;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.interchange.util.dialog.NewProjectDialog;
import org.etlforge.ui.wait.WaitLock;

public final class DialogBasedNewProjectLoader implements NewProjectLoader {
   private final ProjectManager projectManager;

   public DialogBasedNewProjectLoader(ProjectManager var1) {
      this.projectManager = var1;
   }

   @Override
   public void loadNewProject(ApplicationWindow var1) {
      NewProjectDialog var2 = new NewProjectDialog(this.projectManager, false);
      this.loadNewProject(var1, var2);
   }

   private void loadNewProject(ApplicationWindow var1, NewProjectDialog var2) {
      boolean var3 = var2.open();
      if (var3) {
         WaitLock var4 = var1.startWait();
         Application.getWorker().submitForParallel(new NewProjectCreator(this.projectManager, var2.getProjectLocation(), var4));
      }
   }
}
