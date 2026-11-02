package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import org.apache.commons.lang.StringUtils;
import org.etlforge.util.clipboard.ClipboardUtils;
import org.etlforge.util.string.StringLimiter;

final class NodeValue {
   private static final NodeValue EMPTY = new NodeValue("");
   private static final int MAX_VALUE_DISPLAY_LENGTH = 512;
   private final String value;
   private final String display;

   public static NodeValue create(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return EMPTY;
      }

      char[] var1 = var0.toCharArray();
      int var2 = 0;

      for (char var6 : var1) {
         if (Character.isWhitespace(var6)) {
            var1[var2] = ' ';
         }

         var2++;
      }

      return new NodeValue(new String(var1));
   }

   private NodeValue(String var1) {
      this.value = var1;
      this.display = this.createDisplayString();
   }

   public String get() {
      return this.value;
   }

   public String getValueToDisplay() {
      return this.display;
   }

   public void copyToClipboard() {
      ClipboardUtils.copyToSystemClipboard(this.value);
   }

   public boolean isEmpty() {
      return this.value.isEmpty();
   }

   @Override
   public String toString() {
      return this.display;
   }

   private String createDisplayString() {
      return this.value.length() > 512 ? new StringLimiter(this.value).getLimitedString(512) : this.value;
   }
}
