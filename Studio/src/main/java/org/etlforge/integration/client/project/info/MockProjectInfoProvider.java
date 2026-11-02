package org.etlforge.integration.client.project.info;

import java.util.ArrayList;

public class MockProjectInfoProvider implements ProjectInfoProvider {
   @Override
   public void downloadProjectInfo(DownloadProjectInfoCallback var1) {
      try {
         Thread.sleep(2000L);
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         var1.caught(var3);
         return;
      }

      var1.projectInfoDownloaded(new ArrayList<>());
   }
}
