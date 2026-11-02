package com.etlforge.integration.client.ui.admin.action;

import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.util.ListResourceBundle;

public final class Icons extends ListResourceBundle {
   private static final Object[][] contents = new Object[][]{
      {"admin.action.ServerLogs.Icon.Small", EnterpriseAdminResources.getIcon("ServerLogs.16")},
      {"admin.action.ServerLogs.Icon.Large", EnterpriseAdminResources.getIcon("ServerLogs.32")}
   };

   @Override
   protected Object[][] getContents() {
      return contents;
   }
}
