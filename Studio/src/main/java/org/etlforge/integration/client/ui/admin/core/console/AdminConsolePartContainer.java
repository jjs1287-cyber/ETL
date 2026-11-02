package org.etlforge.integration.client.ui.admin.core.console;

import javax.swing.JComponent;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.ui.UiProvider;

public interface AdminConsolePartContainer extends UiProvider {
   AdminConsolePart getActivePart();

   JComponent getToolBarContribution();

   void serverConnectionChanged(ServerConnectionEvent var1);
}
