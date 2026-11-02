package org.etlforge.integration.client.ui.jitterpack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.password.AbstractPasswordAction;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class ExportSecurityDefiner implements UiProvider {
   private final ExportSecurityDefiner.PasswordAction passwordAction = new ExportSecurityDefiner.PasswordAction();
   private final JButton passwordButton = this.createButton();
   private final JCheckBox checkBox = this.createCheckBox();
   private char[] password;

   public ExportSecurityDefiner() {
   }

   public char[] getPassword() {
      return this.checkBox.isSelected() ? this.password : null;
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.createUi();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.checkBox.requestFocusInWindow();
   }

   private JCheckBox createCheckBox() {
      KongaCheckBox var1 = new KongaCheckBox(Strings.getEncryptionLabel(PackMode.EXPORT));
      var1.addSelectionChangeListener(new Receiver<Boolean>() {
         public void handle(Boolean var1) {
            ExportSecurityDefiner.this.passwordAction.setEnabled(var1);
            if (var1) {
               ExportSecurityDefiner.this.passwordButton.requestFocusInWindow();
            }
         }
      });
      return var1;
   }

   private JButton createButton() {
      KongaButton var1 = new KongaButton(this.passwordAction);
      this.passwordAction.setTrigger(var1);
      return var1;
   }

   private UiProvider createUi() {
      return BoxBuilder.horizontal()
         .withBorder(StyleSheet.getDefaultTitleBorder(Strings.get("Security")))
         .addAllWithSpace(10, this.checkBox, this.passwordButton);
   }

   private class PasswordAction extends AbstractPasswordAction {
      public PasswordAction() {
         this.setIncludeReTypeField(true);
         this.setEnabled(false);
      }

      @Override
      protected void handlePassword(char[] var1) {
         if (var1 != null) {
            ExportSecurityDefiner.this.password = (char[])var1.clone();
         }
      }
   }
}

