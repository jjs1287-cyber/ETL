package org.etlforge.application.ui.widget.password;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.overlay.OverlayContainer;
import org.etlforge.ui.widget.overlay.OverlayDecorator;
import org.etlforge.ui.widget.overlay.OverlayPositioner;

public final class PasswordDefinitionUiOverlay {
   private final Receiver<char[]> receiver;
   private OverlayDecorator overlay;
   private OverlayContainer container;
   private OverlayPositioner positioner;
   private PasswordDefinitionUi pwdUi;
   private boolean includeReTypePassword;
   private JComponent focusReceiverWhenClosed;

   public PasswordDefinitionUiOverlay(Receiver<char[]> var1) {
      this.receiver = var1;
   }

   public void setFocusReceiverWhenClosed(JComponent var1) {
      this.focusReceiverWhenClosed = var1;
   }

   public void displayIn(OverlayContainer var1, OverlayPositioner var2) {
      this.container = var1;
      this.positioner = var2;
      this.pwdUi = PasswordDefinitionUiFactory.getUi();
      this.pwdUi.setIncludeReTypeField(this.includeReTypePassword);
      this.overlay = this.buildOverlay();
      this.overlay.show(var1, var2);
      JRootPane var3 = SwingUtilities.getRootPane(this.overlay.get());
      if (var3 != null) {
         this.pwdUi.addCapsLockListener(var3);
      }

      this.pwdUi.requestFocus();
   }

   public void setIncludeReTypeField(boolean var1) {
      this.includeReTypePassword = var1;
   }

   public void close() {
      if (this.container != null) {
         this.overlay.hide();
      }
   }

   private OverlayDecorator buildOverlay() {
      JPanel var1 = BorderLayoutBuilder.fillWith(this.pwdUi);
      var1.setBorder(Empty.border(10, 10, 0, 10));
      OverlayDecorator var2 = new OverlayDecorator(var1, null) {
         @Override
         protected void onCancel() {
            PasswordDefinitionUiOverlay.this.dispose(null);
         }

         @Override
         protected void onOk() {
            if (PasswordDefinitionUiOverlay.this.pwdUi.validate()) {
               char[] var1x = PasswordDefinitionUiOverlay.this.pwdUi.getPassword();
               PasswordDefinitionUiOverlay.this.dispose(var1x);
            } else {
               PasswordDefinitionUiOverlay.this.redisplay();
            }
         }
      };
      var2.useOkAndCancel();
      var2.setBlocking(true);
      var2.setFocusReceiverWhenClosed(this.focusReceiverWhenClosed);
      return var2;
   }

   private void redisplay() {
      this.overlay.setFadeIn(false);
      this.overlay.show(this.container, this.positioner);
   }

   private void dispose(char[] var1) {
      this.pwdUi.dispose();
      this.overlay.hide();
      this.receiver.handle(var1);
   }
}
