package org.etlforge.integration.client.ui.admin.component.scheduler;

import java.util.List;
import org.etlforge.integration.client.ui.admin.core.console.AdminConsolePart;
import org.etlforge.integration.client.ui.admin.core.console.PartBasedConsolePage;
import org.etlforge.integration.client.ui.admin.resources.AdminIcons;

public final class SchedulerConsolePage extends PartBasedConsolePage {
   public SchedulerConsolePage(List<? extends AdminConsolePart> var1) {
      super("Admin.Console.Scheduler", Strings.get("Scheduler.Page.Title"), var1);
      this.setHelpId("administration.consoles.scheduler");
      this.setMementoPath(new String[]{"Admin", "ScheduleConsole"});
      this.setIcon(AdminIcons.SCHEDULE_CONSOLE_16);
      this.useDefaultCategory();
   }
}
