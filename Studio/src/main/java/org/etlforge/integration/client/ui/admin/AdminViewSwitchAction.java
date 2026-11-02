package org.etlforge.integration.client.ui.admin;

import org.etlforge.application.ui.view.shell.ViewShell;
import org.etlforge.application.ui.view.shell.ViewSwitchAction;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.icons.ClientIcons;

public final class AdminViewSwitchAction extends ViewSwitchAction {
   public AdminViewSwitchAction(ApplicationWindow var1, ViewShell var2) {
      super(var1, var2);
      this.putValue("SmallIcon", ClientIcons.ADMINISTRATION_16);
      this.putValue("KongaMediumIconKey", ClientIcons.ADMINISTRATION_24);
   }
}
