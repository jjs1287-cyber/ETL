package org.etlforge.integration.client.ui.jitterpack;

import com.google.common.base.Preconditions;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.project.jitterpack.JitterPack;
import org.etlforge.integration.client.ui.jitterpack.resources.JitterpackResources;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.text.KongaPasswordField;

final class JitterPackPasswordPrompter implements UiProvider {
   private final JLabel caption;
   private final JPasswordField pwdField;

   private JitterPackPasswordPrompter(JitterPack var1) {
      this.caption = this.createCaption(var1);
      this.pwdField = new KongaPasswordField(20);
   }

   public static char[] getPassword(JitterPack var0, Window var1) {
      assert EventQueue.isDispatchThread() : "getPassword() must be called from the EDT";
      Preconditions.checkNotNull(var0, "jp");
      JitterPackPasswordPrompter var2 = new JitterPackPasswordPrompter(var0);
      KongaDialog var3 = createDialog(var1, var2);
      var3.setVisible(true);
      return var3.wasOkPressed() ? var2.pwdField.getPassword() : null;
   }

   private static KongaDialog createDialog(Window var0, JitterPackPasswordPrompter var1) {
      final KongaDialog var2 = UiProviderDialogDisplayer.createDialog(
         var0, var1, JitterpackResources.getString("PushedJitterPackHandler.PasswordPrompt.Title"), ModalityType.APPLICATION_MODAL, KongaDialog.OK_CANCEL, null
      );
      var1.pwdField.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            var2.getOKButton().doClick();
         }
      });
      var2.setResizable(false);
      var2.setOkButtonAsDefault();
      return var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder();
      var2.north(this.caption).south(this.pwdField);
      var2.nonOpaque().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.pwdField.requestFocusInWindow();
   }

   private JLabel createCaption(JitterPack var1) {
      return TextStyles.InfoText
         .makeLabel(JitterpackResources.formatMessage("PushedJitterPackHandler.PasswordPrompt.Caption", "Backup", var1.getFile().getName()));
   }
}

