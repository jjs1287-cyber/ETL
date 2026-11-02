package org.etlforge.integration.client.ui.core.window.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.Icon;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionDictionary;
import org.etlforge.application.ui.window.toolbar.ToolbarMode;
import org.etlforge.integration.client.server.ConnectionStatus;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.InvalidCredentialsException;
import org.etlforge.integration.client.server.login.LoginCallback;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.server.ui.login.LoginServiceUi;
import org.etlforge.integration.user.User;

public class LoginAction extends AbstractApplicationAction implements ServerConnectionListener {
   public static final String ID = "action.core.Login";
   private final ServerManager serverManager;

   public LoginAction(ServerManager var1) {
      this(var1, "action.core.Login");
   }

   protected LoginAction(ServerManager var1, String var2) {
      super(var2, ClientWindowActionDictionary.DICTIONARY);
      this.serverManager = var1;
      this.init(ClientWindowActionDictionary.DICTIONARY);
   }

   private void init(ActionDictionary var1) {
      this.serverManager.addServerConnectionListener(this);
      this.setIcon(this.serverManager.getConnectionStatus(), this.serverManager.getCurrentLoginCredentials(), var1);
   }

   private void setIcon(ConnectionStatus var1, LoginCredentials var2, ActionDictionary var3) {
      String var4 = this.getID();
      if (var1 == ConnectionStatus.CONNECTED) {
         var4 = var4 + "On";
         if (var2 != null && var2.isUsingHttps()) {
            var4 = var4 + ".Https";
         }
      }

      this.putValue("SmallIcon", var3.getSmallIcon(var4));
      Icon var5 = ToolbarMode.getConfiguredMode() == ToolbarMode.MINIMAL ? var3.getLargeIcon(var4) : var3.getMediumIcon(var4);
      this.putValue("SwingLargeIconKey", var5);
   }

   @Override
   public final void serverConnectionChanged(final ServerConnectionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LoginAction.this.setIcon(var1.getStatus(), var1.getCredentials(), ClientWindowActionDictionary.DICTIONARY);
         }
      });
   }

   @Override
   public final void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LoginAction.this.login();
         }
      });
   }

   private void login() {
      this.setEnabled(false);
      this.setInitialLoginState(this.serverManager.getLoginService());
      LoginServiceUi var1 = new LoginServiceUi(this.serverManager);
      var1.loginWithDialog(new LoginAction.CallbackImpl());
   }

   protected void setInitialLoginState(LoginService var1) {
   }

   private final class CallbackImpl implements LoginCallback {
      private CallbackImpl() {
      }

      @Override
      public void loginSucceeded(ServerInfo var1, LoginCredentials var2, User var3) {
         this.enable();
      }

      @Override
      public void invalidCredentials(InvalidCredentialsException var1) {
         this.enable();
      }

      @Override
      public void cancelled() {
         this.enable();
      }

      @Override
      public void caught(Throwable var1) {
         this.enable();
      }

      private void enable() {
         LoginAction.this.setEnabledSafely(true);
      }
   }
}
