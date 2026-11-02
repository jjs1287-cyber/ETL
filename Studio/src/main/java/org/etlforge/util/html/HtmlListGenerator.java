package org.etlforge.util.html;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.io.KongaIoUtils;

public final class HtmlListGenerator {
   private final String listType;
   private final String bulletType;
   private final List<String> items;
   private String currentItem;

   private HtmlListGenerator(String var1, String var2) {
      this.listType = var1;
      this.bulletType = var2;
      this.items = new ArrayList<>();
      this.currentItem = "";
   }

   public static HtmlListGenerator unordered() {
      return unordered(HtmlListGenerator.BulletType.DISC);
   }

   public static HtmlListGenerator unordered(HtmlListGenerator.BulletType var0) {
      return new HtmlListGenerator("ul", var0.toHtml());
   }

   public static HtmlListGenerator numbered() {
      return numbered(HtmlListGenerator.NumberingType.ARABIC_NUMERALS);
   }

   public static HtmlListGenerator numbered(HtmlListGenerator.NumberingType var0) {
      return new HtmlListGenerator("ol", var0.toHtml());
   }

   public HtmlListGenerator add(String var1) {
      this.add(var1, null);
      return this;
   }

   public HtmlListGenerator add(String var1, HtmlStyleTag var2) {
      String var3 = var2 != null ? var2.wrap(var1) : var1;
      this.currentItem = this.currentItem + var3;
      return this;
   }

   public HtmlListGenerator escapeAndAdd(String var1) {
      return this.escapeAndAdd(var1, null);
   }

   public HtmlListGenerator escapeAndAdd(String var1, HtmlStyleTag var2) {
      String var3 = var2 != null ? var2.escapeAndWrap(var1) : StringEscapeUtils.escapeHtml(var1);
      this.currentItem = this.currentItem + var3;
      return this;
   }

   public HtmlListGenerator newItem() {
      if (this.currentItem.length() > 0) {
         this.items.add(this.currentItem);
      }

      this.currentItem = "";
      return this;
   }

   public String toHtml() {
      StringBuilder var1 = new StringBuilder("<").append(this.listType).append(" type=").append(this.bulletType).append(">");

      for (String var3 : this.items) {
         appendItem(var3, var1);
      }

      if (this.currentItem.length() > 0) {
         appendItem(this.currentItem, var1);
      }

      var1.append(KongaIoUtils.LINE_BREAK).append("</").append(this.listType).append(">");
      return var1.toString();
   }

   private static void appendItem(String var0, StringBuilder var1) {
      var1.append(KongaIoUtils.LINE_BREAK).append("<li>").append(var0).append("</li>");
   }

   public enum BulletType {
      DISC,
      CIRCLE,
      SQUARE;

      private String toHtml() {
         return this.name();
      }
   }

   public enum NumberingType {
      ARABIC_NUMERALS("1"),
      CAPITAL_LETTERS("A"),
      LOWERCASE_LETTER("a"),
      ROMAN_NUMERALS("I"),
      LOWERCASE_ROMAN_NUMERALS("i");

      private final String stringVal;

      NumberingType(String var3) {
         this.stringVal = var3;
      }

      private String toHtml() {
         return this.stringVal;
      }
   }
}
