package org.etlforge.integration.design.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.design.exception.ConnectorRuntimeException;
import org.xml.sax.Attributes;

public class XmlElementUtils {
   private static char xmlStartAngle = '<';
   private static char xmlendAngle = '>';
   private static char xmlslash = '/';

   public static String endElementTag(String var0) {
      StringBuilder var1 = new StringBuilder();
      endElementTag(var1, var0);
      return var1.toString();
   }

   public static void endElementTag(Appendable var0, String var1) {
      try {
         var0.append(xmlStartAngle).append(xmlslash).append(var1).append(xmlendAngle);
      } catch (IOException var3) {
         throw new ConnectorRuntimeException(var3);
      }
   }

   public static void emptyElement(Appendable var0, String var1) {
      try {
         var0.append(xmlStartAngle).append(var1).append(xmlslash).append(xmlendAngle);
      } catch (IOException var3) {
         throw new ConnectorRuntimeException(var3);
      }
   }

   public static String elementValue(String var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      elementValue(var2, var0, var1);
      return var2.toString();
   }

   public static void elementValue(Appendable var0, String var1, String var2) {
      try {
         if (Strings.isNullOrEmpty(var2)) {
            emptyElement(var0, var1);
         } else {
            startElementTag(var0, var1);
            var0.append(StringEscapeUtils.escapeXml(var2));
            endElementTag(var0, var1);
         }
      } catch (IOException var4) {
         throw new ConnectorRuntimeException(var4);
      }
   }

   public static void elementValueOptional(Appendable var0, String var1, String var2) {
      if (!Strings.isNullOrEmpty(var2)) {
         elementValue(var0, var1, var2);
      }
   }

   public static String startElementTag(String var0) {
      StringBuilder var1 = new StringBuilder();
      startElementTag(var1, var0);
      return var1.toString();
   }

   public static void startElementTag(Appendable var0, String var1) {
      try {
         var0.append(xmlStartAngle).append(var1).append(xmlendAngle);
      } catch (IOException var3) {
         throw new ConnectorRuntimeException(var3);
      }
   }

   public static String startElementTag(String var0, Attributes var1) {
      StringBuilder var2 = new StringBuilder();
      startElementTag(var2, var0, var1);
      return var2.toString();
   }

   public static void startElementTag(Appendable var0, String var1, Attributes var2) {
      startElementTag(var0, var1, var2, null);
   }

   public static void startElementTag(Appendable var0, String var1, Attributes var2, Map<String, String> var3) {
      try {
         var0.append(xmlStartAngle);
         var0.append(var1);
         addAttributes(var0, var2);
         addPrefixDefs(var0, var3);
         var0.append(xmlendAngle);
      } catch (IOException var5) {
         throw new ConnectorRuntimeException(var5);
      }
   }

   private static void addAttributesExcluding(Appendable var0, Attributes var1, String var2) {
      if (var1 != null) {
         boolean var3 = !Strings.isNullOrEmpty(var2);

         for (int var4 = 0; var4 < var1.getLength(); var4++) {
            String var5 = var1.getQName(var4);
            String var6 = var1.getValue(var4);
            if (!var3 || !var5.equals(var2)) {
               addAttribute(var0, var5, var6);
            }
         }
      }
   }

   public static void startElementTagExcludingAttribute(Appendable var0, String var1, Attributes var2, String var3) {
      try {
         var0.append(xmlStartAngle);
         var0.append(var1);
         addAttributesExcluding(var0, var2, var3);
         var0.append(xmlendAngle);
      } catch (IOException var5) {
         throw new ConnectorRuntimeException(var5);
      }
   }

   public static void startElementTagAttributeMap(Appendable var0, String var1, Map<String, String> var2, Map<String, String> var3) {
      try {
         var0.append(xmlStartAngle);
         var0.append(var1);
         addAttributes(var0, var2);
         addPrefixDefs(var0, var3);
         var0.append(xmlendAngle);
      } catch (IOException var5) {
         throw new ConnectorRuntimeException(var5);
      }
   }

