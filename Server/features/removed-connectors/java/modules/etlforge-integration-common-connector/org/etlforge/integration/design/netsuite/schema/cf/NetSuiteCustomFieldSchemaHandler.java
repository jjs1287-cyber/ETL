package org.etlforge.integration.design.netsuite.schema.cf;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.TreeMap;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldSchema;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetSuiteCustomFieldSchemaHandler extends DefaultHandler {
   private static final String SearchColumnCustomFieldElement = "<element name=\"customLabel\" type=\"xsd:string\" minOccurs=\"0\"/>";
   private static final String _attributeName = "attribute";
   private static final String _qName = "name";
   private static final String _qType = "type";
   private static final String _extension = "extension";
   private static final String _sequence = "sequence";
   private final boolean isSearchRow;
   private final String customFieldTypeName;
   private final String customFieldType;
   private final String customFieldIdName;
   private final String customFieldIdValue;
   private boolean isRoot = true;
   private boolean isNetSuiteIdAttribute = false;
   StringBuilder sb = new StringBuilder();

   NetSuiteCustomFieldSchemaHandler(boolean var1, String var2, ConnectorCustomFieldSchema var3) {
      this.isSearchRow = var1;
      this.customFieldTypeName = var3.getCustomFieldTypeRefName(var2);
      this.customFieldType = var3.getNetsuiteType();
      this.customFieldIdName = var3.getNetSuiteIdName();
      this.customFieldIdValue = var3.getNetsuiteId();
   }

   @Override
   public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
      if (this.isRoot) {
         if (!var3.equals("complexType")) {
            throw new ConnectorNetSuiteDefinitionError("root is not complexType");
         }

         TreeMap var6 = Maps.newTreeMap();
         var6.put("name", this.customFieldTypeName);
         XmlElementUtils.startElementTagAttributeMap(this.sb, var3, var6, null);
         this.isRoot = false;
      } else if (!"extension".equals(var3)) {
         if (!this.isNetSuiteIdAttribute) {
            if ("attribute".equals(var3)) {
               String var5 = var4.getValue("name");
               this.isNetSuiteIdAttribute = "scriptId".equals(var5) || "internalId".equals(var5);
               if (this.isNetSuiteIdAttribute) {
                  return;
               }
            }

            XmlElementUtils.startElementTag(this.sb, var3, var4);
         }
      }
   }

   @Override
   public void endElement(String var1, String var2, String var3) throws SAXException {
      if (this.isNetSuiteIdAttribute) {
         if ("attribute".equals(var3)) {
            this.isNetSuiteIdAttribute = false;
         }
      } else if (!"extension".equals(var3)) {
         boolean var4 = var3.equals("sequence");
         if (this.isSearchRow && var4) {
            this.sb.append("<element name=\"customLabel\" type=\"xsd:string\" minOccurs=\"0\"/>");
         }

         XmlElementUtils.endElementTag(this.sb, var3);
         if (var4) {
            this.appendIdAttribute();
         }
      }
   }

   private void appendIdAttribute() {
      TreeMap var1 = Maps.newTreeMap();
      var1.put("type", "string");
      var1.put("name", this.customFieldIdName);
      var1.put("fixed", this.customFieldIdValue);
      this.appendAttribute(var1);
      var1.put("name", "netsuitetype");
      var1.put("fixed", this.customFieldType);
      this.appendAttribute(var1);
   }

   private void appendAttribute(Map<String, String> var1) {
      XmlElementUtils.startElementTagAttributeMap(this.sb, "attribute", var1, null);
      XmlElementUtils.endElementTag(this.sb, "attribute");
   }
}
