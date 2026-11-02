package org.etlforge.integration.design.netsuite.schema.cf;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.TreeMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.cf.ConnectorCustomFieldSchema;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlSchemaSnippetAssumption;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetSuiteCustomFieldsSchemaTransformer {
   private final List<ConnectorCustomFieldSchema> customFields;
   private final String schemaXml;
   private final String objectTypeName;

   NetSuiteCustomFieldsSchemaTransformer(String var1, String var2, List<ConnectorCustomFieldSchema> var3) {
      this.customFields = var3;
      this.schemaXml = var2;
      this.objectTypeName = var1;
   }

   public String getModifiedSchema() {
      NetSuiteWsdlSchemaSnippetAssumption.validate(this.schemaXml);
      NetSuiteCustomFieldsSchemaTransformer.Handler var1 = new NetSuiteCustomFieldsSchemaTransformer.Handler();

      try {
         SaxParsers.parseString(this.schemaXml, var1, false);
         return var1.sb.toString();
      } catch (SAXException var3) {
         throw new ConnectorNetSuiteDefinitionError(var3);
      }
   }

   private class Handler extends DefaultHandler {
      private boolean isRoot = true;
      StringBuilder sb = new StringBuilder();
      private static final String _elementName = "element";
      private static final String _qName = "name";
      private static final String _qType = "type";

      private Handler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.isRoot) {
            if (!var3.equals("complexType")) {
               throw new ConnectorNetSuiteDefinitionError("root is not complexType");
            }

            TreeMap var5 = Maps.newTreeMap();
            var5.put("name", NetSuiteCustomFieldListComplexTypeName.getNewLocalName(NetSuiteCustomFieldsSchemaTransformer.this.objectTypeName));
            XmlElementUtils.startElementTagAttributeMap(this.sb, var3, var5, null);
            this.isRoot = false;
         } else if ("element".equals(var3)) {
            XmlQName var9 = new XmlQName(var4.getValue("type"));

            for (ConnectorCustomFieldSchema var7 : NetSuiteCustomFieldsSchemaTransformer.this.customFields) {
               TreeMap var8 = Maps.newTreeMap();
               var8.put("name", var7.getCustomFieldXmlName());
               var8.put("type", var9.getQNameFromLocalName(var7.getCustomFieldTypeRefName(NetSuiteCustomFieldsSchemaTransformer.this.objectTypeName)));
               var8.put("minOccurs", "0");
               XmlElementUtils.startElementTagAttributeMap(this.sb, var3, var8, null);
               XmlElementUtils.endElementTag(this.sb, var3);
            }
         } else {
            XmlElementUtils.startElementTag(this.sb, var3, var4);
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (!"element".equals(var3)) {
            XmlElementUtils.endElementTag(this.sb, var3);
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         String var4 = new String(var1, var2, var3);
         this.sb.append(StringEscapeUtils.escapeXml(var4));
      }
   }
}
