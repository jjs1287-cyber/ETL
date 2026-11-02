package org.etlforge.integration.client.server.ui.user.profile;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.etlforge.application.ui.widget.password.AbstractPasswordAction;
import org.etlforge.integration.user.User;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.DialogLayout;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.overlay.OverlayCenterPositioner;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.ui.widget.text.KongaTextField;

final class UserProfilePanel implements UiProvider {
   private User user;
   private JPanel controlPanel;
   private final JTextField loginField;
   private final JTextField firstNameField;
   private final JTextField lastNameField;
   private final JTextField emailField;
   private final UserProfilePanel.PasswordAction passwordAction = new UserProfilePanel.PasswordAction();

   public UserProfilePanel() {
      this.loginField = new KongaTextField(20);
      this.loginField.setEditable(false);
      this.loginField.setToolTipText(Strings.get("UserProfile.LoginToolTip"));
      this.firstNameField = new KongaTextField(20);
      this.lastNameField = new KongaTextField(20);
      this.emailField = new KongaTextField(40);
      this.layoutComponents();
      this.setEnabled(false);
   }

   public void populate(User var1) {
      this.user = var1;
      this.setEnabled(true);
      this.passwordAction.setEnabled(true);
      this.loginField.setText(var1.getName());
      this.firstNameField.setText(var1.getFirstName());
      this.lastNameField.setText(var1.getLastName());
      this.emailField.setText(var1.getEmail());
   }

   public User applyToUser() {
      this.user.setFirstName(this.firstNameField.getText());
      this.user.setLastName(this.lastNameField.getText());
      this.user.setEmail(this.emailField.getText());
      return this.user;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.controlPanel);
   }

   @Override
   public void requestFocus() {
      this.loginField.requestFocusInWindow();
   }

   private void setEnabled(boolean var1) {
      this.controlPanel.setEnabled(var1);
      this.passwordAction.setEnabled(var1);
   }

   private void layoutComponents() {
      JPanel var1 = new JPanel(new DialogLayout(10, 10));
      this.layoutField(var1, Strings.get("UserProfile.Label.Login"), this.loginField);
      this.layoutField(var1, null, this.createPasswordButton());
      this.layoutField(var1, Strings.get("UserProfile.Label.FirstName"), this.firstNameField);
      this.layoutField(var1, Strings.get("UserProfile.Label.LastName"), this.lastNameField);
      this.layoutField(var1, Strings.get("UserProfile.Label.Email"), this.emailField);
      var1.setBorder(Empty.border(10));
      this.controlPanel = var1;
   }

   private JComponent createPasswordButton() {
      KongaButton var1 = new KongaButton(this.passwordAction);
      return InvisiblePanel.of(var1);
   }

   private void layoutField(JPanel var1, String var2, JComponent var3) {
      JLabel var4 = var2 == null ? new JLabel(" ") : new JLabel(var2);
      var1.add(var4);
      var1.add(var3);
   }

   private class PasswordAction extends AbstractPasswordAction {
      public PasswordAction() {
         this.setIncludeReTypeField(true);
         this.setPositioner(new OverlayCenterPositioner());
         this.setEnabled(false);
      }

      @Override
      protected void handlePassword(char[] var1) {
         if (var1 != null) {
            UserProfilePanel.this.user.setPassword(new String(var1));
         }
      }
   }
}
