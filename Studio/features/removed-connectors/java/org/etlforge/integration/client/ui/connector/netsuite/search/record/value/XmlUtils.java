package org.etlforge.integration.client.ui.connector.netsuite.search.record.value;

import com.google.common.base.Strings;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringEscapeUtils;

public class XmlUtils {
   public static void ShortElement(StringBuilder var0, String var1) {
      var0.append('<').append(var1).append("/>");
   }

   public static void ElementValue(StringBuilder var0, String var1, String var2) {
      if (Strings.isNullOrEmpty(var2)) {
         ShortElement(var0, var1);
      } else {
         startElementTag(var0, var1);
         var0.append(StringEscapeUtils.escapeXml(var2));
         endElementTag(var0, var1);
      }
   }

   public static void startElementTag(StringBuilder var0, String var1) {
      var0.append('<').append(var1).append('>');
   }

   public static void endElementTag(StringBuilder var0, String var1) {
      var0.append('<').append('/').append(var1).append('>');
   }

   public static void startElementTagAttribute(StringBuilder var0, String var1, String var2, String var3) {
      var0.append('<').append(var1);
      addAttribute(var0, var2, var3);
      var0.append('>');
   }

   public static void startElementTagPrefixNamespaceDef(StringBuilder var0, String var1, Map<String, String> var2) {
      var0.append('<').append(var1);
      addPrefixNamespaceMapDef(var0, var2);
      var0.append('>');
   }

   private static void addPrefixNamespaceMapDef(StringBuilder var0, Map<String, String> var1) {
      if (var1 != null) {
         for (Entry var3 : var1.entrySet()) {
            addPrefixNamespaceDef(var0, (String)var3.getKey(), (String)var3.getValue());
         }
      }
   }

   private static void addPrefixNamespaceDef(StringBuilder var0, String var1, String var2) {
      var0.append(' ').append("xmlns");
      if (!Strings.isNullOrEmpty(var1)) {
         var0.append(':').append(var1);
      }

      var0.append("=\"").append(var2).append('"');
   }

   public static void startElementTagAttributes(StringBuilder var0, String var1, Map<String, String> var2) {
      var0.append('<').append(var1);
      addAttributes(var0, var2);
      var0.append('>');
   }

   private static void addAttributes(StringBuilder var0, Map<String, String> var1) {
      if (var1 != null) {
         for (Entry var3 : var1.entrySet()) {
            addAttribute(var0, (String)var3.getKey(), (String)var3.getValue());
         }
      }
   }

   public static void addAttribute(StringBuilder var0, String var1, String var2) {
      var0.append(' ').append(var1).append("=\"").append(StringEscapeUtils.escapeXml(var2)).append('"');
   }
}
