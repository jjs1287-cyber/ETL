package org.etlforge.integration.data.entity.script;

import java.awt.Window;
import org.etlforge.integration.data.script.GrammarUtils;

public final class GuidPathTranslator {
   public static String translateToGuid(String var0, GuidPathCache var1, Window var2) {
      if(var0.contains("<javascript>")){
         java.util.regex.Matcher single=java.util.regex.Pattern.compile("'<TAG>(.*?)</TAG>'",java.util.regex.Pattern.DOTALL).matcher(var0);
         StringBuffer text=new StringBuffer();
         while(single.find()){
            String path=org.apache.commons.lang.StringEscapeUtils.unescapeJavaScript(single.group(1));String guid=var1.getGuid(path);
            single.appendReplacement(text,java.util.regex.Matcher.quoteReplacement(guid==null?single.group():"'"+guid+"'"));
         }
         single.appendTail(text);var0=text.toString();
      }
      StringBuilder var3 = new StringBuilder();
      int var4 = 0;
      int var6 = -1;

      int var5;
      while ((var5 = var0.indexOf("\"<TAG>", var6 + 1)) >= 0) {
         var6 = var0.indexOf("</TAG>\"", var5);
         if (var6 < 0) {
            break;
         }

         String var7 = var0.substring(var5 + "\"<TAG>".length(), var6);
         var6 += "</TAG>\"".length();
         String var8 = var1.getGuid(var7);
         if(var8==null&&var0.contains("<javascript>"))var8=var1.getGuid(org.apache.commons.lang.StringEscapeUtils.unescapeJavaScript(var7));
         if (var8 == null) {
            var6 = var5 + "\"<TAG>".length();
         } else {
            if (var5 > var4) {
               var3.append(var0.substring(var4, var5));
            }

            var3.append('"');
            var3.append(var8);
            var3.append('"');
            var4 = var6;
         }
      }

      if (var4 == 0) {
         return var0;
      }

      var3.append(var0.substring(var4));
      return var3.toString();
   }

   public static String translateToPath(String var0, GuidPathCache var1) {
      if(var0.contains("<javascript>")){
         java.util.regex.Matcher single=java.util.regex.Pattern.compile("'([st]\\.[0-9a-fA-F-]{36})'").matcher(var0);StringBuffer text=new StringBuffer();
         while(single.find()){
            String path=var1.getPathFromGuid(single.group(1));
            single.appendReplacement(text,java.util.regex.Matcher.quoteReplacement(path==null?single.group():"'<TAG>"+org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(path).replace("\\/","/")+"</TAG>'"));
         }
         single.appendTail(text);var0=text.toString();
      }
      StringBuilder var2 = new StringBuilder();
      int var3 = 0;
      int var5 = -1;

      int var4;
      while ((var4 = GrammarUtils.getIndexOfNextId(var0, var5 + 1)) >= 0) {
         var5 = var0.indexOf(34, var4 + 1);
         if (var5 < 0) {
            break;
         }

         String var6 = var0.substring(var4 + 1, var5);
         String var7 = var1.getPathFromGuid(var6);
         if (var7 == null) {
            var4 = var5 + 1;
         } else {
            if (var4 > var3) {
               var2.append(var0.substring(var3, var4));
            }

            var2.append("\"<TAG>");
            var2.append(var0.contains("<javascript>")?org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(var7).replace("\\/","/"):var7);
            var2.append("</TAG>\"");
            var3 = var5 + 1;
         }
      }

      if (var3 == 0) {
         return var0;
      }

      var2.append(var0.substring(var3));
      return var2.toString();
   }

   private GuidPathTranslator() {
   }
}
