package org.etlforge.integration.json.xml;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.xsd.SchemaDomBuilder;
import org.etlforge.xml.xsd.XsdUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

final class ArrayTweaker {
   private static final String ARRAY_ATTRIBUTE_XPATH = "//xs:attribute[@name='array']";
   private static final String SIMPLE_ARRAY_ATTRIBUTE_XPATH = "//xs:attribute[@name='simple']";
   private final Document schema;
   private final NamespaceContext nsCtx;

   public ArrayTweaker(Document var1, NamespaceContext var2) {
      this.schema = var1;
      this.nsCtx = var2;
   }

   public void tweakArrays() {
      for (Element var2 : this.findElements("//xs:attribute[@name='array']")) {
         this.processArrayAttribute(var2);
      }

      for (Element var4 : this.findElements("//xs:attribute[@name='simple']")) {
         XsdUtils.makeAttributeFixedAndRequired(var4);
      }
   }

   private void processArrayAttribute(Element var1) throws DOMException {
      Element var2 = (Element)var1.getParentNode();
      String var3 = var2.getLocalName();
      if ("extension".equals(var3)) {
         this.correctAlwaysEmptyArray(var2);
      } else if ("complexType".equals(var3)) {
         this.makeArrayItemsOptionalLoops(var2);
         XsdUtils.makeAttributeFixedAndRequired(var1);
         var2.removeAttribute("mixed");
      }
   }

   private void correctAlwaysEmptyArray(Element var1) {
      Element var2 = this.locateAndStripComplexType(var1);
      SchemaDomBuilder var3 = new SchemaDomBuilder(this.schema);
      var3.setSchemaPrefix("xs");
      Element var4 = var3.sequence();
      Element var5 = var3.optionalUnboundedElement("item", "xs:string", true);
      var4.appendChild(var5);
      var2.appendChild(var4);
      Element var6 = var3.requiredFixedAttribute("array", "true", "xs:string");
      var2.appendChild(var6);
   }

   private Element locateAndStripComplexType(Element var1) throws DOMException {
      Node var2 = var1.getParentNode();
      Element var3 = (Element)var2.getParentNode();
      var3.removeChild(var2);
      KongaXmlUtils.removeWhitespace(var3);
      return var3;
   }

   private void makeArrayItemsOptionalLoops(Element var1) {
      Element var2 = this.findFirstElementDescendant(var1);
      if (var2 != null) {
         var2.setAttribute("minOccurs", "0");
         var2.setAttribute("maxOccurs", "unbounded");
      }
   }

   private Elements findElements(String var1) {
      return Elements.matching(var1, this.schema, this.nsCtx);
   }

   private Element findFirstElementDescendant(Element var1) {
      Iterator var2 = Elements.under(var1).iterator();
      if (var2.hasNext()) {
         Element var3 = (Element)var2.next();
         return "element".equals(var3.getLocalName()) ? var3 : this.findFirstElementDescendant(var3);
      } else {
         return null;
      }
   }
}
