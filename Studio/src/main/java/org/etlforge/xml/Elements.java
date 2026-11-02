package org.etlforge.xml;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Elements implements Iterable<Element>, NodeList {
   private final TypedNodeList<Element> impl;

   public static Elements under(Node var0) {
      return new Elements(var0.getChildNodes());
   }

   public static Elements childElementsWithName(Element var0, String var1) {
      return new Elements(var0.getElementsByTagName(var1));
   }

   public static Elements matching(String var0, Node var1) {
      try {
         XPath var2 = newXPath();
         return evaluateXPath(var0, var1, var2);
      } catch (XPathExpressionException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static Elements matching(String var0, Node var1, NamespaceContext var2) {
      try {
         XPath var3 = newXPath();
         var3.setNamespaceContext(var2);
         return evaluateXPath(var0, var1, var3);
      } catch (XPathExpressionException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static XPath newXPath() {
      return XPathFactory.newInstance().newXPath();
   }

   private static Elements evaluateXPath(String var0, Node var1, XPath var2) throws XPathExpressionException {
      NodeList var3 = (NodeList)var2.evaluate(var0, var1, XPathConstants.NODESET);
      return new Elements(var3);
   }

   public static Element firstMatch(String var0, Node var1) {
      Elements var2 = matching(var0, var1);
      return var2.getLength() > 0 ? var2.item(0) : null;
   }

   public static Element firstMatch(String var0, Node var1, NamespaceContext var2) {
      Elements var3 = matching(var0, var1, var2);
      return var3.getLength() > 0 ? var3.item(0) : null;
   }

   public Elements(NodeList var1) {
      this.impl = TypedNodeList.of(var1, Element.class);
   }

   @Override
   public Iterator<Element> iterator() {
      return this.impl.iterator();
   }

   @Override
   public int getLength() {
      return this.impl.getLength();
   }

   public Element item(int var1) {
      return this.impl.item(var1);
   }

   public void visit(ElementVisitor var1) {
      for (Element var3 : this) {
         var1.visit(var3);
      }
   }
}
