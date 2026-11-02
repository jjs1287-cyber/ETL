package org.etlforge.integration.json.xml;

import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.lang.StringUtils;
import org.etlforge.xml.Elements;
import org.etlforge.xml.XmlFileWriter;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

final class TypeEnforcer {
   private Map<String, String> knownTypes;

   public void enforceTypes(File var1, File var2) throws XsdGeneratorException, IOException {
      this.knownTypes = Maps.newHashMap();
      this.collectPathsToKnownTypes(var1);
      this.updateXsdIfNecessary(var2);
   }

   private void collectPathsToKnownTypes(File var1) throws XsdGeneratorException {
      try {
         SAXParserFactory var2 = SAXParserFactory.newInstance();
         var2.setValidating(true);
         var2.setNamespaceAware(true);
         SAXParser var3 = var2.newSAXParser();
         TypeEnforcer.KnownStringsPathCollector var4 = new TypeEnforcer.KnownStringsPathCollector();
         var3.parse(var1, var4);
      } catch (ParserConfigurationException var5) {
         throw new XsdGeneratorException(var5);
      } catch (SAXException var6) {
         throw new XsdGeneratorException(var6);
      } catch (IOException var7) {
         throw new XsdGeneratorException(var7);
      }
   }

   private void updateXsdIfNecessary(File var1) throws XsdGeneratorException, IOException {
      if (!this.knownTypes.isEmpty()) {
         TypeEnforcer.XsdChanger var2 = new TypeEnforcer.XsdChanger(this.knownTypes);
         var2.checkXsd(var1);
      }
   }

   private class KnownStringsPathCollector extends DefaultHandler {
      private final ElementPath path = new ElementPath();

      private KnownStringsPathCollector() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.path.push(var2);
         String var5 = var4.getValue("xsi:type");
         if (StringUtils.isNotEmpty(var5)) {
            TypeEnforcer.this.knownTypes.put(this.path.get(), var5);
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         this.path.pop();
      }
   }

   private static class XsdChanger {
      private final Map<String, String> elementTypes;
      private boolean changed;
      private ElementPath elements;

      private XsdChanger(Map<String, String> var1) {
         this.elementTypes = var1;
      }

      public void checkXsd(File var1) throws XsdGeneratorException, IOException {
         Document var2 = this.init(var1);
         this.traverse(var2.getDocumentElement());
         this.save(var2, var1);
      }

      private Document init(File var1) throws XsdGeneratorException {
         try {
            this.changed = false;
            this.elements = new ElementPath();
            return DomParser.parseFile(var1, true);
         } catch (Exception var3) {
            throw new XsdGeneratorException(var3);
         }
      }

      private void traverse(Element var1) {
         boolean var2 = "element".equals(var1.getLocalName());
         if (var2) {
            this.elements.push(var1.getAttribute("name"));
            this.checkElement(var1);
         }

         for (Element var4 : Elements.under(var1)) {
            this.traverse(var4);
         }

         if (var2) {
            this.elements.pop();
         }
      }

      private void checkElement(Element var1) throws DOMException {
         String var2 = this.elements.get();
         String var3 = this.elementTypes.get(var2);
         if (var3 != null) {
            String var4 = var1.getAttribute("type");
            if (!var3.equals(var4)) {
               this.changed = true;
            }

            var1.setAttribute("type", var3);
         }
      }

      private void save(Document var1, File var2) throws IOException {
         if (this.changed) {
            XmlFileWriter var3 = new XmlFileWriter();
            var3.write(var1, var2);
         }
      }
   }
}
