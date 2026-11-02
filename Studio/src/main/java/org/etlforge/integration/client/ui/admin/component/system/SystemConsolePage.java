package org.etlforge.integration.client.ui.admin.component.system;

import java.util.List;
import org.etlforge.integration.client.ui.admin.core.console.AdminConsolePart;
import org.etlforge.integration.client.ui.admin.core.console.PartBasedConsolePage;
import org.etlforge.integration.client.ui.admin.resources.AdminIcons;
import org.etlforge.integration.client.ui.admin.resources.AdminViewResources;

public final class SystemConsolePage extends PartBasedConsolePage {
   public SystemConsolePage(List<? extends AdminConsolePart> var1) {
      super("Admin.Console.System", AdminViewResources.getString("System.Page.Title"), var1);
      this.setIcon(AdminIcons.SYSTEM_CONSOLE_16);
      this.setMementoPath(new String[]{"Admin", "SystemConsole"});
      this.setHelpId("administration.consoles.system");
      this.useDefaultCategory();
   }
}
