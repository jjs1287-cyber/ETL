package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.event.ActionEvent;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.integration.client.project.CloseProjectVetoException;
import org.etlforge.integration.client.project.CurrentProjectService;
import org.etlforge.integration.client.ui.interchange.component.launcher.InterchangeLauncher;
import org.etlforge.integration.client.ui.interchange.component.launcher.ProjectLauncherViewer;

public final class CloseProjectAction extends AbstractProjectBasedAction {
   public static final String ID = "action.ic.CloseProject";
   private final InterchangeLauncher launcher;

   public CloseProjectAction(InterchangeLauncher var1, CurrentProjectService var2) {
      super(var2, "action.ic.CloseProject", InterchangeActionDictionary.DICTIONARY);
      this.launcher = var1;
   }

   @Override
   public void execute(ActionEvent var1) {
      (new BusyWorker(this.launcher.getWindow()) {
         @Override
         protected void doWork() {
            CloseProjectAction.this.executeImpl();
         }
      }).run();
   }

   private void executeImpl() {
      try {
         this.closeProject();
         this.openLauncher();
      } catch (CloseProjectVetoException var2) {
      }
   }

   private void closeProject() throws CloseProjectVetoException {
      CurrentProjectService var1 = this.getCurrentProjectService();
      var1.closeProject();
   }

   private void openLauncher() {
      ProjectLauncherViewer var1 = new ProjectLauncherViewer(this.launcher);
      var1.open();
   }
}
