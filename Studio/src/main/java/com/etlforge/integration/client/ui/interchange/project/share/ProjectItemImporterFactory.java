package com.etlforge.integration.client.ui.interchange.project.share;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectManager;

public final class ProjectItemImporterFactory {
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;

   public ProjectItemImporterFactory(ApplicationWindow var1, ProjectManager var2) {
      this.appWin = var1;
      this.projectManager = var2;
   }

   public ProjectItemImporter newImporter(ManagedProject var1) {
      return new ProjectItemImporter(this.appWin, this.projectManager, var1);
   }
}
