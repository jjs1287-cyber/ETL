package org.etlforge.integration.design.netsuite.schema.cf;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.definition.NetSuiteWsdlSchemaSnippetAssumption;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetSuiteCustomFieldObjectSchemaTransformer {
   private final String objectName;
   private final String schemaXml;
   private final String customObjectIdReq;
   private static final String _elementName = "element";
   private static final String _qName = "name";
   private static final String _qType = "type";
   private static final String _recType = "recType";

   public NetSuiteCustomFieldObjectSchemaTransformer(String var1, String var2) {
      this(var1, var2, null);
   }

   public NetSuiteCustomFieldObjectSchemaTransformer(String var1, String var2, String var3) {
      this.objectName = var1;
      this.schemaXml = var2;
      this.customObjectIdReq = var3;
   }

   private final boolean isCustomObjectReq() {
      return !Strings.isNullOrEmpty(this.customObjectIdReq);
   }

   public String getModifiedSchema() {
      NetSuiteWsdlSchemaSnippetAssumption.validate(this.schemaXml);
      NetSuiteCustomFieldObjectSchemaTransformer.Handler var1 = new NetSuiteCustomFieldObjectSchemaTransformer.Handler();

      try {
         SaxParsers.parseString(this.schemaXml, var1, false);
         if (var1.howmanyCustomFieldList != 1) {
            throw new ConnectorNetSuiteDefinitionError("wsdl contains unexpected elements");
         } else {
            return var1.sb.toString();
         }
      } catch (SAXException var3) {
         throw new ConnectorNetSuiteDefinitionError(var3);
      }
   }

   private Map<String, String> customFieldListElementAttributesToMap(Attributes var1) {
      return customFieldListElementAttributesToMap(var1, this.objectName);
   }

   private static Map<String, String> customFieldListElementAttributesToMap(Attributes var0, String var1) {
      TreeMap var2 = Maps.newTreeMap();

      for (int var3 = 0; var3 < var0.getLength(); var3++) {
         String var4 = var0.getQName(var3);
         String var5 = var0.getValue(var3);
         if ("type".equals(var4)) {
            XmlQName var6 = new XmlQName(var5);
            NetSuiteCustomFieldListComplexTypeName.validateType(var6);
            var5 = NetSuiteCustomFieldListComplexTypeName.getNewLocalName(var1);
            var5 = var6.getQNameFromLocalName(var5);
         }

         var2.put(var4, var5);
      }

      return var2;
   }

   private class Handler extends DefaultHandler {
      int howmanyCustomFieldList = 0;
      private boolean shouldWrite = true;
      StringBuilder sb = new StringBuilder();

      private Handler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if ("element".equals(var3)) {
            String var5 = var4.getValue("name");
            if (NetSuiteCustomFieldObjectSchemaTransformer.this.isCustomObjectReq() && "recType".equals(var5)) {
               this.shouldWrite = false;
            }

            if (!this.shouldWrite) {
               return;
            }

            if ("customFieldList".equals(var5)) {
               this.howmanyCustomFieldList++;
               Map var6 = NetSuiteCustomFieldObjectSchemaTransformer.this.customFieldListElementAttributesToMap(var4);
               XmlElementUtils.startElementTagAttributeMap(this.sb, var3, var6, null);
               return;
            }
         }

         if (this.shouldWrite) {
            XmlElementUtils.startElementTag(this.sb, var3, var4);
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (!this.shouldWrite) {
            if ("element".equals(var3)) {
               this.shouldWrite = true;
               this.sb.append(NetSuiteCustomObjectRecTypeHelper.getCustomObjectRecType(NetSuiteCustomFieldObjectSchemaTransformer.this.customObjectIdReq));
            }
         } else {
            if (this.shouldWrite) {
               XmlElementUtils.endElementTag(this.sb, var3);
            }
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (this.shouldWrite) {
            String var4 = new String(var1, var2, var3);
            this.sb.append(StringEscapeUtils.escapeXml(var4));
         }
      }
   }
}
