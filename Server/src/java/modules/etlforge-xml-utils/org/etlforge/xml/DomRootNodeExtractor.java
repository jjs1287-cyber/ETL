package org.etlforge.xml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

final class DomRootNodeExtractor extends RootNodeExtractor {
   @Override
   public Element extractRoot(File var1) throws KongaXmlException {
      try {
         DocumentBuilder var2 = this.getDomBuilder();
         Document var3 = var2.parse(var1);
         return var3.getDocumentElement();
      } catch (SAXException var4) {
         throw new KongaXmlException("Failed to parse the specified XML file.", var4);
      } catch (IOException var5) {
         throw new KongaXmlException("Failed to read the specified XML file.", var5);
      }
   }

   @Override
   public Element extractRoot(Reader var1) throws KongaXmlException {
      try {
         DocumentBuilder var2 = this.getDomBuilder();
         InputSource var3 = new InputSource(var1);
         Document var4 = var2.parse(var3);
         return var4.getDocumentElement();
      } catch (SAXException var5) {
         throw new KongaXmlException("Failed to parse the specified XML.", var5);
      } catch (IOException var6) {
         throw new KongaXmlException("Failed to read the specified XML.", var6);
      }
   }

   private DocumentBuilder getDomBuilder() throws KongaXmlException {
      try {
         DocumentBuilderFactory var1 = this.getDomBuilderFactory();
         return var1.newDocumentBuilder();
      } catch (ParserConfigurationException var2) {
         throw new KongaXmlException("Failed to create the DOM parser.", var2);
      }
   }

   private DocumentBuilderFactory getDomBuilderFactory() {
      DocumentBuilderFactory var1 = DocumentBuilderFactory.newInstance();
      var1.setNamespaceAware(true);
      return var1;
   }
}