   public static void startElementTagPrefixDefs(Appendable var0, String var1, Map<String, String> var2) {
      startElementTag(var0, var1, null, var2);
   }

   private static void addPrefixDefs(Appendable var0, Map<String, String> var1) {
      if (var1 != null && !var1.isEmpty()) {
         for (Entry var3 : var1.entrySet()) {
            addPrefixDef(var0, (String)var3.getKey(), (String)var3.getValue());
         }
      }
   }

   private static void addPrefixDef(Appendable var0, String var1, String var2) {
      if (!Strings.isNullOrEmpty(var2)) {
         StringBuilder var3 = new StringBuilder();
         var3.append("xmlns");
         if (!Strings.isNullOrEmpty(var1)) {
            var3.append(':').append(var1);
         }

         addAttribute(var0, var3.toString(), var2);
      }
   }

   private static void addAttribute(Appendable var0, String var1, String var2) {
      try {
         var0.append(' ');
         var0.append(var1);
         var0.append("=\"");
         var0.append(StringEscapeUtils.escapeXml(var2));
         var0.append('"');
      } catch (IOException var4) {
         throw new ConnectorRuntimeException(var4);
      }
   }

   private static void addAttributes(Appendable var0, Map<String, String> var1) {
      if (var1 != null) {
         for (Entry var3 : var1.entrySet()) {
            addAttribute(var0, (String)var3.getKey(), (String)var3.getValue());
         }
      }
   }

   private static void addAttributes(Appendable var0, Attributes var1) {
      if (var1 != null) {
         for (int var2 = 0; var2 < var1.getLength(); var2++) {
            addAttribute(var0, var1.getQName(var2), var1.getValue(var2));
         }
      }
   }

   public static void appendString(Appendable var0, String var1) {
      try {
         var0.append(var1);
      } catch (IOException var3) {
         throw new ConnectorRuntimeException(var3);
      }
   }

   public static void appendXmlValue(Appendable var0, String var1) {
      appendString(var0, StringEscapeUtils.escapeXml(var1));
   }

   public static void startElementTag(Appendable var0, String var1, String var2, String var3) {
      try {
         var0.append(xmlStartAngle);
         var0.append(var1);
         addPrefixDef(var0, var2, var3);
         var0.append(xmlendAngle);
      } catch (IOException var5) {
         var5.printStackTrace();
      }
   }

   public static String getQName(String var0, String var1) {
      return Strings.isNullOrEmpty(var0) ? var1 : var0 + ":" + var1;
   }

   public static String getPrefixFromQName(String var0) {
      String[] var1 = var0.split(":");
      return var1.length == 1 ? "" : var1[0];
   }

   public static void endElementTagShort(StringBuilder var0, String var1) {
      int var2 = var0.length() - 1;
      if (var2 > 1 && var0.charAt(var2) == xmlendAngle) {
         var0.insert(var2, xmlslash);
      } else {
         endElementTag(var0, var1);
      }
   }

   public static void startElementTagDerivedType(Appendable var0, String var1, Attributes var2, Map<String, String> var3, String var4) {
      try {
         var0.append(xmlStartAngle);
         var0.append(var1);
         addAttributes(var0, var2);
         if (!Strings.isNullOrEmpty(var4)) {
            addAttribute(var0, "xsi:type", var4);
         }

         addPrefixDefs(var0, var3);
         var0.append(xmlendAngle);
      } catch (IOException var6) {
         throw new ConnectorRuntimeException(var6);
      }
   }

   public static Map<String, String> toMap(Attributes var0) {
      TreeMap var1 = Maps.newTreeMap();
      if (var0 == null) {
         return var1;
      }

      for (int var2 = 0; var2 < var0.getLength(); var2++) {
         var1.put(var0.getQName(var2), var0.getValue(var2));
      }

      return var1;
   }
}
