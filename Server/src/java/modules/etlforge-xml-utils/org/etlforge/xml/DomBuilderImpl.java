package org.etlforge.xml;

import java.io.Writer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

class DomBuilderImpl implements DomBuilder {
   private final Document document;
   private final Element element;

   public DomBuilderImpl(Document var1, String var2) {
      this(var1, var2, var1);
   }

   private DomBuilderImpl(Document var1, String var2, Node var3) {
      try {
         this.document = var1;
         this.element = var1.createElement(var2);
         var3.appendChild(this.element);
      } catch (DOMException var5) {
         throw new DomBuilderException(var5);
      }
   }

   @Override
   public void addAttribute(String var1, String var2) {
      try {
         this.element.setAttribute(var1, var2);
      } catch (DOMException var4) {
         throw new DomBuilderException(var4);
      }
   }

   @Override
   public DomBuilder addChild(String var1) {
      return new DomBuilderImpl(this.document, var1, this.element);
   }

   @Override
   public void addValue(String var1) {
      try {
         Text var2 = this.document.createTextNode(var1);
         this.element.appendChild(var2);
      } catch (DOMException var3) {
         throw new DomBuilderException(var3);
      }
   }

   @Override
   public void writeXml(Writer var1) {
      new XmlFileWriter().write(this.document, var1);
   }

   @Override
   public Document getDocument() {
      return this.document;
   }
}
