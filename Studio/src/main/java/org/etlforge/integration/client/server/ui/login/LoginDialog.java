package org.etlforge.integration.client.server.ui.login;

import com.google.common.collect.ImmutableList;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.help.WindowHelpButton;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.login.LoginCredentials;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.util.ButtonOrder;
import org.etlforge.ui.util.UiUtils;

public class LoginDialog extends KongaDialog {
   private final LoginUi ui;
   private final JButton helpButton;
   private boolean loginEnabled;

   public LoginDialog(ServerManager var1) {
      this(var1, UiUtils.getActiveWindow());
   }

   public LoginDialog(ServerManager var1, Window var2) {
      super(var2, PackageResources.LoginDialog.TITLE);
      this.ui = this.createLoginUi(var1);
      this.helpButton = new WindowHelpButton(this, "Login_to_Server", false).getButton();
      this.createDialog();
      this.pack();
      this.setResizable(false);
      this.manageLocation();
   }

   private LoginUi createLoginUi(ServerManager var1) {
      LoginUi var2 = LoginUiFactory.getUi(var1);
      var2.setCredentialsReceiver(new LoginDialog.LoginTrigger());
      var2.setValidStateMonitor(new LoginDialog.ValidStateMonitor());
      var2.setCapsLockListener(this.getRootPane());
      return var2;
   }

   @Override
   protected void onDialogClose() {
      try {
         this.ui.dispose();
      } finally {
         super.onDialogClose();
      }
   }

   public LoginCredentials getCredentials() {
      return this.wasOkPressed() ? this.ui.getCredentials() : null;
   }

   private void createDialog() {
      ContainerCanvas.decorate(this.ui, this.getContentPane(), "Center");
      JComponent var1 = this.createButtonPanel();
      this.getContentPane().add(var1, "South");
   }

   private JComponent createButtonPanel() {
      this.setFixedButtonSize(false);
      BoxBuilder var1 = Horizontal.box().withEmptyBorder(10);
      if (!ClientInfo.isDataLoader()) {
         var1.add(this.helpButton);
      }

      JButton var2 = this.createOKButton();
      JButton var3 = this.createCancelButton();
      ImmutableList var4 = ButtonOrder.okAndCancelOrder(var2, var3);
      var1.glue().addAllWithSpace(10, var4);
      this.customizeOKButton();
      this.setOkButtonAsDefault();
      return var1.nonOpaque().container();
   }

   private void customizeOKButton() {
      JButton var1 = this.getOKButton();
      AbstractAction var2 = new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            LoginDialog.this.onOk();
         }
      };
      if (SystemUtils.IS_OS_MAC) {
         var1.setText("Login");
      } else {
         ActionDecorators.LOGIN.decorate(var2);
         var1.setAction(var2);
      }

      var1.setEnabled(this.loginEnabled);
   }

   private class LoginTrigger implements Receiver<LoginCredentials> {
      private LoginTrigger() {
      }

      public void handle(LoginCredentials var1) {
         if (LoginDialog.this.getOKButton().isEnabled()) {
            LoginDialog.this.onOk();
         }
      }
   }

   private class ValidStateMonitor implements Receiver<Boolean> {
      private ValidStateMonitor() {
      }

      public void handle(Boolean var1) {
         LoginDialog.this.loginEnabled = var1;
         JButton var2 = LoginDialog.this.getOKButton();
         if (var2 != null) {
            var2.setEnabled(var1);
         }
      }
   }
}
