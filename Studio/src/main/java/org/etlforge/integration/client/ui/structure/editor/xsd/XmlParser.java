package org.etlforge.integration.client.ui.structure.editor.xsd;

import java.io.File;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

final class XmlParser {
   private final File xmlFile;
   private ExpandedName rootName;

   public XmlParser(File var1) {
      this.xmlFile = var1;
   }

   public ExpandedName validate() throws Exception {
      SAXParser var1 = this.createSaxParser();
      var1.parse(this.xmlFile, new DefaultHandler() {
         @Override
         public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
            if (XmlParser.this.rootName == null) {
               XmlNameFactory var5 = XmlNameFactory.conforming();
               XmlParser.this.rootName = var5.newExpandedName(var1, var2);
            }
         }
      });
      return this.rootName;
   }

   private SAXParser createSaxParser() throws ParserConfigurationException, SAXException {
      SAXParserFactory var1 = SAXParserFactory.newInstance();
      var1.setNamespaceAware(true);
      var1.setValidating(true);
      return var1.newSAXParser();
   }
}
