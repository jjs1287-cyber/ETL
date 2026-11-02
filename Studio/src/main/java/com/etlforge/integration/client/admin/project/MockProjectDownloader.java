package com.etlforge.integration.client.admin.project;

import java.io.File;
import org.etlforge.integration.data.entity.id.IntegrationProjectId;
import org.etlforge.lang.exception.NotImplementedYetException;

public class MockProjectDownloader implements ProjectDownloader {
   @Override
   public void downloadProject(IntegrationProjectId var1, File var2, ProjectDownloaderCallback var3) {
      try {
         Thread.sleep(3000L);
         var3.caught(new NotImplementedYetException("This feature is not yet implemented in the backend."));
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         var3.caught(var5);
      }
   }
}
