package org.etlforge.integration.json.xml;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.etlforge.xml.XmlNameFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

final class XsdInfo {
   private final Map<String, XsdSimpleType> types = Maps.newHashMap();
   private final Set<String> simpleArrayPaths = Sets.newHashSet();

   public XsdInfo(File var1) throws Exception {
      SAXParser var2 = this.createParser();
      var2.parse(var1, new XsdInfo.SAXHandler());
   }

   private SAXParser createParser() throws ParserConfigurationException, SAXException {
      SAXParserFactory var1 = SAXParserFactory.newInstance();
      var1.setNamespaceAware(true);
      return var1.newSAXParser();
   }

   public XsdSimpleType lookupType(ElementPath var1) {
      String var2 = var1.get();
      XsdSimpleType var3 = this.types.get(var2);
      if (var3 == null) {
         var3 = XsdSimpleType.STRING;
         this.types.put(var2, var3);
      }

      return var3;
   }

   public boolean isSimpleArray(ElementPath var1) {
      return this.simpleArrayPaths.contains(var1.get());
   }

   private class SAXHandler extends DefaultHandler {
      private final ElementPath elements = new ElementPath();
      private final XmlNameFactory nameFactory = XmlNameFactory.nonConforming();

      private SAXHandler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (var2.equals("element")) {
            this.processElementDefinition(var4);
         } else if (var2.equals("attribute")) {
            this.checkSimpleArrayPath(var4);
         }
      }

      private void processElementDefinition(Attributes var1) {
         String var2 = var1.getValue("name");
         this.elements.push(var2);
         String var3 = var1.getValue("type");
         if (var3 != null) {
            this.storeType(var3);
         }
      }

      private void storeType(String var1) {
         String var2 = this.nameFactory.newQualifiedName(var1).getLocalName();
         XsdSimpleType var3 = XsdSimpleType.fromSchemaTypeName(var2);
         if (var3 == null) {
            var3 = XsdSimpleType.STRING;
         }

         String var4 = this.elements.get();
         XsdInfo.this.types.put(var4, var3);
      }

      private void checkSimpleArrayPath(Attributes var1) {
         if ("simple".equals(var1.getValue("name"))) {
            XsdInfo.this.simpleArrayPaths.add(this.elements.get());
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (var2.equals("element")) {
            this.elements.pop();
         }
      }
   }
}
