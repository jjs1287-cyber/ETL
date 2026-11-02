package org.etlforge.integration.client.ui.project.info;

import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.JobStatus;
import org.etlforge.application.ui.job.UiJob;
import org.etlforge.application.ui.job.UiJobMessages;
import org.etlforge.integration.client.project.info.DownloadProjectInfoCallback;
import org.etlforge.integration.client.project.info.ProjectInfoCache;
import org.etlforge.integration.client.project.info.ProjectInfoProvider;
import org.etlforge.integration.client.ui.project.info.resources.ProjectInfoUiResources;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ProjectsInfoDownloader extends UiJob {
   private final ProjectConsoleModel model;
   private final ProjectInfoProvider infoProvider;
   private final DownloadProjectInfoCallback callback;
   private volatile ProjectInfoCache cache = ProjectInfoCache.EMPTY;

   public ProjectsInfoDownloader(ProjectConsoleModel var1, ProjectInfoProvider var2) {
      super(ProjectInfoUiResources.getString("ProjectInfoDownloader.Name"));
      this.model = var1;
      this.infoProvider = var2;
      this.callback = new ProjectsInfoDownloader.CallbackImpl();
      this.setBlockUntilDone(true);
      this.setUncaughtExceptionHandler(this.callback);
   }

   public void setCache(ProjectInfoCache var1) {
      this.cache = var1;
   }

   @Override
   protected void runImpl() throws InterruptedException {
      this.setMessage(ProjectInfoUiResources.getString("ProjectInfoDownloader.Started"));
      this.model.removeProjects();
      this.model.setDownloadingInfo(true);
      this.infoProvider.downloadProjectInfo(this.callback);
   }

   @Override
   protected void onJobCancelled() {
      this.callback.cancelled();
   }

   private class CallbackImpl implements DownloadProjectInfoCallback {
      private CallbackImpl() {
      }

      @Override
      public void projectInfoDownloaded(List<IntegrationProject> var1) {
         ProjectsInfoDownloader.this.setMessage(UiJobMessages.UPDATE_UI);
         ProjectsInfoDownloader.this.cache.storeProjects(var1);
         ProjectsInfoDownloader.this.model.setDownloadingInfo(false);
         ProjectsInfoDownloader.this.model.setProjects(var1);
         ProjectsInfoDownloader.this.model.setInfoIsCached(false);
      }

      @Override
      public void cancelled() {
         ProjectsInfoDownloader.this.model.setDownloadingInfo(false);
      }

      @Override
      public void caught(Throwable var1) {
         ProjectsInfoDownloader.this.setStatus(JobStatus.ERROR);
         ProjectsInfoDownloader.this.setMessage(UiJobMessages.ERROR);
         ProjectsInfoDownloader.this.model.setDownloadingInfo(false);
         ErrorLog.attention(ProjectsInfoDownloader.class, ProjectInfoUiResources.getString("ProjectInfoDownloader.ErrorMessage"), var1);
      }
   }
}
