package com.etlforge.integration.client.proxy.ui;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.Window;
import javax.swing.JComponent;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.widget.WizardStyleDialog;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.wait.WaitService;

public final class ProxySettingsEditorModelDialog {
   private final ProxySettingsEditor editor;

   public ProxySettingsEditorModelDialog(ProxySettingsEditor var1) {
      this.editor = Preconditions.checkNotNull(var1);
   }

   public void open(Optional<Component> var1) {
      WizardStyleDialog var2 = this.createDialog(var1);
      this.editor.setWaitService(var2);
      Binding var3 = this.bindDialogButtons(var2);
      var2.setVisible(true);
      var3.dispose();
      this.editor.setWaitService(WaitService.NONE);
   }

   private WizardStyleDialog createDialog(Optional<Component> var1) {
      JComponent var2 = WizardStyleDialog.wrapUi(this.editor);
      Window var3 = ApplicationUi.getWindowAncestor(var1);
      WizardStyleDialog var4 = new WizardStyleDialog(var3, "Proxy Settings", var2, KongaDialog.OK_CANCEL_APPLY, true) {
         @Override
         protected void onOk() {
            ProxySettingsEditorModelDialog.this.editor.save();
            super.onOk();
         }

         @Override
         protected void onApply() {
            ProxySettingsEditorModelDialog.this.editor.save();
         }

         @Override
         protected void onDialogOpened() {
            ProxySettingsEditorModelDialog.this.editor.reset();
            super.onDialogOpened();
         }
      };
      var4.setFocusedComponent(this.editor);
      var4.setResizable(false);
      var4.setLocationRelativeTo(null);
      var4.setOkButtonAsDefault();
      return var4;
   }

   private Binding bindDialogButtons(WizardStyleDialog var1) {
      ProxySettingsEditorModelDialog.ValidStateBinding var2 = new ProxySettingsEditorModelDialog.ValidStateBinding(this.editor.getModel(), var1);
      var2.syncUi();
      return var2;
   }

   private class ValidStateBinding extends AbstractBinding<Boolean> {
      private final WizardStyleDialog dialog;

      ValidStateBinding(ProxySettingsEditorModel var2, WizardStyleDialog var3) {
         super(var2, "valid");
         this.dialog = var3;
      }

      protected void updateUiWithValue(Boolean var1) {
         this.dialog.getOKButton().setEnabled(var1);
         this.dialog.getApplyButton().setEnabled(var1);
      }
   }
}
