package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;

public class EnableOperationSchedulesAction extends SetOperationSchedulesEnabledAction {
   public EnableOperationSchedulesAction(ProjectAdministration var1) {
      super(var1, EnterpriseAdminResources.getString("EnableOperationSchedules.Name"), null, true);
      this.putValue("ShortDescription", EnterpriseAdminResources.getString("EnableOperationSchedules.ToolTip"));
   }
}
