package org.etlforge.util.html;

import org.apache.commons.lang.StringEscapeUtils;

public enum HtmlStyleTag {
   BOLD("b"),
   ITALIC("i"),
   UNDERLINE("u");

   private final String value;

   HtmlStyleTag(String var3) {
      this.value = var3;
   }

   public String wrap(String var1) {
      StringBuilder var2 = new StringBuilder("<").append(this.value).append(">");
      var2.append(var1).append("</").append(this.value).append(">");
      return var2.toString();
   }

   public String escapeAndWrap(String var1) {
      return this.wrap(StringEscapeUtils.escapeHtml(var1));
   }
}
