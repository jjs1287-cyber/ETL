package org.etlforge.application.ui.viewer.error;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.text.KongaTextArea;

final class MessageDisplayer implements UiProvider {
   private final JTextArea messageArea = KongaTextArea.viewer(Size.rows(3), Size.columns(40));

   public MessageDisplayer() {
      TextStyles.InfoText.makeOver(this.messageArea);
   }

   public void displayMessage(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            MessageDisplayer.this.messageArea.setText(var1);
            MessageDisplayer.this.messageArea.setCaretPosition(0);
         }
      });
   }

   public String getMessage() {
      return this.messageArea.getText();
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = TextCapsuleFactory.encapsulate(this.messageArea, Icons.ERROR_ICON, false);
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.messageArea.requestFocusInWindow();
   }
}
