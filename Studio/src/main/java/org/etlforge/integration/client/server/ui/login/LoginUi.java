package org.etlforge.integration.client.server.ui.login;

import javax.swing.JComponent;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiProvider;

public interface LoginUi extends UiProvider, Disposable {
   String LAYOUT = "layout";

   void setCredentialsReceiver(Receiver<LoginCredentials> var1);

   void setValidStateMonitor(Receiver<Boolean> var1);

   @Override
   void dispose();

   boolean isInputValid();

   LoginCredentials getCredentials();

   void setCapsLockListener(JComponent var1);
}
