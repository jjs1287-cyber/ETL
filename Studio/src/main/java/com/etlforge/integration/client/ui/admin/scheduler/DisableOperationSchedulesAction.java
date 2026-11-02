package com.etlforge.integration.client.ui.admin.scheduler;

import com.etlforge.integration.client.admin.project.ProjectAdministration;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;

public class DisableOperationSchedulesAction extends SetOperationSchedulesEnabledAction {
   public DisableOperationSchedulesAction(ProjectAdministration var1) {
      super(var1, EnterpriseAdminResources.getString("DisableOperationSchedules.Name"), null, false);
      this.putValue("ShortDescription", EnterpriseAdminResources.getString("DisableOperationSchedules.ToolTip"));
   }
}
