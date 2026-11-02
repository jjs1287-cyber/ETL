package org.etlforge.util.persist;

import com.google.common.base.Charsets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.XmlFileWriter;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class XmlPersistor implements FilePersistor {
   private static final String TEXT_ELEMENT_NAME = "konga.string";
   private final Document document;
   private final Element element;

   public static XmlPersistor newRoot(String var0) {
      try {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         Document var3 = var2.newDocument();
         Element var4 = var3.createElement(KeyValidator.checkKey(var0));
         var3.appendChild(var4);
         return new XmlPersistor(var3, var4);
      } catch (ParserConfigurationException var5) {
         throw new RuntimeException(var5);
      }
   }

   public static XmlPersistor load(File var0) throws IOException, SAXException {
      try {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         Document var3 = var2.parse(var0);
         return load(var3);
      } catch (ParserConfigurationException var4) {
         throw new RuntimeException(var4);
      }
   }

   public static XmlPersistor load(String var0) throws IOException, SAXException {
      try {
         DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
         DocumentBuilder var2 = var1.newDocumentBuilder();
         InputSource var3 = new InputSource(new StringReader(var0));
         Document var4 = var2.parse(var3);
         return load(var4);
      } catch (ParserConfigurationException var5) {
         throw new RuntimeException(var5);
      }
   }

   public static XmlPersistor load(Document var0) {
      Element var1 = var0.getDocumentElement();
      return new XmlPersistor(var0, var1);
   }

   private XmlPersistor(Document var1, Element var2) {
      this.document = var1;
      this.element = var2;
   }

   @Override
   public Persistor createChild(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("name cannot be null");
      }

      String var2 = var1.trim();
      if (var2.length() == 0) {
         throw new IllegalArgumentException("name cannot be blank");
      }

      if (var2.startsWith("konga.")) {
         throw new IllegalArgumentException("name cannot start with konga.");
      }

      Element var3 = this.document.createElement(var2);
      this.element.appendChild(var3);
      return new XmlPersistor(this.document, var3);
   }

   @Override
   public Iterable<Persistor> getAllChildren() {
      ArrayList var1 = new ArrayList();
      NodeList var2 = this.element.getChildNodes();
      int var3 = 0;

      for (int var4 = var2.getLength(); var3 < var4; var3++) {
         Node var5 = var2.item(var3);
         if (var5 instanceof Element && !var5.getNodeName().startsWith("konga.")) {
            var1.add(new XmlPersistor(this.document, (Element)var5));
         }
      }

      return var1;
   }

   @Override
   public Persistor getFirstChild(String var1) {
      Iterator var2 = this.getChildren(var1).iterator();
      return var2.hasNext() ? (Persistor)var2.next() : null;
   }

   @Override
   public Iterable<Persistor> getChildren(String var1) {
      ArrayList var2 = new ArrayList();
      NodeList var3 = this.element.getChildNodes();
      int var4 = 0;

      for (int var5 = var3.getLength(); var4 < var5; var4++) {
         Node var6 = var3.item(var4);
         if (var6 instanceof Element && var6.getNodeName().equals(var1)) {
            var2.add(new XmlPersistor(this.document, (Element)var6));
         }
      }

      return var2;
   }

   @Override
   public void removeAllChildren() {
      this.removeChildren(null);
   }

   @Override
   public void removeChildren(String var1) {
      for (Element var3 : this.getChildElements(var1)) {
         this.element.removeChild(var3);
      }
   }

   private List<Element> getChildElements(String var1) {
      ArrayList var2 = new ArrayList();
      NodeList var3 = this.element.getChildNodes();
      int var4 = 0;

      for (int var5 = var3.getLength(); var4 < var5; var4++) {
         Node var6 = var3.item(var4);
         if (var6 instanceof Element) {
            Element var7 = (Element)var6;
            String var8 = var7.getNodeName();
            if (!"konga.string".equals(var8) && (var1 == null || var1.equals(var8))) {
               var2.add(var7);
            }
         }
      }

      return var2;
   }

   @Override
   public Double getDouble(String var1) {
      String var2 = this.checkKeyRelaxed(var1);
      if (this.element.hasAttribute(var2)) {
         try {
            return Double.valueOf(this.element.getAttribute(var2));
         } catch (NumberFormatException var4) {
         }
      }

      return null;
   }

   @Override
   public Long getLong(String var1) {
      String var2 = this.checkKeyRelaxed(var1);
      if (this.element.hasAttribute(var2)) {
         try {
            return Long.valueOf(this.element.getAttribute(var2));
         } catch (NumberFormatException var4) {
         }
      }

      return null;
   }

   @Override
   public String getName() {
      return this.element.getNodeName();
   }

   @Override
   public String getString(String var1) {
      String var2 = this.checkKeyRelaxed(var1);
      return this.element.hasAttribute(var2) ? this.element.getAttribute(var2) : this.getText(var1);
   }

   @Override
   public String getText(String var1) {
      Text var2 = this.getTextNode(var1);
      return var2 != null ? var2.getNodeValue() : null;
   }

   @Override
   public Boolean getBoolean(String var1) {
      String var2 = this.checkKeyRelaxed(var1);
      if (this.element.hasAttribute(var2)) {
         try {
            return Boolean.valueOf(this.element.getAttribute(var2));
         } catch (NumberFormatException var4) {
         }
      }

      return null;
   }

   @Override
   public Integer getInteger(String var1) {
      String var2 = this.checkKeyRelaxed(var1);
      if (this.element.hasAttribute(var2)) {
         try {
            return Integer.valueOf(this.element.getAttribute(var2));
         } catch (NumberFormatException var4) {
         }
      }

      return null;
   }

   @Override
   public void putDouble(String var1, double var2) {
      this.setAttributeImpl(this.checkKey(var1), String.valueOf(var2));
   }

   @Override
   public void putLong(String var1, long var2) {
      this.setAttributeImpl(this.checkKey(var1), String.valueOf(var2));
   }

   @Override
   public void putText(String var1, String var2) {
      String var3 = this.checkKey(var1);
      this.element.removeAttribute(var3);
      Text var4 = this.getTextNode(var3);
      if (var2 != null) {
         if (var4 == null) {
            Element var5 = this.document.createElement("konga.string");
            var5.setAttribute("name", var3);
            var4 = this.document.createTextNode(var2);
            var5.appendChild(var4);
            this.element.appendChild(var5);
         } else {
            var4.setNodeValue(var2);
         }
      } else if (var4 != null) {
         this.element.removeChild(var4.getParentNode());
      }
   }

   @Override
   public void putString(String var1, String var2) {
      this.setAttributeImpl(this.checkKey(var1), var2);
   }

   @Override
   public void putBoolean(String var1, boolean var2) {
      this.setAttributeImpl(this.checkKey(var1), String.valueOf(var2));
   }

   @Override
   public void putInteger(String var1, int var2) {
      this.setAttributeImpl(this.checkKey(var1), String.valueOf(var2));
   }

   @Override
   public void store(File var1) throws IOException {
      OutputStreamWriter var2 = new OutputStreamWriter(new FileOutputStream(var1), Charsets.UTF_8);

      try {
         this.store(var2);
      } finally {
         var2.close();
      }
   }

   public void store(Writer var1) {
      this.removeWhitespace();
      XmlFileWriter var2 = new XmlFileWriter();
      var2.write(this.document, var1);
   }

   private void removeWhitespace() {
      KongaXmlUtils.removeWhitespace(this.document.getDocumentElement());
   }

   public String toXml() {
      this.removeWhitespace();
      StringWriter var1 = new StringWriter();
      this.store(var1);
      return var1.toString();
   }

   public void printToConsole() {
      System.out.println(this.toXml());
   }

   public void exportTo(XmlPersistor var1) {
      if (var1 == null) {
         throw new NullPointerException("target");
      }

                                                                            
      org.w3c.dom.NamedNodeMap attributes = this.element.getAttributes();
      for (int i = 0; i < attributes.getLength(); i++) {
         Node attribute = attributes.item(i);
         var1.element.setAttribute(attribute.getNodeName(), attribute.getNodeValue());
      }

      NodeList var2 = this.element.getChildNodes();

      for (int var3 = 0; var3 < var2.getLength(); var3++) {
         Node var4 = var2.item(var3);
         Node var5 = var4.cloneNode(true);

         try {
            var1.element.appendChild(var1.document.adoptNode(var5));
         } catch (DOMException var7) {
            var7.printStackTrace();
         }
      }
   }

   private void setAttributeImpl(String var1, String var2) {
      this.removeTextNode(var1);
      this.element.setAttribute(var1, var2);
   }

   private void removeTextNode(String var1) {
      Text var2 = this.getTextNode(var1);
      if (var2 != null) {
         this.element.removeChild(var2.getParentNode());
      }
   }

   private Text getTextNode(String var1) {
      if (var1 == null) {
         return null;
      }

      NodeList var2 = this.element.getChildNodes();
      int var3 = 0;

      for (int var4 = var2.getLength(); var3 < var4; var3++) {
         Node var5 = var2.item(var3);
         if (var5 instanceof Element) {
            Element var6 = (Element)var5;
            if (var6.getNodeName().equals("konga.string") && var1.equals(var6.getAttribute("name"))) {
               return (Text)var6.getFirstChild();
            }
         }
      }

      return null;
   }

   private String checkKeyRelaxed(String var1) {
      return KeyValidator.checkKeyRelaxed(var1);
   }

   private String checkKey(String var1) throws IllegalArgumentException {
      return KeyValidator.checkKey(var1);
   }
}
