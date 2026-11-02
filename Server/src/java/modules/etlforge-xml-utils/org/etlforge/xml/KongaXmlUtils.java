package org.etlforge.xml;

import java.io.Closeable;
import java.io.IOException;
import java.util.Comparator;
import org.etlforge.lang.StringConverter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;

public final class KongaXmlUtils {
   public static final String NAMESPACE = "namespace";
   public static final String UTF_8_PROCESSING_INSTRUCTION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
   public static final Comparator<ExpandedName> SORT_EXPANDED_NAME_BY_LOCAL_PART = new Comparator<ExpandedName>() {
      public int compare(ExpandedName var1, ExpandedName var2) {
         return var1.getLocalName().compareToIgnoreCase(var2.getLocalName());
      }
   };

   public static void removeWhitespace(Node var0) {
      NodeList var1 = var0.getChildNodes();

      for (int var2 = var1.getLength() - 1; var2 >= 0; var2--) {
         Node var3 = var1.item(var2);
         if (var3 instanceof Text) {
            if (((Text)var3).getData().trim().isEmpty()) {
               var0.removeChild(var3);
            }
         } else {
            removeWhitespace(var3);
         }
      }
   }

   public static boolean isTrue(Attributes var0, String var1) {
      String var2 = var0.getValue(var1);
      return isTrue(var2);
   }

   public static boolean isTrue(String var0) {
      return "true".equals(var0) || "1".equals(var0);
   }

   public static boolean isTrue(Element var0) {
      String var1 = getElementValue(var0);
      return isTrue(var1);
   }

   public static void visitElements(NodeList var0, ElementVisitor var1) {
      int var2 = 0;

      for (int var3 = var0.getLength(); var2 < var3; var2++) {
         Node var4 = var0.item(var2);
         if (var4 instanceof Element) {
            var1.visit((Element)var4);
         }
      }
   }

   public static Element getFirstElementChildOfName(Node var0, String var1) {
      for (Element var3 : Elements.under(var0)) {
         if (var1.equals(var3.getNodeName())) {
            return var3;
         }
      }

      return null;
   }

   public static <V> V getElementValue(Element var0, StringConverter<V> var1) {
      for (Node var3 : NodeListIterator.children(var0)) {
         if (var3 instanceof Text) {
            return var1.apply(var3.getNodeValue());
         }
      }

      return null;
   }

   public static String getElementValue(Element var0) {
      return getElementValue(var0, StringConverter.IDENTITY);
   }

   public static <V> V getChildElementValue(Element var0, String var1, StringConverter<V> var2) {
      NodeList var3 = var0.getElementsByTagName(var1);
      return var3.getLength() > 0 ? getElementValue((Element)var3.item(0), var2) : null;
   }

   public static String getChildElementValue(Element var0, String var1) {
      return getChildElementValue(var0, var1, StringConverter.IDENTITY);
   }

   public static void close(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      }
   }

   private KongaXmlUtils() {
   }
}
