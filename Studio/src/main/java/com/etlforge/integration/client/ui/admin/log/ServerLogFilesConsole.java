package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import org.etlforge.application.ui.job.UiJob;

public final class ServerLogFilesConsole extends AbstractLogFilesConsole {
   public ServerLogFilesConsole(ServerAdministration var1) {
      super("ServerLogFilesConsole", var1);
   }

   @Override
   public String getTitle() {
      return EnterpriseAdminResources.getString("ServerLog.Title");
   }

   @Override
   protected UiJob createDownloadFileListJob(ServerAdministration var1, GetServerFileListCallback var2) {
      return new DownloadServerLogFileListJob(var1, var2);
   }

   @Override
   protected String getDownloadFileListToolTip() {
      return EnterpriseAdminResources.getString("ServerLog.Download.ToolTip");
   }
}
