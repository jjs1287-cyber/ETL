package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;

public class EnableSchedulesAction extends SetSchedulesEnabledAction {
   public EnableSchedulesAction(ProjectAdministration var1) {
      super(var1, EnterpriseAdminResources.getString("EnableSchedule.Name"), null, true);
      this.putValue("ShortDescription", EnterpriseAdminResources.getString("EnableSchedule.ToolTip"));
   }
}
