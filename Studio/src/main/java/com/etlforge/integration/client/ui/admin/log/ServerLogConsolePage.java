package com.etlforge.integration.client.ui.admin.log;

import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.util.List;
import org.etlforge.integration.client.ui.admin.core.console.AdminConsolePart;
import org.etlforge.integration.client.ui.admin.core.console.PartBasedConsolePage;

public final class ServerLogConsolePage extends PartBasedConsolePage {
   public ServerLogConsolePage(List<? extends AdminConsolePart> var1) {
      super("ServerLogConsole", EnterpriseAdminResources.getString("Log.Page.Title"), var1);
      this.setIcon(EnterpriseAdminResources.getIcon("ServerLogs.16"));
      this.setMementoPath(new String[]{"Admin", "ServerLogConsole"});
   }
}
