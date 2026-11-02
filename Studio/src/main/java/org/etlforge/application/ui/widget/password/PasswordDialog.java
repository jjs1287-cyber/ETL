package org.etlforge.application.ui.widget.password;

import java.awt.EventQueue;
import java.awt.Window;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.util.UiUtils;

public final class PasswordDialog extends KongaDialog {
   private final PasswordDefinitionUi pwdDefinitionUi;

   private PasswordDialog(Window var1, Object var2, boolean var3) {
      super(var1, PackageResources.DIALOG_TITLE);
      this.pwdDefinitionUi = this.createPwdUi(var3);
      this.createDialog(var2);
      this.pack();
      this.setResizable(false);
      this.manageLocation();
   }

   @Override
   protected void onDialogOpened() {
      super.onDialogOpened();
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            PasswordDialog.this.pwdDefinitionUi.requestFocus();
         }
      });
   }

   @Override
   protected void onDialogClose() {
      try {
         this.pwdDefinitionUi.dispose();
      } finally {
         super.onDialogClose();
      }
   }

   public static char[] definePassword(boolean var0) {
      return definePassword(null, var0);
   }

   public static char[] definePassword(String var0, boolean var1) {
      return definePassword(UiUtils.getActiveWindow(), var0, var1);
   }

   public static char[] definePassword(Window var0, String var1, boolean var2) {
      UiProviderAdapter var3 = StringUtils.isBlank(var1) ? null : UiProviderAdapter.label(var1);
      return definePassword(var0, var3, var2);
   }

   public static char[] definePassword(Window var0, UiProvider var1, boolean var2) {
      PasswordDialog var3 = new PasswordDialog(var0, var1, var2);
      var3.setVisible(true);
      return var3.wasOkPressed() ? var3.pwdDefinitionUi.getPassword() : null;
   }

   @Override
   protected void onOk() {
      if (this.pwdDefinitionUi.validate()) {
         super.onOk();
      }
   }

   private void createDialog(Object var1) {
      this.standardLayout(Vertical.borderLayout().of(var1, this.pwdDefinitionUi).withGap(5), OK_CANCEL);
      this.setOkButtonAsDefault();
   }

   private PasswordDefinitionUi createPwdUi(boolean var1) {
      PasswordDefinitionUi var2 = PasswordDefinitionUiFactory.getUi();
      var2.addCapsLockListener(this.getRootPane());
      var2.setIncludeReTypeField(var1);
      return var2;
   }
}
