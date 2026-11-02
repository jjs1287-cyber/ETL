package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.interchange.entity.project.loader.DialogBasedNewProjectLoader;

public final class NewProjectAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.NewProject";
   private final ApplicationWindow appWin;
   private final ProjectManager projectManager;

   public NewProjectAction(ApplicationWindow var1, ProjectManager var2) {
      super("action.ic.NewProject", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.projectManager = var2;
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            NewProjectAction.this.openDialog();
         }
      });
   }

   private void openDialog() {
      DialogBasedNewProjectLoader var1 = new DialogBasedNewProjectLoader(this.projectManager);
      var1.loadNewProject(this.appWin);
   }
}
