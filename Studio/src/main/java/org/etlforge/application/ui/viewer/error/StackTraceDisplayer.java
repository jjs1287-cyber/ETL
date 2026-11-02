package org.etlforge.application.ui.viewer.error;

import com.google.common.base.Preconditions;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.lang.exception.ExceptionUtils;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class StackTraceDisplayer implements UiProvider {
   private final JTextArea stackTraceArea = KongaTextArea.viewer(Size.rows(20), Size.columns(60));
   private boolean simpleLayout;

   public StackTraceDisplayer() {
      TextStyles.SystemErrText.makeOver(this.stackTraceArea);
      this.stackTraceArea.setLineWrap(false);
   }

   public void displayStackTrace(final Throwable var1) {
      Preconditions.checkNotNull(var1, "th");
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            StackTraceDisplayer.this.stackTraceArea.setText(ExceptionUtils.getStackTrace(var1));
            StackTraceDisplayer.this.stackTraceArea.setCaretPosition(0);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.simpleLayout) {
         var1.display(new JScrollPane(this.stackTraceArea));
      } else {
         JComponent var2 = TextCapsuleFactory.encapsulate(this.stackTraceArea, Icons.EMPTY_ICON, true);
         var1.display(var2);
      }
   }

   @Override
   public void requestFocus() {
      this.stackTraceArea.requestFocusInWindow();
   }

   public void setSimpleLayout(boolean var1) {
      this.simpleLayout = var1;
   }
}
