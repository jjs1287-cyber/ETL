package org.etlforge.integration.activity.ui.log;

import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.log.OperationLogMessage;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.text.KongaTextPane;

public final class StyledTextLogMessageDisplayer implements LogMessageDisplayer {
   private final KongaTextPane textPane = new KongaTextPane();
   private final FindInMessageSupport findSupport;

   public StyledTextLogMessageDisplayer() {
      this.textPane.setEditable(false);
      StyledDocument var1 = this.textPane.getStyledDocument();
      StyledTextLogMessageDisplayer.StyleDefiner.addStylesToDocument(var1);
      this.findSupport = new FindInMessageSupport(this.textPane);
   }

   @Override
   public void displayDetails(OperationLogEntry var1) {
      this.clear();
      if (var1.hasDetails()) {
         this.insertLogDetails(var1);
      } else {
         this.insertShortMessage(var1);
      }

      this.textPane.setCaretPosition(0);
   }

   private void insertShortMessage(OperationLogEntry var1) {
      this.textPane.insertStyledText(var1.getShortMessage(), "text");
   }

   private void insertLogDetails(OperationLogEntry var1) {
      for (OperationLogMessage var3 : var1.getDetails()) {
         String var4 = var3.getEntryName() + ":\n";
         this.textPane.insertStyledText(var4, "header");
         String var5 = var3.getMessage() + "\n\n";
         this.textPane.insertStyledText(var5, "text");
      }
   }

   @Override
   public void clear() {
      this.textPane.setText("");
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(new JScrollPane(this.textPane));
   }

   @Override
   public void requestFocus() {
      this.textPane.requestFocusInWindow();
   }

   @Override
   public Action getSearchAction() {
      return this.findSupport.getFindAction();
   }

   private static final class StyleDefiner {
      public static final String HEADER = "header";
      public static final String TEXT = "text";

      public static void addStylesToDocument(StyledDocument var0) {
         Style var1 = StyleContext.getDefaultStyleContext().getStyle("default");
         Style var2 = var0.addStyle("text", var1);
         StyleConstants.setFontFamily(var2, "Verdana");
         StyleConstants.setFontSize(var2, 12);
         Style var3 = var0.addStyle("header", var1);
         StyleConstants.setFontFamily(var3, "Verdana");
         StyleConstants.setFontSize(var3, 12);
         StyleConstants.setBold(var3, true);
      }
   }
}
