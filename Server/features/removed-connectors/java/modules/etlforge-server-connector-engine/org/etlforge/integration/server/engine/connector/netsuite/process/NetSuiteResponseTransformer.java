package org.etlforge.integration.server.engine.connector.netsuite.process;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDerivedTypeSearchRowHandler;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.design.util.XmlSaxHandler;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteResponseTransformer {
   private final String baseTypeNamespace;
   private final String baseTypeLocalName;
   private final String objectElementName;
   private final ConnectorNetSuiteNamespaces ns;
   private final boolean isSearchRow;
   private final NetSuiteRecordRefWrapper recordRef;

   public NetSuiteResponseTransformer(NetSuiteRecordRefWrapper var1, ConnectorNetSuiteNamespaces var2, String var3, String var4) {
      this(var1, var2, var3, var4, null, false);
   }

   public NetSuiteResponseTransformer(NetSuiteRecordRefWrapper var1, ConnectorNetSuiteNamespaces var2, String var3, String var4, String var5, boolean var6) {
      this.recordRef = var1;
      this.ns = var2;
      this.baseTypeLocalName = var4;
      this.baseTypeNamespace = var3;
      this.objectElementName = var5;
      this.isSearchRow = var6;
   }

   public void transform(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = Files.newWriter(new File(var2), Charsets.UTF_8);
         this.transform(var1, var3);
      } catch (SAXException var9) {
         throw new ConnectorServerRuntimeException(var9);
      } catch (IOException var10) {
         throw new ConnectorServerRuntimeException(var10);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   public void transform(String var1, BufferedWriter var2) throws SAXException, IOException {
      NetSuiteResponseTransformer.Handler var3 = new NetSuiteResponseTransformer.Handler(var2);
      SaxParsers.parseFile(new File(var1), var3, true);
   }

   private static Map<String, String> convertAttributeToMap(Attributes var0) {
      HashMap var1 = Maps.newHashMap();

      for (int var2 = 0; var2 < var0.getLength(); var2++) {
         String var3 = var0.getQName(var2);
         if (!"xsi:type".equals(var3)) {
            var1.put(var3, var0.getValue(var2));
         }
      }

      return var1;
   }

   private class Handler extends XmlSaxHandler {
      final BufferedWriter writer;
      private String baseRefDerivedQName = null;
      private int fieldLevel;
      private String customizationRefQName;
      private String searchRowBasicObjectQName;

      public Handler(BufferedWriter var2) {
         this.writer = var2;
         this.append(XmlUtils.xmlUtf8Header);
         this.fieldLevel = -1;
         this.customizationRefQName = null;
         this.searchRowBasicObjectQName = null;
      }

      private void append(String var1) {
         try {
            this.writer.append(var1);
         } catch (IOException var3) {
            throw new ConnectorServerRuntimeException(var3);
         }
      }

      private boolean isBaseTypeElement(String var1, String var2) {
         return var2.equals(NetSuiteResponseTransformer.this.baseTypeLocalName) && var1.equals(NetSuiteResponseTransformer.this.baseTypeNamespace);
      }

      private void startDerivedTypeImpl(String var1, String var2, String var3, Attributes var4) {
         if (this.fieldLevel <= 0) {
            this.fieldLevel = NetSuiteResponseTransformer.this.isSearchRow ? this.getLevel() + 2 : this.getLevel() + 1;
         }

         String var5 = this.prefixNamespace.getPrefix(NetSuiteResponseTransformer.this.ns.getCoreNamespace());
         if (var5 == null) {
            throw new ConnectorServerRuntimeException("No prefix defined for " + NetSuiteResponseTransformer.this.ns.getCoreNamespace());
         }

         String var6 = NetSuiteResponseTransformer.this.objectElementName;
         if (Strings.isNullOrEmpty(var6)) {
            XmlQName var7 = new XmlQName(var4.getValue("xsi:type"));
            var6 = var7.getLocalName();
            if (NetSuiteResponseTransformer.this.isSearchRow) {
               String var8 = NetSuiteSearchObjectName.getObjectNameFromSearchRow(var6);
               this.searchRowBasicObjectQName = XmlQName.getQName(var7.getPrefix(), var8);
            }
         }

         this.baseRefDerivedQName = XmlQName.getQName(var5, var6);
         XmlElementUtils.startElementTag(this.writer, var3, null, this.prefixNamespace.getCurrentPrefixNamespaces());
         Map var9 = NetSuiteResponseTransformer.convertAttributeToMap(var4);
         XmlElementUtils.startElementTagAttributeMap(this.writer, this.baseRefDerivedQName, var9, null);
      }

      private boolean isCustomizationRef(Attributes var1) {
         if (!this.isFieldLevel()) {
            return false;
         }

         if (this.customizationRefQName != null) {
            this.customizationRefQName = null;
         }

         String var2 = var1.getValue("xsi:type");
         if (Strings.isNullOrEmpty(var2)) {
            return false;
         }

         XmlQName var3 = new XmlQName(var2);
         String var4 = this.prefixNamespace.getNamespace(var3.getPrefix());
         if (!NetSuiteResponseTransformer.this.recordRef.isCustomizationRef(var4, var3.getLocalName())) {
            return false;
         }

         this.customizationRefQName = var2;
         return true;
      }

      private boolean isFieldLevel() {
         return this.fieldLevel == this.getLevel();
      }

      private boolean isSearchRowBasicLevel(String var1) {
         boolean var2 = NetSuiteResponseTransformer.this.isSearchRow && this.getLevel() == this.fieldLevel - 1;
         if (var2) {
            XmlQName var3 = new XmlQName(var1);
            var2 = NetSuiteDerivedTypeSearchRowHandler.isObjectElementName(var3.getLocalName());
         }

         return var2;
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.isBaseTypeElement(var1, var2)) {
            this.startDerivedTypeImpl(var1, var2, var3, var4);
         } else if (this.isCustomizationRef(var4)) {
            XmlElementUtils.startElementTag(this.writer, var3, null, this.prefixNamespace.getCurrentPrefixNamespaces());
            XmlElementUtils.startElementTag(this.writer, this.customizationRefQName, var4);
         } else if (this.isSearchRowBasicLevel(var3)) {
            XmlElementUtils.startElementTag(this.writer, this.searchRowBasicObjectQName, var4, this.prefixNamespace.getCurrentPrefixNamespaces());
         } else {
            XmlElementUtils.startElementTag(this.writer, var3, var4, this.prefixNamespace.getCurrentPrefixNamespaces());
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         if (this.isBaseTypeElement(var1, var2)) {
            XmlElementUtils.endElementTag(this.writer, this.baseRefDerivedQName);
            XmlElementUtils.endElementTag(this.writer, var3);
            this.fieldLevel = -1;
            this.searchRowBasicObjectQName = null;
         } else if (this.isFieldLevel() && !Strings.isNullOrEmpty(this.customizationRefQName)) {
            XmlElementUtils.endElementTag(this.writer, this.customizationRefQName);
            XmlElementUtils.endElementTag(this.writer, var3);
         } else if (this.isSearchRowBasicLevel(var3)) {
            XmlElementUtils.endElementTag(this.writer, this.searchRowBasicObjectQName);
         } else {
            XmlElementUtils.endElementTag(this.writer, var3);
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         super.characters(var1, var2, var3);
         String var4 = new String(var1, var2, var3);
         XmlElementUtils.appendXmlValue(this.writer, var4);
      }
   }
}
