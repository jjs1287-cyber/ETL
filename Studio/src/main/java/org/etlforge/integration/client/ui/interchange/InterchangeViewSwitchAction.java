package org.etlforge.integration.client.ui.interchange;

import org.etlforge.application.ui.view.shell.ViewShell;
import org.etlforge.application.ui.view.shell.ViewSwitchAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.component.launcher.InterchangeLauncher;
import org.etlforge.integration.client.ui.interchange.component.launcher.ProjectLauncherViewer;

public final class InterchangeViewSwitchAction extends ViewSwitchAction {
   private final InterchangeLauncher launcher;

   public InterchangeViewSwitchAction(ApplicationWindow var1, ViewShell var2, InterchangeLauncher var3) {
      super(var1, var2);
      this.putValue("SmallIcon", ClientIcons.INTEGRATION_16);
      this.putValue("KongaMediumIconKey", ClientIcons.INTEGRATION_24);
      this.launcher = var3;
   }

   @Override
   protected void onViewLoaded(boolean var1) {
      if (var1) {
         this.openProjectSelector();
      }
   }

   private void openProjectSelector() {
      ProjectLauncherViewer var1 = new ProjectLauncherViewer(this.launcher);
      var1.open(ProjectLauncherViewer.Mode.DIALOG);
   }
}
