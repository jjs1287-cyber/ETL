package org.etlforge.ui.util;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.collections.Pair;

public final class KeyValueToolTip {
   private KeyValueToolTip() {
   }

   public static String create(Object[] var0, Object[] var1) {
      Preconditions.checkArgument(var0.length == var1.length, "There must be the same number of keys as values");
      StringBuilder var2 = new StringBuilder("<html>");
      int var3 = 0;

      for (Object var7 : var0) {
         if (var3 > 0) {
            var2.append("<br>");
         }

         var2.append("<b>").append(var7).append(":</b> ");
         var2.append(var1[var3]);
         var3++;
      }

      var2.append("</html>");
      return var2.toString();
   }

   public static String create(Collection<Pair<Object, Object>> var0) {
      StringBuilder var1 = new StringBuilder("<html>");
      int var2 = 0;

      for (Pair var4 : var0) {
         if (var2 > 0) {
            var1.append("<br>");
         }

         Object var5 = var4.first;
         var1.append("<b>").append(var5).append(":</b> ");
         Object var6 = var4.second;
         var1.append(var6);
         var2++;
      }

      var1.append("</html>");
      return var1.toString();
   }

   public static String create(Map<Object, Object> var0) {
      StringBuilder var1 = new StringBuilder("<html>");
      int var2 = 0;

      for (Entry var4 : var0.entrySet()) {
         if (var2 > 0) {
            var1.append("<br>");
         }

         Object var5 = var4.getKey();
         var1.append("<b>").append(var5).append(":</b> ");
         Object var6 = var4.getValue();
         var1.append(var6);
         var2++;
      }

      var1.append("</html>");
      return var1.toString();
   }

   public static String createTable(Map<Object, Object> var0) {
      return createTable(var0, null);
   }

   public static String createTable(Map<Object, Object> var0, String var1) {
      StringBuilder var2 = new StringBuilder("<html>");
      var2.append("<table cellspacing=\"1\" cellpadding=\"1\">");

      for (Entry var4 : var0.entrySet()) {
         var2.append("<tr>");
         var2.append("<td><b>").append(var4.getKey()).append("</b></td>");
         var2.append("<td>").append(var4.getValue()).append("</td>");
         var2.append("</tr>");
      }

      var2.append("</table>");
      if (var1 != null && var1.length() > 0) {
         var2.append("<br>").append(var1);
      }

      return var2.append("</html>").toString();
   }
}
