package org.etlforge.integration.server.netsuite.wsdl;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.netsuite.definition.NetSuiteConstants;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSearchField;
import org.etlforge.integration.design.netsuite.search.wsdl.NetSuiteSearchWsdl;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.design.util.XsdSaxHandler;
import org.etlforge.integration.etlforgeconnector.XmlQName;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchObjectName;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSearchXsdParser {
   private final NetSuiteSearchWsdl wsdlSearch;
   private final ConnectorNetSuiteNamespaces ns;

   NetSuiteSearchXsdParser(NetSuiteSearchWsdl var1) {
      this.wsdlSearch = var1;
      this.ns = var1.getConnectorNetSuiteNamespaces();
   }

   void parseString(String var1) {
      NetSuiteSearchXsdParser.NetSuiteSearchXsdHandler var2 = new NetSuiteSearchXsdParser.NetSuiteSearchXsdHandler();

      try {
         SaxParsers.parseString(var1, var2, true);
      } catch (SAXException var4) {
         throw new ConnectorNetSuiteDefinitionError(var4);
      }
   }

   void parse(File var1) {
      NetSuiteSearchXsdParser.NetSuiteSearchXsdHandler var2 = new NetSuiteSearchXsdParser.NetSuiteSearchXsdHandler();

      try {
         SaxParsers.parseFile(var1, var2, true);
      } catch (SAXException var4) {
         throw new ConnectorNetSuiteDefinitionError(var4);
      } catch (IOException var5) {
         throw new ConnectorNetSuiteDefinitionError(var5);
      }
   }

   private boolean isSearchRecordOrRow(String var1, String var2) {
      return (var2.equals("SearchRecord") || var2.equals("SearchRecordBasic") || var2.equals("SearchRow") || var2.equals("SearchRowBasic"))
         && var1.equals(this.ns.getCoreNamespace());
   }

   private class NetSuiteSearchXsdHandler extends XsdSaxHandler {
      int globalElementLevel = -1;
      boolean isInsideSchema = false;
      private XsdGlobalElementName globalElementName;
      private boolean isInsideGlobalElement = false;
      private boolean isSearchRowBasic;
      private boolean isSearchRecordOrRow;
      private boolean isSupportedEnumerationType;
      private boolean isComplexType;

      NetSuiteSearchXsdHandler() {
         this.globalElementLevel = -1;
         this.isInsideSchema = false;
         this.initGlobalElement();
      }

      private void initGlobalElement() {
         this.isInsideGlobalElement = false;
         this.globalElementName = null;
         this.isSupportedEnumerationType = false;
         this.isComplexType = false;
         this.isSearchRowBasic = false;
         this.isSearchRecordOrRow = false;
      }

      private boolean isGlobalElement() {
         return this.isInsideSchema && this.getLevel() == this.globalElementLevel;
      }

      private void startGlobalElement(String var1, String var2, String var3, Attributes var4) {
         if (this.isGlobalElement()) {
            this.isInsideGlobalElement = true;
            if (!var2.equals("import")) {
               boolean var5 = var2.equals("element");
               if (var5) {
                  return;
               }

               String var6 = var4.getValue("name");
               if (Strings.isNullOrEmpty(var6)) {
                  throw new ConnectorNetSuiteDefinitionError("global element name in schema must exists ");
               }

               this.globalElementName = XsdGlobalElementName.newInstance(this.getTargetNamespace(), var6, var5);
               this.isSearchRowBasic = NetSuiteSearchObjectName.isSearchRowBasic(var6);
               if (var2.equals("simpleType")) {
                  this.isSupportedEnumerationType = NetSuiteSearchWsdl.isSupportedEnumerationType(this.globalElementName.getLocalName());
               } else if (var2.equals("complexType")) {
                  this.isComplexType = true;
               }
            }
         }
      }

      private void endGlobalElement(String var1, String var2, String var3) {
         if (this.isGlobalElement()) {
            this.initGlobalElement();
         }
      }

      private boolean isXsdSchema(String var1, String var2) {
         return var2.equals("schema") && NetSuiteConstants.instance.isXsdNamespace(var1);
      }

      private void startSchemaElement(String var1, String var2, String var3, Attributes var4) {
         if (!this.isInsideSchema) {
            if (this.isXsdSchema(var1, var2)) {
               this.isInsideSchema = true;
               this.globalElementLevel = this.getLevel() + 1;
            }
         }
      }

      private void endSchemaElement(String var1, String var2) {
         if (this.isInsideSchema) {
            if (this.getLevel() == this.globalElementLevel - 1) {
               if (this.isXsdSchema(var1, var2)) {
                  this.isInsideSchema = false;
                  this.globalElementLevel = -1;
               }
            }
         }
      }

      private void processSearchRecord(String var1, String var2, String var3, Attributes var4) {
         if (this.isInsideGlobalElement) {
            if (this.isComplexType) {
               if (!this.isSearchRowBasic && !this.isSearchRecordOrRow && var2.equals("extension")) {
                  String var5 = var4.getValue("base");
                  this.handlerXmlQName = new XmlQName(var5);
                  String var6 = this.prefixNamespace.getNamespaceExists(this.handlerXmlQName.getPrefix());
                  this.isSearchRecordOrRow = NetSuiteSearchXsdParser.this.isSearchRecordOrRow(var6, this.handlerXmlQName.getLocalName());
               }

               if (this.isSearchRowBasic || this.isSearchRecordOrRow) {
                  if (var2.equals("element")) {
                     String var9 = var4.getValue("name");
                     String var10 = var4.getValue("type");
                     this.handlerXmlQName = new XmlQName(var10);
                     String var7 = this.prefixNamespace.getNamespaceExists(this.handlerXmlQName.getPrefix());
                     NetSuiteSearchField var8 = new NetSuiteSearchField(var9, var7, this.handlerXmlQName.getLocalName());
                     NetSuiteSearchXsdParser.this.wsdlSearch.addSearchField(this.globalElementName, var8);
                  }
               }
            }
         }
      }

      private void processSimpleType(String var1, String var2, String var3, Attributes var4) {
         if (this.isInsideGlobalElement) {
            if (this.isSupportedEnumerationType) {
               if (var2.equals("enumeration")) {
                  String var5 = var4.getValue("value");
                  if (!Strings.isNullOrEmpty(var5)) {
                     NetSuiteSearchXsdParser.this.wsdlSearch.addEnumType(this.globalElementName, var5);
                  }
               }
            }
         }
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.startSchemaElement(var1, var2, var3, var4);
         this.startGlobalElement(var1, var2, var3, var4);
         if (this.isComplexType) {
            this.processSearchRecord(var1, var2, var3, var4);
         }

         if (this.isSupportedEnumerationType) {
            this.processSimpleType(var1, var2, var3, var4);
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         this.endGlobalElement(var1, var2, var3);
         this.endSchemaElement(var1, var2);
      }
   }
}
