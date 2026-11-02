package com.etlforge.integration.client.ui.project.download.admin;

import com.etlforge.integration.client.admin.project.ProjectDownloadTracker;
import com.etlforge.integration.client.admin.project.ProjectDownloader;
import com.etlforge.integration.client.admin.project.ProjectDownloaderCallback;
import com.etlforge.integration.client.ui.project.download.DownloadProjectJob;
import com.etlforge.integration.client.ui.project.download.ProjectDownloadParams;
import com.etlforge.integration.client.ui.project.download.resources.ProjectDownloadResources;
import java.awt.event.ActionEvent;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.project.ProjectManager;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleView;
import org.etlforge.integration.client.ui.project.info.ProjectConsoleViewAction;
import org.etlforge.integration.client.ui.project.loading.ProjectLoader;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;

public final class DownloadProjectAction extends AbstractSingleObjectSelectionAction<IntegrationProject> implements ProjectConsoleViewAction {
   private final ProjectManager projectManager;
   private final ProjectDownloader downloader;
   private ProjectConsoleView view;

   public DownloadProjectAction(ProjectManager var1, ProjectDownloader var2) {
      super(IntegrationProject.class, ProjectDownloadResources.getString("Admin.Download.Action.Name"), ProjectDownloadResources.getIcon("Download.16"));
      this.projectManager = var1;
      this.downloader = var2;
      this.putValue("KongaMediumIconKey", ProjectDownloadResources.getIcon("Download.24"));
      this.putValue("SwingLargeIconKey", ProjectDownloadResources.getIcon("Download.32"));
      this.putValue("ShortDescription", ProjectDownloadResources.getString("Admin.Download.Action.ToolTip"));
   }

   @Override
   public void setView(ProjectConsoleView var1) {
      this.view = var1;
   }

   protected void run(IntegrationProject var1, ActionEvent var2) {
      ProjectDownloadParamsCollector var3 = new ProjectDownloadParamsCollector(this.projectManager.getProjectDirectory(), var1);
      ProjectDownloadParams var4 = this.getDownloadParameters(var3);
      if (var4 != null) {
         DownloadProjectJob var5 = new DownloadProjectJob(this.downloader, var4, new DownloadProjectAction.Callback(var3.isOpenWhenDownloadedSelected()));
         if (this.view != null) {
            var5.waitWhileRunning(this.view.getWaitService(), 0);
         }

         var5.submit();
      }
   }

   private ProjectDownloadParams getDownloadParameters(ProjectDownloadParamsCollector var1) {
      ProjectDownloadParams var2 = var1.getParams();

      while (var2 != null && !var2.isLocationOk()) {
         var2 = var1.getParams();
      }

      return var2;
   }

   private class Callback extends ProjectDownloaderCallback.Adapter {
      private final boolean openWhenDownloaded;

      public Callback(boolean var2) {
         this.openWhenDownloaded = var2;
      }

      @Override
      public void downloadCompleted(ProjectLocation var1) {
         if (this.shouldDownloadedProjectBeOpened()) {
            this.replaceCurrentProject(var1);
         }
      }

      @Override
      public ProjectDownloadTracker getTracker() {
         return new ProjectDownloadTracker.Adapter() {};
      }

      private boolean shouldDownloadedProjectBeOpened() {
         return this.openWhenDownloaded && DownloadProjectAction.this.view != null;
      }

      private void replaceCurrentProject(ProjectLocation var1) {
         ProjectLoader var2 = new ProjectLoader(DownloadProjectAction.this.projectManager);
         var2.setWaitService(DownloadProjectAction.this.view.getAppWin());
         var2.loadAndDisplay(var1);
      }
   }
}
