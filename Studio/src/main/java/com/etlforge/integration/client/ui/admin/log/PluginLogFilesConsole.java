package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import org.etlforge.application.ui.job.UiJob;

public final class PluginLogFilesConsole extends AbstractLogFilesConsole {
   public PluginLogFilesConsole(ServerAdministration var1) {
      super("PluginLogFilesConsole", var1);
   }

   @Override
   public String getTitle() {
      return EnterpriseAdminResources.getString("PluginLog.Title");
   }

   @Override
   protected UiJob createDownloadFileListJob(ServerAdministration var1, GetServerFileListCallback var2) {
      return new DownloadPluginLogFileListJob(var1, var2);
   }

   @Override
   protected String getDownloadFileListToolTip() {
      return EnterpriseAdminResources.getString("PluginLog.Download.ToolTip");
   }
}
