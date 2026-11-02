package org.etlforge.integration.server.netsuite.wsdl;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinition;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaGlobalElement;
import org.etlforge.integration.design.netsuite.definition.NetSuiteSchemaXsd;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.design.util.XsdSaxHandler;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteXsdParser {
   private final NetSuiteDefinition definitions;
   private final NetSuiteSchemaXsd xsd;

   NetSuiteXsdParser(String var1, NetSuiteDefinition var2) {
      this.definitions = var2;
      this.xsd = new NetSuiteSchemaXsd(var1);
   }

   void parseString(String var1) {
      NetSuiteXsdParser.xsdHandler var2 = new NetSuiteXsdParser.xsdHandler();

      try {
         SaxParsers.parseString(var1, var2, true);
      } catch (SAXException var4) {
         throw new ConnectorNetSuiteDefinitionError(var4);
      }
   }

   private class xsdHandler extends XsdSaxHandler {
      int globalElementLevel = -1;
      boolean isInsideSchema = false;
      boolean isInsideGlobalElement = false;
      boolean isImport = false;
      StringBuilder GEBuilder = null;
      private Set<XsdGlobalElementName> dependsGlobalElements = null;
      private XsdGlobalElementName schemaElementName = null;

      private xsdHandler() {
      }

      private boolean isGlobalElement() {
         return this.isInsideSchema && this.getLevel() == this.globalElementLevel;
      }

      private void startGlobalElement(String var1, String var2, String var3, Attributes var4) {
         if (!this.isInsideGlobalElement) {
            if (this.isGlobalElement()) {
               this.isImport = var2.equals("import");
               if (!this.isImport) {
                  this.dependsGlobalElements = Sets.newHashSet();
                  this.isInsideGlobalElement = true;
                  this.GEBuilder = new StringBuilder();
                  boolean var5 = var2.equals("element");
                  String var6 = var4.getValue("name");
                  if (Strings.isNullOrEmpty(var6)) {
                     throw new ConnectorNetSuiteDefinitionError("global element name in schema must exists ");
                  }

                  this.schemaElementName = XsdGlobalElementName.newInstance(this.getTargetNamespace(), var6, var5);
               }
            }
         }
      }

      private void endGlobalElement(String var1, String var2, String var3) {
         if (this.isGlobalElement()) {
            if (!this.isImport) {
               NetSuiteSchemaGlobalElement var4 = new NetSuiteSchemaGlobalElement(this.dependsGlobalElements, this.GEBuilder.toString());
               NetSuiteXsdParser.this.definitions.putGlobalELement(this.schemaElementName, var4);
            }

            this.isInsideGlobalElement = false;
            this.GEBuilder = null;
            this.dependsGlobalElements = null;
            this.schemaElementName = null;
         }
      }

      private boolean isXsdSchema(String var1, String var2) {
         return var2.equals("schema") && var1.equals("http://www.w3.org/2001/XMLSchema");
      }

      private void startSchemaElement(String var1, String var2, String var3, Attributes var4) {
         this.processRoot();
         if (!this.isInsideSchema) {
            if (this.isXsdSchema(var1, var2)) {
               this.isInsideSchema = true;
               this.globalElementLevel = this.getLevel() + 1;
               NetSuiteXsdParser.this.xsd.setSchemaUri(var1);
               NetSuiteXsdParser.this.xsd.setSchemaLocalName(var2);
               NetSuiteXsdParser.this.xsd.setSchemaQName(var3);
               NetSuiteXsdParser.this.xsd.setSchemaAttributes(var4);
               NetSuiteXsdParser.this.xsd.setPrefixNamespace(this.prefixNamespace.getPrefixNamespaces());
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

      private void addDepend(XsdGlobalElementName var1) {
         if (!var1.isStandard()) {
            if (!this.dependsGlobalElements.contains(var1)) {
               this.dependsGlobalElements.add(var1);
            }
         }
      }

      private void processDerivedTypesandDepends(String var1, String var2, String var3, Attributes var4) {
         if (this.isInsideGlobalElement) {
            if (!this.isGlobalElement() || !var2.equals("import")) {
               if (var2.equals("extension")) {
                  String var5 = var4.getValue("base");
                  XsdGlobalElementName var6 = this.getNetSuiteSchemaGlobalTypeName(var5);
                  this.addDepend(var6);
                  if (!var6.isStandard()) {
                     NetSuiteXsdParser.this.definitions.getDerivedTypes().putDerivedType(var6, this.schemaElementName);
                  }
               } else {
                  String var8 = var4.getValue("type");
                  if (Strings.isNullOrEmpty(var8)) {
                     String var9 = var4.getValue("ref");
                     if (!Strings.isNullOrEmpty(var9)) {
                        XsdGlobalElementName var7 = this.getNetSuiteSchemaGlobalElementName(var9);
                        this.addDepend(var7);
                     }
                  } else {
                     XsdGlobalElementName var10 = this.getNetSuiteSchemaGlobalTypeName(var8);
                     this.addDepend(var10);
                  }
               }
            }
         }
      }

      private void processRoot() {
         if (this.isRoot()) {
            NetSuiteXsdParser.this.xsd.setTargetNamespace(this.getTargetNamespace());
            NetSuiteXsdParser.this.definitions.setNetSuiteSchemaXsd(this.getTargetNamespace(), NetSuiteXsdParser.this.xsd);
         }
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.startSchemaElement(var1, var2, var3, var4);
         this.startGlobalElement(var1, var2, var3, var4);
         this.processDerivedTypesandDepends(var1, var2, var3, var4);
         if (this.GEBuilder != null) {
            XmlElementUtils.startElementTag(this.GEBuilder, var3, var4, this.prefixNamespace.getCurrentPrefixNamespaces());
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         if (this.GEBuilder != null) {
            XmlElementUtils.endElementTag(this.GEBuilder, var3);
         }

         this.endSchemaElement(var1, var2);
         this.endGlobalElement(var1, var2, var3);
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (this.GEBuilder != null) {
            String var4 = new String(var1, var2, var3);
            this.GEBuilder.append(StringEscapeUtils.escapeXml(var4));
         }
      }
   }
}
