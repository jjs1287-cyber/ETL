package com.etlforge.integration.client.deploy.ui;

import com.etlforge.integration.client.deploy.api.SharedItemInfo;
import com.etlforge.integration.client.deploy.ui.prefs.WarnWhenDeployingSharedItemsPreference;
import java.util.List;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiProvider;

public class SharedItemsWarning extends DeployWarningPrompt<SharedItemInfo> {
   public SharedItemsWarning(IntegrationProject var1, List<SharedItemInfo> var2) {
      super(
         var1,
         var2,
         "Shared Items",
         "<html>Some of the deployed items are used in other projects.<br>Deploying them will affect those projects as well.</html>",
         WarnWhenDeployingSharedItemsPreference.INSTANCE
      );
   }

   @Override
   protected UiProvider createUi(IntegrationProject var1, List<SharedItemInfo> var2) {
      return new SharedItemsInfoTable(var1, var2);
   }
}
