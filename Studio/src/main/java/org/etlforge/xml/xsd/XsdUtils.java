package org.etlforge.xml.xsd;

import org.w3c.dom.Element;

public final class XsdUtils {
   public static void makeAttributeFixedAndRequired(Element var0) {
      var0.setAttribute("fixed", "true");
      var0.setAttribute("use", "required");
   }

   private XsdUtils() {
   }
}
