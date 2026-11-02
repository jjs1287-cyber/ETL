package org.etlforge.integration.client.ui.core.window.action;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractApplicationWindowAction;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginConfiguration;
import org.etlforge.integration.client.server.ui.ServerConnectionActionBinder;
import org.etlforge.integration.client.server.ui.user.profile.UserProfileDialog;
import org.etlforge.system.Disposable;

public final class UpdateUserProfileAction extends AbstractApplicationWindowAction {
   public static final String ID = "action.core.UpdateUserProfile";
   private final ServerManager serverManager;
   private final Disposable serverListener;

   public UpdateUserProfileAction(ApplicationWindow var1, ServerManager var2) {
      super(var1, "action.core.UpdateUserProfile", ClientWindowActionDictionary.DICTIONARY);
      this.serverManager = var2;
      this.serverListener = ServerConnectionActionBinder.install(var2, this);
   }

   @Override
   public void execute(ActionEvent var1) {
      new BusyWorker(this.getWindow(), new Runnable() {
         @Override
         public void run() {
            JFrame var1x = UpdateUserProfileAction.this.getWindow().getFrame();
            LoginConfiguration var2 = UpdateUserProfileAction.this.serverManager.getLoginService().getConfiguration();
            UserProfileDialog.showProfile(var1x, var2);
         }
      }).run();
   }

   @Override
   public void dispose() {
      super.dispose();
      this.serverListener.dispose();
   }
}
