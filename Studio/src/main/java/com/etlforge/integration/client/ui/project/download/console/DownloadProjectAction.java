package com.etlforge.integration.client.ui.project.download.console;

import com.etlforge.integration.client.ui.project.download.ProjectDownloadParams;
import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import java.awt.event.ActionEvent;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

final class DownloadProjectAction extends AbstractSingleObjectSelectionAction<ProjectDownloadParams> {
   private DownloadProjectController controller;
   private boolean available = true;

   public DownloadProjectAction() {
      super(ProjectDownloadParams.class, ProjectDownloadResources.getString("Console.Download.Action.Name"), ProjectDownloadResources.getIcon("Download.16"));
      this.putValue("ShortDescription", "Console.Download.Action.ToolTip");
      this.putValue("KongaMediumIconKey", ProjectDownloadResources.getIcon("Download.24"));
      this.putValue("SwingLargeIconKey", ProjectDownloadResources.getIcon("Download.32"));
   }

   public void setController(DownloadProjectController var1) {
      this.controller = var1;
      this.handleSelectionChange();
   }

   public void setDownloadAvailable(boolean var1) {
      this.available = var1;
      this.handleSelectionChange();
   }

   protected boolean isEnabledFor(ProjectDownloadParams var1) {
      return this.available && this.controller != null;
   }

   protected void run(ProjectDownloadParams var1, ActionEvent var2) {
      this.controller.downloadProject(var1);
   }
}
