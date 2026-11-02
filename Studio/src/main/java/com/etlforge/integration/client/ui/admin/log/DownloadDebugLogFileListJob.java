package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;

public final class DownloadDebugLogFileListJob extends AbstractLogFileListDownloadJob {
   public DownloadDebugLogFileListJob(ServerAdministration var1, GetServerFileListCallback var2) {
      super(EnterpriseAdminResources.getString("DebugLog.Download.Job.Name"), var1, var2);
   }

   @Override
   protected void makeServerCall(ServerAdministration var1, GetServerFileListCallback var2) {
      var1.listDebugLogFiles(null, null, var2);
   }
}
