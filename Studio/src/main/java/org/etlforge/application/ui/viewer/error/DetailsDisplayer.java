package org.etlforge.application.ui.viewer.error;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.text.KongaTextArea;

final class DetailsDisplayer implements UiProvider {
   private final JTextArea detailsTextArea = KongaTextArea.viewer(Size.rows(8), Size.columns(40));

   public DetailsDisplayer() {
      this.detailsTextArea.setFont(TextStyles.DefaultMonoSpaced.getFont());
   }

   public void displayDetails(final String var1) {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DetailsDisplayer.this.detailsTextArea.setText(var1.trim());
            DetailsDisplayer.this.detailsTextArea.setCaretPosition(0);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = TextCapsuleFactory.encapsulate(this.detailsTextArea, Icons.EMPTY_ICON, true);
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.detailsTextArea.requestFocusInWindow();
   }
}
