package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import org.etlforge.application.ui.job.UiJob;

public final class DebugLogFilesConsole extends AbstractLogFilesConsole {
   public DebugLogFilesConsole(ServerAdministration var1) {
      super("DebugLogFilesConsole", var1);
   }

   @Override
   public String getTitle() {
      return EnterpriseAdminResources.getString("DebugLog.Title");
   }

   @Override
   protected UiJob createDownloadFileListJob(ServerAdministration var1, GetServerFileListCallback var2) {
      return new DownloadDebugLogFileListJob(var1, var2);
   }

   @Override
   protected String getDownloadFileListToolTip() {
      return EnterpriseAdminResources.getString("DebugLog.Download.ToolTip");
   }
}
