package org.etlforge.integration.activity.ui.log;

import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.application.ui.widget.HtmlDisplayer;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.client.log.OperationLogMessage;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.text.KongaTextComponentCustomizer;

public final class HtmlLogMessageDisplayer implements LogMessageDisplayer {
   private final JEditorPane editorPane = new HtmlDisplayer();
   private final FindInMessageSupport findSupport;

   public HtmlLogMessageDisplayer() {
                                                                                   
      this.editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
      KongaTextComponentCustomizer.customize(this.editorPane);
      this.findSupport = new FindInMessageSupport(this.editorPane);
   }

   @Override
   public void displayDetails(OperationLogEntry var1) {
      this.clear();
      if (var1.hasDetails()) {
         this.insertLogDetails(var1);
      } else {
         this.insertShortMessage(var1);
      }

      this.editorPane.setCaretPosition(0);
   }

   @Override
   public void clear() {
      this.editorPane.setText("");
   }

   private void insertShortMessage(OperationLogEntry var1) {
      HtmlLogMessageDisplayer.HtmlTable var2 = new HtmlLogMessageDisplayer.HtmlTable();
      var2.addMessage(var1.getShortMessage());
      var2.done();
      this.editorPane.setText(var2.toHtml());
   }

   private void insertLogDetails(OperationLogEntry var1) {
      HtmlLogMessageDisplayer.HtmlTable var2 = new HtmlLogMessageDisplayer.HtmlTable();

      for (OperationLogMessage var4 : var1.getDetails()) {
         var2.addHeader(var4.getEntryName());
         var2.addMessage(var4.getMessage());
      }

      var2.done();
      this.editorPane.setText(var2.toHtml());
   }

   @Override
   public void decorate(UiCanvas var1) {
      JScrollPane var2 = new JScrollPane(this.editorPane);
      var2.setPreferredSize(new Dimension());
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.editorPane.requestFocusInWindow();
   }

   @Override
   public Action getSearchAction() {
      return this.findSupport.getFindAction();
   }

   private static final class HtmlTable {
      private final StringBuilder content = new StringBuilder("<html><table width=500px>");

      public HtmlTable() {
      }

      public void addHeader(String var1) {
         this.openRow();
         this.writeValueInBold(var1);
         this.closeRow();
      }

      public void addMessage(String var1) {
         this.openRow();
         this.writeValue(var1);
         this.addEmptyRow();
         this.closeRow();
      }

      public void done() {
         this.content.append("</table></html>");
      }

      public String toHtml() {
         return this.content.toString();
      }

      private void openRow() {
         this.content.append("<tr><td align=left>");
      }

      private void closeRow() {
         this.content.append("</td></tr>");
      }

      private void addEmptyRow() {
         this.content.append("<tr><td></td></tr>");
      }

      private void writeValue(String var1) {
         this.content.append(translate(var1));
      }

      private void writeValueInBold(String var1) {
         this.content.append("<b>");
         this.writeValue(var1);
         this.content.append("</b>");
      }

      private static String translate(String var0) {
         String var1 = StringEscapeUtils.escapeHtml(var0);
         return var1.replaceAll("\\n", "<br>");
      }
   }
}
