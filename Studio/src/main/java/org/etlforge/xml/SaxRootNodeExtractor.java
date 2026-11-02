package org.etlforge.xml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

final class SaxRootNodeExtractor extends RootNodeExtractor {
   @Override
   public Element extractRoot(File var1) throws KongaXmlException {
      try {
         SaxRootNodeExtractor.Handler var2 = new SaxRootNodeExtractor.Handler();
         this.parse(var1, var2);
         return var2.getRoot();
      } catch (SAXException var3) {
         throw new KongaXmlException("Failed to parse the specified XML file.", var3);
      } catch (IOException var4) {
         throw new KongaXmlException("Failed to read the specified XML file.", var4);
      }
   }

   private void parse(File var1, SaxRootNodeExtractor.Handler var2) throws KongaXmlException, SAXException, IOException {
      try {
         SAXParser var3 = this.getSaxParser();
         var3.parse(var1, var2);
      } catch (SaxRootNodeExtractor.StopException var4) {
      }
   }

   private SAXParser getSaxParser() throws KongaXmlException {
      try {
         SAXParserFactory var1 = this.getParserFactory();
         return var1.newSAXParser();
      } catch (Exception var2) {
         throw new KongaXmlException("Failed to create the SAX parser.", var2);
      }
   }

   @Override
   public Element extractRoot(Reader var1) throws KongaXmlException {
      try {
         SaxRootNodeExtractor.Handler var2 = new SaxRootNodeExtractor.Handler();
         this.parse(var1, var2);
         return var2.getRoot();
      } catch (SAXException var3) {
         throw new KongaXmlException("Failed to parse the specified XML.", var3);
      } catch (IOException var4) {
         throw new KongaXmlException("Failed to read the specified XML.", var4);
      }
   }

   private void parse(Reader var1, SaxRootNodeExtractor.Handler var2) throws KongaXmlException, SAXException, IOException {
      try {
         SAXParser var3 = this.getSaxParser();
         InputSource var4 = new InputSource(var1);
         var3.parse(var4, var2);
      } catch (SaxRootNodeExtractor.StopException var5) {
      }
   }

   private SAXParserFactory getParserFactory() {
      SAXParserFactory var1 = SAXParserFactory.newInstance();
      var1.setNamespaceAware(true);
      return var1;
   }

   private static final class ElementImpl implements Element {
      private final String namespaceUri;
      private final String tagName;
      private final String prefix;
      private final String localName;
      private final Attributes attributes;

      public ElementImpl(String var1, String var2, String var3, Attributes var4) {
         this.namespaceUri = var1;
         this.tagName = var2;
         this.prefix = StringUtils.substringBefore(var2, ":");
         this.localName = var3;
         this.attributes = var4;
      }

      @Override
      public String getAttribute(String var1) {
         return this.attributes.getValue(var1);
      }

      @Override
      public boolean hasAttribute(String var1) {
         return this.attributes.getValue(var1) != null;
      }

      @Override
      public String getLocalName() {
         return this.localName;
      }

      @Override
      public String getNamespaceURI() {
         return this.namespaceUri;
      }

      @Override
      public boolean hasAttributes() {
         return this.attributes.getLength() > 0;
      }

      @Override
      public short getNodeType() {
         return 1;
      }

      @Override
      public String getPrefix() {
         return this.prefix;
      }

      @Override
      public String getTagName() {
         return this.tagName;
      }

      @Override
      public String getNodeName() {
         return this.tagName;
      }

      @Override
      public String getNodeValue() {
         return null;
      }

      @Override
      public Attr getAttributeNode(String var1) {
         return null;
      }

      @Override
      public Attr getAttributeNodeNS(String var1, String var2) {
         return null;
      }

      @Override
      public String getAttributeNS(String var1, String var2) {
         return null;
      }

      @Override
      public NodeList getElementsByTagName(String var1) {
         return null;
      }

      @Override
      public NodeList getElementsByTagNameNS(String var1, String var2) {
         return null;
      }

      @Override
      public TypeInfo getSchemaTypeInfo() {
         return null;
      }

      @Override
      public boolean hasAttributeNS(String var1, String var2) {
         return false;
      }

      @Override
      public void removeAttribute(String var1) {
      }

      @Override
      public Attr removeAttributeNode(Attr var1) {
         return null;
      }

      @Override
      public void removeAttributeNS(String var1, String var2) {
      }

      @Override
      public void setAttribute(String var1, String var2) {
      }

      @Override
      public Attr setAttributeNode(Attr var1) {
         return null;
      }

      @Override
      public Attr setAttributeNodeNS(Attr var1) {
         return null;
      }

      @Override
      public void setAttributeNS(String var1, String var2, String var3) {
      }

      @Override
      public void setIdAttribute(String var1, boolean var2) {
      }

      @Override
      public void setIdAttributeNode(Attr var1, boolean var2) {
      }

      @Override
      public void setIdAttributeNS(String var1, String var2, boolean var3) {
      }

      @Override
      public Node appendChild(Node var1) {
         return null;
      }

      @Override
      public Node cloneNode(boolean var1) {
         return null;
      }

      @Override
      public short compareDocumentPosition(Node var1) {
         return 0;
      }

      @Override
      public NamedNodeMap getAttributes() {
         return null;
      }

      @Override
      public String getBaseURI() {
         return null;
      }

      @Override
      public NodeList getChildNodes() {
         return null;
      }

      @Override
      public Object getFeature(String var1, String var2) {
         return null;
      }

      @Override
      public Node getFirstChild() {
         return null;
      }

      @Override
      public Node getLastChild() {
         return null;
      }

      @Override
      public Node getNextSibling() {
         return null;
      }

      @Override
      public Document getOwnerDocument() {
         return null;
      }

      @Override
      public Node getParentNode() {
         return null;
      }

      @Override
      public Node getPreviousSibling() {
         return null;
      }

      @Override
      public String getTextContent() {
         return null;
      }

      @Override
      public Object getUserData(String var1) {
         return null;
      }

      @Override
      public boolean hasChildNodes() {
         return false;
      }

      @Override
      public Node insertBefore(Node var1, Node var2) {
         return null;
      }

      @Override
      public boolean isDefaultNamespace(String var1) {
         return false;
      }

      @Override
      public boolean isEqualNode(Node var1) {
         return false;
      }

      @Override
      public boolean isSameNode(Node var1) {
         return false;
      }

      @Override
      public boolean isSupported(String var1, String var2) {
         return false;
      }

      @Override
      public String lookupNamespaceURI(String var1) {
         return null;
      }

      @Override
      public String lookupPrefix(String var1) {
         return null;
      }

      @Override
      public void normalize() {
      }

      @Override
      public Node removeChild(Node var1) {
         return null;
      }

      @Override
      public Node replaceChild(Node var1, Node var2) {
         return null;
      }

      @Override
      public void setNodeValue(String var1) {
      }

      @Override
      public void setPrefix(String var1) {
      }

      @Override
      public void setTextContent(String var1) {
      }

      @Override
      public Object setUserData(String var1, Object var2, UserDataHandler var3) {
         return null;
      }
   }

   private static final class Handler extends DefaultHandler {
      private Element root;

      private Handler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) {
         if (this.root == null) {
            this.root = new SaxRootNodeExtractor.ElementImpl(var1, var3, var2, var4);
            throw new SaxRootNodeExtractor.StopException();
         }
      }

      public Element getRoot() {
         return this.root;
      }
   }

   private static class StopException extends RuntimeException {
      private StopException() {
      }
   }
}
