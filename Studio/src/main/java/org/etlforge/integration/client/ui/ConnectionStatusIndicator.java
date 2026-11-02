package org.etlforge.integration.client.ui;

import javax.swing.Action;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.ui.UiProvider;

public interface ConnectionStatusIndicator extends ServerConnectionListener, UiProvider {
   void setLoginAction(Action var1);
}
