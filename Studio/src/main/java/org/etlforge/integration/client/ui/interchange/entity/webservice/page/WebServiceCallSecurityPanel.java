package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import com.google.common.base.Optional;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.application.ui.widget.password.AbstractPasswordAction;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.text.KongaTextField;

public final class WebServiceCallSecurityPanel extends InputPanel {
   private final JTextField userNameField;
   private final JButton passwordButton;
   private Optional<Password> password = Optional.absent();

   public WebServiceCallSecurityPanel() {
      this(null);
   }

   public WebServiceCallSecurityPanel(WebServiceCall var1) {
      this.userNameField = new KongaTextField(20);
      this.passwordButton = this.createPasswordButton();
      this.layoutComponents();
      if (var1 != null) {
         this.setValues(var1);
      }

      this.addInputFields();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.userNameField);
   }

   public void setValues(WebServiceCall var1) {
      this.userNameField.setText(var1.getHttpUserName());
      this.password = var1.getHttpPassword();
   }

   public void applyValuesTo(WebServiceCall var1) {
      var1.setHttpUserName(this.userNameField.getText());
      var1.setHttpPassword(this.password);
   }

   private JButton createPasswordButton() {
      WebServiceCallSecurityPanel.PasswordAction var1 = new WebServiceCallSecurityPanel.PasswordAction();
      KongaButton var2 = new KongaButton(var1);
      var1.setTrigger(this.passwordButton);
      return var2;
   }

   private void layoutComponents() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 5, 10, 5);
      this.layoutUserField(var1);
      var1.setFill(OneColumnPanel.NONE);
      this.layoutPasswordButton(var1);
      var1.setFill(OneColumnPanel.HORIZONTAL);
      this.wrap(var1);
   }

   private void layoutUserField(OneColumnPanel var1) {
      JComponent var2 = InputFieldFactory.getLabeledComponent(this.userNameField, Strings.get("UserName"));
      var1.add(var2);
   }

   private void layoutPasswordButton(OneColumnPanel var1) {
      var1.add(this.passwordButton);
   }

   private void addInputFields() {
      this.addInputField(new TextInputField("Name", this.userNameField));
   }

   private class PasswordAction extends AbstractPasswordAction {
      private PasswordAction() {
      }

      @Override
      protected void handlePassword(char[] var1) {
         if (var1 != null) {
            WebServiceCallSecurityPanel.this.password = Optional.of(Password.encrypt(new String(var1)));
            WebServiceCallSecurityPanel.this.fireInputChanged(WebServiceCallSecurityPanel.this.passwordButton, "Password");
         }
      }
   }
}
