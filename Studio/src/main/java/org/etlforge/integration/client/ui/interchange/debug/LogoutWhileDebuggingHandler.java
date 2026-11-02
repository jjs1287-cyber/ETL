package org.etlforge.integration.client.ui.interchange.debug;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import java.text.MessageFormat;
import org.etlforge.application.ui.resources.ApplicationResources;
import org.etlforge.integration.client.server.ServerInfo;
import org.etlforge.integration.client.server.login.ConnectionChangeVetoException;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.integration.client.server.login.event.LoginListener;
import org.etlforge.integration.client.server.login.event.LoginRequestEvent;
import org.etlforge.integration.client.server.login.event.LogoutRequestEvent;
import org.etlforge.integration.debug.client.DebugService;
import org.etlforge.integration.debug.client.DebugSession;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.HtmlLabel;

final class LogoutWhileDebuggingHandler extends LoginListener.Adapter {
   private final DebugService debugService;
   private final DebugSession session;

   public LogoutWhileDebuggingHandler(DebugService var1, DebugSession var2) {
      this.debugService = var1;
      this.session = var2;
   }

   @Override
   public void loginRequested(LoginRequestEvent var1) throws ConnectionChangeVetoException {
      assert !EventQueue.isDispatchThread() : "The contract for ConnectionChangeVeto.execute() promises that the method will not be called from the GUI thread.";
      if (!this.isNothingToWorryAbout(var1.getCurrentServer(), var1.getCurrentCredentials(), var1.getNewCredentials())) {
         this.checkVeto(LogoutWhileDebuggingHandler.PromptMode.LOGIN, var1.getCurrentServer());
      }
   }

   @Override
   public void logoutRequested(LogoutRequestEvent var1) throws ConnectionChangeVetoException {
      this.checkVeto(LogoutWhileDebuggingHandler.PromptMode.LOGOUT, var1.getCurrentServer());
   }

   private void checkVeto(LogoutWhileDebuggingHandler.PromptMode var1, ServerInfo var2) throws ConnectionChangeVetoException {
      if (this.isVetoedImpl(var1, var2)) {
         throw new ConnectionChangeVetoException(this.getClass().getName());
      }
   }

   private boolean isVetoedImpl(LogoutWhileDebuggingHandler.PromptMode var1, ServerInfo var2) {
      boolean var3 = false;
      if (LogoutDuringDebuggingWarningPreference.get()) {
         var3 = !this.isGreenLightedByUser(var1, var2);
      }

      if (!var3) {
         this.stopCurrentSession(this.session);
      }

      return var3;
   }

   private boolean isNothingToWorryAbout(ServerInfo var1, LoginCredentials var2, LoginCredentials var3) {
      return var1 == null || !this.isSessionActive() || this.isSame(var1, var3) && this.isSame(var2, var3);
   }

   private boolean isSessionActive() {
      synchronized (this.debugService) {
         return this.session.getId().equals(this.debugService.getCurrentSessionId());
      }
   }

   private boolean isSame(ServerInfo var1, LoginCredentials var2) {
      return var1.getName().equals(var2.getServer());
   }

   private boolean isSame(LoginCredentials var1, LoginCredentials var2) {
      return var1.getServer().equals(var2.getServer())
         && var1.getUser().equals(var2.getUser())
         && var1.getPasswordHash().equals(var2.getPasswordHash())
         && var1.isUsingHttps() == var2.isUsingHttps();
   }

   private boolean isGreenLightedByUser(final LogoutWhileDebuggingHandler.PromptMode var1, final ServerInfo var2) {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            LogoutWhileDebuggingHandler.Prompt var1x = new LogoutWhileDebuggingHandler.Prompt(var1, var2);
            KongaDialog var2x = LogoutWhileDebuggingHandler.this.createDialog(var1x);
            var2x.setVisible(true);
            boolean var3 = var2x.wasOkPressed();
            if (var3 && var1x.isDoNotShowAgainSelected()) {
               LogoutDuringDebuggingWarningPreference.INSTANCE.set(false);
            }

            return var3;
         }
      });
   }

   private KongaDialog createDialog(LogoutWhileDebuggingHandler.Prompt var1) {
      KongaDialog var2 = new KongaDialog(UiUtils.getActiveFrame(), "Stop current test?", true);
      var2.setFixedButtonSize(false);
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 10);
      var3.center(var1).south(var2.getButtonPanel(KongaDialog.OK_CANCEL, 10));
      var2.getOKButton().setText(var1.mode.continueLabel);
      var2.getCancelButton().setText(var1.mode.cancelLabel);
      var3.withEmptyBorder(10).asContentPaneFor(var2);
      var2.setResizable(false);
      var2.manageLocation(LogoutWhileDebuggingHandler.class);
      var2.pack();
      var2.setOkButtonAsDefault();
      return var2;
   }

   private void stopCurrentSession(DebugSession var1) {
      try {
         var1.getController().stopAndWait();
      } catch (InterruptedException var3) {
         var3.printStackTrace();
         Thread.currentThread().interrupt();
      }
   }

   private static class Prompt implements UiProvider {
      public final LogoutWhileDebuggingHandler.PromptMode mode;
      private final String message;
      private final KongaCheckBox doNotShowAgain;

      public Prompt(LogoutWhileDebuggingHandler.PromptMode var1, ServerInfo var2) {
         this.mode = var1;
         this.message = MessageFormat.format(var1.message, var2.getName());
         this.doNotShowAgain = new KongaCheckBox(ApplicationResources.STRINGS.getString("DoNotShowAgain"));
      }

      public boolean isDoNotShowAgainSelected() {
         return this.doNotShowAgain.isSelected();
      }

      @Override
      public void decorate(UiCanvas var1) {
         HtmlLabel var2 = new HtmlLabel(this.message, 400);
         new BorderLayoutBuilder(0, 5).north(var2).south(this.doNotShowAgain).decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.doNotShowAgain.requestFocusInWindow();
      }
   }

   private enum PromptMode {
      LOGIN(
         "You are currently running a test on the server {0}. That test will be stopped if you login to another server, or login with new credentials.\n\nDo you want to continue?",
         "Continue With &Login",
         "&Cancel Login"
      ),
      LOGOUT("You are currently running a test on the server {0}. That test will be stopped if you logout.", "Continue With &Logout", "&Cancel Logout");

      public final String message;
      public final String continueLabel;
      public final String cancelLabel;

      PromptMode(String var3, String var4, String var5) {
         this.message = var3;
         this.continueLabel = var4;
         this.cancelLabel = var5;
      }
   }
}
