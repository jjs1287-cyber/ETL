package org.etlforge.integration.activity.ui.log;

import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.log.OperationLogMessage;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class PlainTextLogMessageDisplayer implements LogMessageDisplayer {
   private final JTextArea textArea = KongaTextArea.viewer(Size.rows(10), Size.columns(60));
   private final FindInMessageSupport findSupport = new FindInMessageSupport(this.textArea);

   @Override
   public void displayDetails(OperationLogEntry var1) {
      this.clear();
      if (var1.hasDetails()) {
         this.insertLogDetails(var1);
      } else {
         this.insertShortMessage(var1);
      }

      this.textArea.setCaretPosition(0);
   }

   @Override
   public void clear() {
      this.textArea.setText("");
   }

   private void insertShortMessage(OperationLogEntry var1) {
      this.textArea.setText(var1.getShortMessage());
   }

   private void insertLogDetails(OperationLogEntry var1) {
      for (OperationLogMessage var3 : var1.getDetails()) {
         String var4 = var3.getEntryName() + ":\n\n";
         this.textArea.append(var4);
         String var5 = var3.getMessage() + "\n\n\n";
         this.textArea.append(var5);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.textArea));
   }

   @Override
   public void requestFocus() {
      this.textArea.requestFocusInWindow();
   }

   @Override
   public Action getSearchAction() {
      return this.findSupport.getFindAction();
   }
}
