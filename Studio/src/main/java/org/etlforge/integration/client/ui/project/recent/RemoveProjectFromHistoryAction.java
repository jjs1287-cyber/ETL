package org.etlforge.integration.client.ui.project.recent;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.util.KeyUtils;

public final class RemoveProjectFromHistoryAction extends AbstractSingleObjectSelectionAction<ProjectLocation> implements RecentProjectsViewerAction {
   private RecentProjectsViewer viewer;

   public RemoveProjectFromHistoryAction() {
      super(ProjectLocation.class, Strings.get("Recent.Remove.Name"));
      this.putValue("AcceleratorKey", KeyUtils.DELETE);
      this.putValue("ShortDescription", Strings.get("Recent.Remove.ToolTip"));
   }

   @Override
   public void setViewer(RecentProjectsViewer var1) {
      this.viewer = Preconditions.checkNotNull(var1);
      this.setSelectionSource(var1);
      this.setEnabled(this.getSelectedObject() != null);
   }

   protected boolean isEnabledFor(ProjectLocation var1) {
      return this.viewer != null;
   }

   protected void run(final ProjectLocation var1, ActionEvent var2) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            RemoveProjectFromHistoryAction.this.remove(var1);
         }
      });
   }

   private void remove(ProjectLocation var1) {
      RecentProjects var2 = this.viewer.getRecentProjects();
      var2.removeProjectLocation(var1);
      this.viewer.update();
   }
}
