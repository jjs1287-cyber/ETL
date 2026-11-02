package org.etlforge.integration.design.netsuite.definition;

import com.google.common.collect.Maps;
import java.util.HashMap;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class NetSuiteRecordRefTypeTransformer {
   private static final String sequenceName = "sequence";
   private static final String attributeName = "attribute";
   private final String recordRefSchema;
   private String schemaPrefix = null;
   private final String recordRefNamespacePrefix;

   public NetSuiteRecordRefTypeTransformer(String var1, String var2) {
      this.recordRefNamespacePrefix = var1;
      this.recordRefSchema = var2;
   }

   public String getRecordRef() {
      NetSuiteRecordRefTypeTransformer.RecordRefHandler var1 = new NetSuiteRecordRefTypeTransformer.RecordRefHandler();
      return this.getModifiedString(var1);
   }

   public String getRecordJBRef() {
      NetSuiteRecordRefTypeTransformer.RecordRefJBHandler var1 = new NetSuiteRecordRefTypeTransformer.RecordRefJBHandler();
      return this.getModifiedString(var1);
   }

   public String getCustomizationRef() {
      NetSuiteRecordRefTypeTransformer.CustomRefHandler var1 = new NetSuiteRecordRefTypeTransformer.CustomRefHandler();
      return this.getModifiedString(var1);
   }

   private String getModifiedString(NetSuiteRecordRefTypeTransformer.Handler var1) {
      try {
         SaxParsers.parseString(this.recordRefSchema, var1, false);
         return var1.sb.toString();
      } catch (SAXException var3) {
         throw new ConnectorNetSuiteDefinitionError(var3);
      }
   }

   private void setSchemaPrefix(String var1) {
      if (this.schemaPrefix == null) {
         this.schemaPrefix = XmlQName.getPrefix(var1);
      }
   }

   private String getSchemaQName(String var1) {
      return XmlQName.getQName(this.schemaPrefix, var1);
   }

   private String getTypeQName(String var1) {
      return XmlQName.getQName(this.recordRefNamespacePrefix, var1);
   }

   private static boolean isSequence(String var0) {
      return var0.endsWith("sequence");
   }

   private static boolean isAttribute(String var0) {
      return var0.endsWith("attribute");
   }

   private class CustomRefHandler extends NetSuiteRecordRefTypeTransformer.Handler {
      private boolean isRoot = true;
      private boolean noScriptId = true;

      private CustomRefHandler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.isRoot) {
            HashMap var8 = Maps.newHashMap();
            var8.put("name", "CustomizationRef");
            XmlElementUtils.startElementTagAttributeMap(this.sb, var3, var8, null);
            this.isRoot = false;
         } else {
            if (this.noScriptId && NetSuiteRecordRefTypeTransformer.isAttribute(var3)) {
               this.noScriptId = false;
               NetSuiteRecordRefTypeTransformer.this.setSchemaPrefix(var3);
               HashMap var5 = Maps.newHashMap();
               String var6 = NetSuiteRecordRefTypeTransformer.this.getSchemaQName("attribute");
               String var7 = NetSuiteRecordRefTypeTransformer.this.getSchemaQName("string");
               var5.put("name", "scriptId");
               var5.put("type", var7);
               XmlElementUtils.startElementTagAttributeMap(this.sb, var6, var5, null);
               XmlElementUtils.endElementTag(this.sb, var6);
            }

            XmlElementUtils.startElementTag(this.sb, var3, var4);
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         XmlElementUtils.endElementTag(this.sb, var3);
      }
   }

   private class Handler extends DefaultHandler {
      protected final StringBuilder sb = new StringBuilder();

      Handler() {
      }
   }

   private class RecordRefHandler extends NetSuiteRecordRefTypeTransformer.Handler {
      private RecordRefHandler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         XmlElementUtils.startElementTag(this.sb, var3, var4);
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (NetSuiteRecordRefTypeTransformer.isSequence(var3)) {
            NetSuiteRecordRefTypeTransformer.this.setSchemaPrefix(var3);
            HashMap var4 = Maps.newHashMap();
            String var5 = NetSuiteRecordRefTypeTransformer.this.getSchemaQName("element");
            String var6 = NetSuiteRecordRefTypeTransformer.this.getTypeQName("CustomizationRef");
            var4.put("name", "CustomizationRef");
            var4.put("type", var6);
            var4.put("minOccurs", "0");
            XmlElementUtils.startElementTagAttributeMap(this.sb, var5, var4, null);
            XmlElementUtils.endElementTag(this.sb, var5);
         }

         XmlElementUtils.endElementTag(this.sb, var3);
      }
   }

   private class RecordRefJBHandler extends NetSuiteRecordRefTypeTransformer.Handler {
      private boolean isRoot = true;
      private final boolean noScriptId = true;

      private RecordRefJBHandler() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.isRoot) {
            HashMap var5 = Maps.newHashMap();
            var5.put("name", "RecordRef_JB");
            XmlElementUtils.startElementTagAttributeMap(this.sb, var3, var5, null);
            this.isRoot = false;
         } else {
            XmlElementUtils.startElementTag(this.sb, var3, var4);
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         XmlElementUtils.endElementTag(this.sb, var3);
      }
   }
}
