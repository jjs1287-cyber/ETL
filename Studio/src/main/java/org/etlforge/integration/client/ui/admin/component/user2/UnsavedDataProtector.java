package org.etlforge.integration.client.ui.admin.component.user2;

import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.integration.client.server.login.ConnectionChangeVetoException;
import org.etlforge.integration.client.server.login.LoginService;
import org.etlforge.integration.client.server.login.event.LoginListener;
import org.etlforge.integration.client.server.login.event.LoginRequestEvent;
import org.etlforge.integration.client.server.login.event.LogoutRequestEvent;
import org.etlforge.system.Disposable;

class UnsavedDataProtector implements Disposable {
   private final Editor owner;
   private final LoginService loginService;
   private final UserConsoleModel model;
   private final LoginListener handler;

   public UnsavedDataProtector(Editor var1, LoginService var2, UserConsoleModel var3) {
      this.owner = var1;
      this.loginService = var2;
      this.model = var3;
      this.handler = new UnsavedDataProtector.Handler();
      var2.addLoginListener(this.handler);
   }

   @Override
   public void dispose() {
      this.loginService.removeLoginListener(this.handler);
   }

   private class Handler extends LoginListener.Adapter {
      private Handler() {
      }

      @Override
      public void loginRequested(LoginRequestEvent var1) throws ConnectionChangeVetoException {
         if (UnsavedDataProtector.this.model.isDirty()) {
            String var2 = "Connecting to a new ETLForge server, or changing the current connection credentials, may lead to unsaved User & Group changes being lost.\n\nDo you want to continue?";
            this.askUser(var2);
         }
      }

      @Override
      public void logoutRequested(LogoutRequestEvent var1) throws ConnectionChangeVetoException {
         if (UnsavedDataProtector.this.model.isDirty()) {
            String var2 = "All unsaved User & Group changes will be lost if you logout from the ETLForge server.\n\nDo you want to continue?";
            this.askUser(var2);
         }
      }

      private void askUser(String var1) throws ConnectionChangeVetoException {
         this.switchToEditor();
         Answer var2 = Ask.continueOrCancel(null, var1, "Unsaved Changes", Alert.Option.CONTINUE, Alert.Type.WARNING);
         if (!var2.isContinue()) {
            throw new ConnectionChangeVetoException(UserConsolePage.class.getName());
         }
      }

      private void switchToEditor() {
         ApplicationWindow var1 = UnsavedDataProtector.this.owner.getWindow();
         if (var1 != null) {
            var1.getEditorService().openEditor(UnsavedDataProtector.this.owner);
         }
      }
   }
}
