package org.etlforge.integration.client.project.event;

import java.util.EventObject;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ProjectManagerEvent extends EventObject {
   private final IntegrationProject project;

   public ProjectManagerEvent(ProjectManager var1, IntegrationProject var2) {
      super(var1);
      this.project = var2;
   }

   public ProjectManager getSource() {
      return (ProjectManager)super.getSource();
   }

   public IntegrationProject getProject() {
      return this.project;
   }

   public ManagedProject getManagedProject() {
      return this.project.getExtensionObject(ManagedProject.class);
   }
}
