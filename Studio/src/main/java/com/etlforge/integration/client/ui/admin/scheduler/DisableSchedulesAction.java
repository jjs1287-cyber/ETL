package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;

public class DisableSchedulesAction extends SetSchedulesEnabledAction {
   public DisableSchedulesAction(ProjectAdministration var1) {
      super(var1, EnterpriseAdminResources.getString("DisableSchedule.Name"), null, false);
      this.putValue("ShortDescription", EnterpriseAdminResources.getString("DisableSchedule.ToolTip"));
   }
}
