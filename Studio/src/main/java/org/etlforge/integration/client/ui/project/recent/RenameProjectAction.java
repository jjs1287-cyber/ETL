package org.etlforge.integration.client.ui.project.recent;

import java.awt.event.ActionEvent;
import org.etlforge.application.errorlog.ErrorLevel;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.wait.WaitService;

public final class RenameProjectAction extends AbstractSingleObjectSelectionAction<ProjectLocation> implements RecentProjectsViewerAction {
   private WaitService waitService = WaitService.NONE;
   private RecentProjectsViewer viewer;

   public RenameProjectAction() {
      super(ProjectLocation.class, Strings.get("Recent.Rename.Name"));
      this.putValue("AcceleratorKey", KeyUtils.F2);
      this.putValue("ShortDescription", Strings.get("Recent.Rename.ToolTip"));
   }

   @Override
   public void setViewer(RecentProjectsViewer var1) {
      this.viewer = var1;
      this.setSelectionSource(var1);
      this.setEnabled(this.getSelectedObject() != null);
   }

   public void setWaitService(WaitService var1) {
      this.waitService = var1;
   }

   protected void run(ProjectLocation var1, ActionEvent var2) {
      if (this.viewer != null) {
         String var3 = NewProjectNameProducer.askForNewName(var1);
         if (var3 != null) {
            this.renameProject(var1, var3);
         }
      }
   }

   private void renameProject(ProjectLocation var1, String var2) {
      RenamedProjectProducer var3 = new RenamedProjectProducer(var1, var2);
      RenameProjectAction.ResultConsumer var4 = new RenameProjectAction.ResultConsumer();
      Computation var5 = Computation.create(var3, var4);
      var5.swingWorkerBuilder().withErrorLog(ErrorLevel.ATTENTION, this.getClass()).withWaitService(this.waitService).buildAndExecute();
   }

   private class ResultConsumer implements Receiver<ProjectLocation> {
      private ResultConsumer() {
      }

      public void handle(ProjectLocation var1) {
         if (var1 != null) {
            RenameProjectAction.this.viewer.getRecentProjects().addProjectLocation(var1);
            RenameProjectAction.this.viewer.update();
         }
      }
   }
}
