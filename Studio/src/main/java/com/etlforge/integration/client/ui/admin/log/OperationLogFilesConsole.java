package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.admin.server.GetServerFileListCallback;
import com.etlforge.integration.client.admin.server.ServerAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import org.etlforge.application.ui.job.UiJob;

public final class OperationLogFilesConsole extends AbstractLogFilesConsole {
   public OperationLogFilesConsole(ServerAdministration var1) {
      super("OperationLogFilesConsole", var1);
   }

   @Override
   protected UiJob createDownloadFileListJob(ServerAdministration var1, GetServerFileListCallback var2) {
      return new DownloadOperationLogFileListJob(var1, var2);
   }

   @Override
   public String getTitle() {
      return EnterpriseAdminResources.getString("OperationLog.Title");
   }

   @Override
   protected String getDownloadFileListToolTip() {
      return EnterpriseAdminResources.getString("OperationLog.Download.ToolTip");
   }
}
