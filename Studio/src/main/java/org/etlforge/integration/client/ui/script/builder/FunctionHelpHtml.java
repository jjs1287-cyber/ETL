package org.etlforge.integration.client.ui.script.builder;

import java.text.BreakIterator;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.data.script.Function;

public final class FunctionHelpHtml {
   private final Function function;
   private String html;
   private int width;

   public FunctionHelpHtml(Function var1) {
      this.function = var1;
      this.width = 400;
   }

   public void setWidth(int var1) {
      this.width = var1;
   }

   public String getFullDocumentation() {
      if (this.html == null) {
         this.html = this.createFullHtml();
      }

      return this.html;
   }

   private String createFullHtml() {
      String var1 = this.function.getDisplayName();
      String var2 = "";
      String var3 = "";
      if (StringUtils.isNotEmpty(var1)) {
         var2 = "<h2>Declaration:</h2>" + var1 + "<br>";
      }

      String var4 = this.function.getDocumentation();
      if (StringUtils.isNotEmpty(var4)) {
         var3 = "<h2>Description:</h2>" + var4;
      }

      return "<html>" + var2 + var3 + "</html>";
   }

   public String getFirstSentenceOfDocumentation() {
      String var1 = this.extractFirstSentenceFromDocumentation();
      return var1 == null ? null : this.wrapInTable(var1);
   }

   private String extractFirstSentenceFromDocumentation() {
      String var1 = this.function.getDocumentation();
      BreakIterator var2 = BreakIterator.getSentenceInstance();
      var2.setText(var1);
      int var3 = var2.next();
      return var3 == -1 ? null : var1.substring(0, var3);
   }

   public String getSignatureAndFirstSentenceOfDocumentation() {
      String var1 = "<code>" + this.function.getDisplayName() + "</code>";
      String var2 = this.extractFirstSentenceFromDocumentation();
      if (var2 != null) {
         var1 = var1 + "<br><hr>" + var2;
      }

      return this.wrapInTable(var1);
   }

   private String wrapInTable(String var1) {
      String var2 = "<html><table width=\"" + this.width + "px\">";
      var2 = var2 + "<tr><td>" + var1 + "</td></tr>";
      return var2 + "</table></html>";
   }
}
