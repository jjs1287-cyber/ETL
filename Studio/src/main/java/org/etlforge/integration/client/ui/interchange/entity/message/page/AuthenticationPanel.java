package org.etlforge.integration.client.ui.interchange.entity.message.page;

import com.google.common.base.Optional;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.password.AbstractPasswordAction;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.Message;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class AuthenticationPanel implements UiProvider {
   private final AuthenticationPanel.Form inputPanel;
   private final CheckBoxInputField activationCheck;
   private final TextInputField account;
   private final AuthenticationPanel.PasswordAction passwordAction;
   private final JButton passwordButton;
   private Optional<Password> password = Optional.absent();
   private DropDownPanel dropDownPanel;

   public AuthenticationPanel() {
      this.activationCheck = this.createActivationControl();
      this.account = this.createAccountField();
      this.passwordAction = new AuthenticationPanel.PasswordAction();
      this.passwordButton = new KongaButton(this.passwordAction);
      this.passwordAction.setTrigger(this.passwordButton);
      this.inputPanel = this.createForm();
      this.layoutComponents();
   }

   private CheckBoxInputField createActivationControl() {
      String var1 = Strings.get("MessagePage.Label.UseAuthentication");
      CheckBoxInputField var2 = InputFieldFactory.getCheckBoxInputField(var1, var1);
      var2.getInputComponent().addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            AuthenticationPanel.this.updateEnabledState();
         }
      });
      return var2;
   }

   private TextInputField createAccountField() {
      String var1 = Strings.get("MessagePage.Label.Account");
      return InputFieldFactory.getTextInputField(var1, var1, 30, true);
   }

   private AuthenticationPanel.Form createForm() {
      AuthenticationPanel.Form var1 = new AuthenticationPanel.Form();
      var1.addInputField(this.activationCheck);
      var1.addInputField(this.account);
      var1.setSendInputChangeEvents(true);
      var1.setSendUndoEvents(true);
      return var1;
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withAnchor(OneRowPanel.SOUTHWEST);
      var1.withInsets(5, 25, 5, 5);
      var1.addRow(this.activationCheck.getInputComponent());
      var1.withInsets(5, 5, 5, 5);
      JComponent var2 = getInputFieldForLayout(this.account);
      JButton var3 = this.passwordButton;
      var1.addRow(var2, var3);
      JPanel var4 = InvisiblePanel.newPanel();
      var1.addToParent(var4, null);
      UiUtils.setOpaqueRecursivelySkipDefaults(var4, false);
      this.dropDownPanel = this.createDropdownPanel(var4);
   }

   private DropDownPanel createDropdownPanel(JComponent var1) {
      DropDownPanel var2 = new DropDownPanel(var1, Strings.get("MessagePage.Label.Authentication"), false);
      var2.setFocusWhenExpanded(this.activationCheck.getInputComponent());
      return var2;
   }

   private static JComponent getInputFieldForLayout(InputField var0) {
      JComponent var1 = var0.getInputComponent();
      boolean var2 = var1 instanceof Scrollable && !(var1 instanceof JTextField);
      JComponent var3 = InputFieldFactory.getCapsule(var0, var2);
      var3.setBorder(Empty.border(0, 25, 0, 0));
      return var3;
   }

   public void applyTo(Message var1) {
      if (this.activationCheck.isSelected()) {
         var1.setAccountName(this.account.getValue().trim());
         var1.setAccountPassword(this.password);
      } else {
         var1.setAccountName("");
         Password var2 = Password.encrypt("");
         var1.setAccountPassword(Optional.of(var2));
      }
   }

   public void setMessage(Message var1) {
      String var2 = var1.getAccountName();
      this.account.setValue(var2);
      this.password = var1.getAccountPassword();
      this.activationCheck.setSelected(var2 != null && var2.length() > 0);
      if (this.activationCheck.isSelected()) {
         this.dropDownPanel.expand();
      } else {
         this.dropDownPanel.collapse();
      }

      this.updateEnabledState();
   }

   private void updateEnabledState() {
      boolean var1 = this.activationCheck.isSelected();
      this.account.setActive(var1);
      this.account.setEnabled(var1);
      this.passwordAction.setEnabled(var1);
      if (var1) {
         this.account.requestFocus();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.dropDownPanel.getDisplayer());
   }

   @Override
   public void requestFocus() {
      this.account.requestFocus();
   }

   public void registerWithParent(InputPanel var1) {
      var1.addInputPanel(this.inputPanel);
   }

   private static class Form extends InputPanel {
      private Form() {
      }

      public void inputChanged(Object var1, String var2) {
         super.fireInputChanged(var1, var2);
      }
   }

   private class PasswordAction extends AbstractPasswordAction {
      private PasswordAction() {
      }

      @Override
      protected void handlePassword(char[] var1) {
         if (var1 != null) {
            AuthenticationPanel.this.password = Optional.of(Password.encrypt(new String(var1)));
            AuthenticationPanel.this.inputPanel.inputChanged(AuthenticationPanel.this.passwordButton, "Password");
         }
      }
   }
}
