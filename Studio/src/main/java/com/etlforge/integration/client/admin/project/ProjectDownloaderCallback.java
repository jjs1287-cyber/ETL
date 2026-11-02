package com.etlforge.integration.client.admin.project;

import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.server.ServerCallback;

public interface ProjectDownloaderCallback extends ServerCallback {
   ProjectDownloadTracker getTracker();

   void downloadCompleted(ProjectLocation var1);

   abstract class Adapter implements ProjectDownloaderCallback {
      @Override
      public void downloadCompleted(ProjectLocation var1) {
      }

      @Override
      public void cancelled() {
      }

      @Override
      public void caught(Throwable var1) {
      }

      @Override
      public ProjectDownloadTracker getTracker() {
         return new ProjectDownloadTracker.Adapter();
      }
   }
}
