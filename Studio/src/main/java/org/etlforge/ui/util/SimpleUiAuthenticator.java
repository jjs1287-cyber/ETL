package org.etlforge.ui.util;

import com.google.common.base.Supplier;
import java.awt.event.ActionListener;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import javax.swing.JLabel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.text.KongaPasswordField;
import org.etlforge.ui.widget.text.KongaTextField;

public final class SimpleUiAuthenticator extends Authenticator {
   @Override
   protected PasswordAuthentication getPasswordAuthentication() {
      return UiThreadSupplier.call(new Supplier<PasswordAuthentication>() {
         public PasswordAuthentication get() {
            return SimpleUiAuthenticator.this.askForCredentials(SimpleUiAuthenticator.this.getRequestingPrompt());
         }
      });
   }

   private PasswordAuthentication askForCredentials(String var1) {
      SimpleUiAuthenticator.Ui var2 = new SimpleUiAuthenticator.Ui(var1);
      KongaDialog var3 = new KongaDialog(UiUtils.getActiveWindow(), "Authentication Required");
      var3.standardLayout(var2, KongaDialog.OK_CANCEL);
      var2.addActionListener(var3.getOkActionListener());
      var3.setLocationRelativeTo(null);
      var3.setResizable(false);
      var3.setOkButtonAsDefault();
      var3.setVisible(true);
      return var3.wasOkPressed() ? new PasswordAuthentication(var2.getUserName(), var2.getPassword()) : null;
   }

   private static class Ui implements UiProvider {
      private final String caption;
      private final KongaTextField userField;
      private final KongaPasswordField passwordField;

      public Ui(String var1) {
         this.caption = var1;
         this.userField = new KongaTextField(20);
         this.passwordField = new KongaPasswordField(20);
      }

      @Override
      public void decorate(UiCanvas var1) {
         TwoColumnPanel var2 = new TwoColumnPanel();
         var2.withLeftInsets(0, 0, 8, 5);
         var2.withRightInsets(0, 0, 8, 0);
         var2.addRow(new JLabel(this.caption));
         this.userField.setLabel("&User name:");
         this.userField.layoutWithLabel(var2);
         this.passwordField.setLabel("&Password");
         var2.addRow(this.passwordField.getLabel(), this.passwordField);
         var2.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.userField.requestFocusInWindow();
      }

      public void addActionListener(ActionListener var1) {
         this.userField.addActionListener(var1);
         this.passwordField.addActionListener(var1);
      }

      public String getUserName() {
         return this.userField.getText();
      }

      public char[] getPassword() {
         return this.passwordField.getPassword();
      }
   }
}
