package org.etlforge.integration.client.ui.interchange.entity.project.page;

import javax.swing.Action;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.client.ui.project.ProjectRenamer;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ProjectPage extends IntegrationEntityPage {
   private ProjectRenamer renamer;
   private Action[] projectActions;

   public ProjectPage(IntegrationProject var1) {
      super(new IntegrationEntityPageModel(var1));
   }

   protected ProjectPanel createDataPanel() {
      ProjectPanel var1 = new ProjectPanel(this.getWindow(), this.getObject());
      this.getPageModel().bindProperties(var1.getBoundPropertyControls());
      if (this.renamer != null) {
         var1.setRenamer(this.renamer);
      }

      if (this.projectActions != null) {
         var1.setProjectActions(this.projectActions);
      }

      return var1;
   }

   public void setProjectRenamer(ProjectRenamer var1) {
      this.renamer = var1;
   }

   public void setProjectActions(Action... var1) {
      this.projectActions = (Action[])var1.clone();
   }

   public IntegrationProject getObject() {
      return (IntegrationProject)super.getObject();
   }
}
