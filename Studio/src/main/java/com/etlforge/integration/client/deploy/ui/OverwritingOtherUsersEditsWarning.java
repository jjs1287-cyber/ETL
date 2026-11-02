package com.etlforge.integration.client.deploy.ui;

import com.etlforge.integration.client.deploy.api.LastUpdateInfo;
import com.etlforge.integration.client.deploy.ui.prefs.WarnBeforeOverwritingOtherUsersEditsPreference;
import java.util.List;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.UiProvider;

public class OverwritingOtherUsersEditsWarning extends DeployWarningPrompt<LastUpdateInfo> {
   public OverwritingOtherUsersEditsWarning(IntegrationProject var1, List<LastUpdateInfo> var2) {
      super(var1, var2, "Overwrite?", "You will overwrite changes made by other users:", WarnBeforeOverwritingOtherUsersEditsPreference.INSTANCE);
   }

   @Override
   protected UiProvider createUi(IntegrationProject var1, List<LastUpdateInfo> var2) {
      return new LastUpdateInfoTable(var1, var2);
   }
}
