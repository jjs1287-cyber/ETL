package org.etlforge.integration.structure.mapping;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.xml.XmlStringBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class JtrForHumansConverter {
   public void convert(MappingTreeStructure var1, File var2) {
      try {
         SAXParser var3 = SAXParserFactory.newInstance().newSAXParser();
         JtrForHumansConverter.StringIndexMapReader var4 = new JtrForHumansConverter.StringIndexMapReader();
         var3.parse(new InputSource(var1.getReader()), var4);
         JtrForHumansConverter.JtrReader var5 = new JtrForHumansConverter.JtrReader(var4.stringMap);
         var3.parse(new InputSource(var1.getReader()), var5);
         this.writeOutput(var5.outputXml, var2);
      } catch (ParserConfigurationException var6) {
         var6.printStackTrace();
      } catch (SAXException var7) {
         var7.printStackTrace();
      } catch (IOException var8) {
         var8.printStackTrace();
      }
   }

   private void writeOutput(XmlStringBuilder var1, File var2) {
      try {
         KongaIoUtils.write(var2, var1.getXml(), Charsets.UTF_8);
      } catch (IOException var4) {
         var4.printStackTrace();
      }
   }

   private static class JtrReader extends DefaultHandler {
      private final XmlStringBuilder outputXml = new XmlStringBuilder();
      private final Map<String, String> stringMap;

      public JtrReader(Map<String, String> var1) {
         this.stringMap = var1;
      }

      @Override
      public void endElement(String var1, String var2, String var3) {
         this.outputXml.closeElement();
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) {
         this.outputXml.addElement(var3);
         if (var3.equals("ElementType")) {
            this.handleElementType(var4);
         } else if (var3.equals("Extensions")) {
            this.handleTypeExtensions(var4);
         } else if (var3.equals("Substitutions")) {
            this.handleSubstitutionGroups(var4);
         } else if (var3.equals("CROM")) {
            this.handleCromElement(var4);
         } else if (var3.equals("Xml")) {
            this.handleCromXmlElement(var4);
         } else if (var3.equals("Attr")) {
            this.handleCromAttrElement(var4);
         } else {
            this.writeAttributesToOutput(var4);
         }
      }

      private void handleCromAttrElement(Attributes var1) {
         this.addAttributeToOutput(var1, "NSQual");
         this.addAttributeToOutput(var1, "FieldPos");
         this.addAttributeToOutput(var1, "FieldLength");
         this.addAttributeToOutput(var1, "DbColType");
         this.addAttributeToOutput(var1, "DbColSize");
         this.addAttributeToOutput(var1, "DbDecDigits");
         this.addAttributeToOutput(var1, "Value");
         this.addIndexedAttributeToOutput(var1, "ValType");
         this.addAttributeToOutput(var1, "DefVal");
         this.addAttributeToOutput(var1, "Format");
         this.addAttributeToOutput(var1, "Domain");
         this.addAttributeToOutput(var1, "LinkToPrimaryKey");
         this.addIndexedAttributeToOutput(var1, "NS");
         this.addIndexedAttributeToOutput(var1, "Type");
         this.addIndexedAttributeToOutput(var1, "TypeNS");
      }

      private void handleCromXmlElement(Attributes var1) {
         this.addAttributeToOutput(var1, "IsAbstract");
         this.addAttributeToOutput(var1, "NSQual");
         this.addAttributeToOutput(var1, "Min");
         this.addAttributeToOutput(var1, "Max");
         this.addIndexedAttributeToOutput(var1, "ArrayType");
         this.addIndexedAttributeToOutput(var1, "NS");
         this.addIndexedAttributeToOutput(var1, "Type");
         this.addIndexedAttributeToOutput(var1, "TypeNS");
         this.addIndexedAttributeToOutput(var1, "AnyNS");
      }

      private void handleCromElement(Attributes var1) {
         this.addAttributeToOutput(var1, "Type");
         this.addAttributeToOutput(var1, "Name");
         this.addIndexedAttributeToOutput(var1, "Display");
         this.addAttributeToOutput(var1, "Comment");
      }

      private void handleSubstitutionGroups(Attributes var1) {
         String var2 = var1.getValue("Names");
         this.writeSubstitutionGroupsToOutputXml(var2);
      }

      private void handleTypeExtensions(Attributes var1) {
         String var2 = var1.getValue("Types");
         this.writeTypeExtensionsToOutputXml(var2);
      }

      private void handleElementType(Attributes var1) {
         this.addIndexedAttributeToOutput(var1, "Name");
      }

      private void writeTypeExtensionsToOutputXml(String var1) {
         this.writeTypeOrGroupsToOutputXml(var1, "Extension");
      }

      private void writeSubstitutionGroupsToOutputXml(String var1) {
         this.writeTypeOrGroupsToOutputXml(var1, "Group");
      }

      private void writeTypeOrGroupsToOutputXml(String var1, String var2) {
         if (var1 != null) {
            for (String var6 : var1.split(",")) {
               String var7 = this.stringMap.get(var6);
               if (var7 != null) {
                  this.outputXml.addElement(var2);
                  this.outputXml.addAttribute("Name", var7);
                  this.outputXml.closeElement();
               }
            }
         }
      }

      private void writeAttributesToOutput(Attributes var1) {
         int var2 = 0;

         for (int var3 = var1.getLength(); var2 < var3; var2++) {
            String var4 = var1.getQName(var2);
            String var5 = var1.getValue(var2);
            this.outputXml.addAttribute(var4, var5);
         }
      }

      private void addAttributeToOutput(Attributes var1, String var2) {
         String var3 = var1.getValue(var2);
         if (var3 != null) {
            this.outputXml.addAttribute(var2, var3);
         }
      }

      private void addIndexedAttributeToOutput(Attributes var1, String var2) {
         String var3 = var1.getValue(var2);
         if (var3 != null) {
            String var4 = this.stringMap.get(var3);
            if (var4 != null) {
               this.outputXml.addAttribute(var2, var4);
            }
         }
      }
   }

   private static class StringIndexMapReader extends DefaultHandler {
      private final Map<String, String> stringMap = Maps.newHashMap();

      public StringIndexMapReader() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) {
         if (var3.equals("String")) {
            this.stringMap.put(var4.getValue("idx"), var4.getValue("val"));
         }
      }
   }
}
