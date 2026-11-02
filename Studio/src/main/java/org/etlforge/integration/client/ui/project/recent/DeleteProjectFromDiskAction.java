package org.etlforge.integration.client.ui.project.recent;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.integration.client.project.ProjectFileLock;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.RecentProjects;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class DeleteProjectFromDiskAction extends AbstractSingleObjectSelectionAction<ProjectLocation> implements RecentProjectsViewerAction {
   private RecentProjectsViewer projectViewer;

   public DeleteProjectFromDiskAction() {
      super(ProjectLocation.class, Strings.get("Recent.Delete.Name"), CommonIcons.DELETE_16);
      this.putValue("ShortDescription", Strings.get("Recent.Delete.ToolTip"));
   }

   @Override
   public void setViewer(RecentProjectsViewer var1) {
      Preconditions.checkNotNull(var1, "viewer");
      this.projectViewer = var1;
      this.setSelectionSource(var1);
      this.setEnabled(this.getSelectedObject() != null);
   }

   protected boolean isEnabledFor(ProjectLocation var1) {
      return this.projectViewer != null;
   }

   protected void run(ProjectLocation var1, ActionEvent var2) {
      if (this.isGreenLighted(var1) && var1 != null) {
         var1.deleteFromDisk();
         this.removeFromHistory(var1);
      }
   }

   private void removeFromHistory(ProjectLocation var1) {
      RecentProjects var2 = this.projectViewer.getRecentProjects();
      var2.removeProjectLocation(var1);
      this.projectViewer.update();
   }

   private boolean isProjectOpen(ProjectLocation var1) {
      ProjectFileLock var2 = new ProjectFileLock();
      return var2.isLocked(var1.getLocation());
   }

   private boolean isGreenLighted(ProjectLocation var1) {
      return this.isProjectOpen(var1) ? this.showOpenProjectMessage() : this.isWarningAccepted();
   }

   private boolean showOpenProjectMessage() {
      Alert.error(Strings.get("Recent.Delete.Open.Message"), Strings.get("Recent.Delete.Open.Title"));
      return false;
   }

   private boolean isWarningAccepted() {
      Answer var1 = Ask.yesOrNo(
         null, Strings.get("Recent.Delete.Warning.Message"), Strings.get("Recent.Delete.Warning.Title"), Alert.Option.NO, Alert.Type.WARNING
      );
      return var1.isYes();
   }
}
