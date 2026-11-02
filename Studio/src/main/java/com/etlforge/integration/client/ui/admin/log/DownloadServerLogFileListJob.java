package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;

public final class DownloadServerLogFileListJob extends AbstractLogFileListDownloadJob {
   public DownloadServerLogFileListJob(ServerAdministration var1, GetServerFileListCallback var2) {
      super(EnterpriseAdminResources.getString("ServerLog.Download.Job.Name"), var1, var2);
   }

   @Override
   protected void makeServerCall(ServerAdministration var1, GetServerFileListCallback var2) {
      var1.listServerLogFiles(var2);
   }
}
