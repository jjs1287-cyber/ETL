package org.etlforge.integration.server.netsuite.wsdl;

import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.XsdGlobalElementName;
import org.etlforge.integration.design.util.XsdSaxHandler;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteWsdlParser {
   final NetSuiteWsdl wsdl;

   NetSuiteWsdlParser(NetSuiteWsdl var1) {
      this.wsdl = var1;
   }

   void parseString(String var1) {
      NetSuiteWsdlParser.WsdlHandler var2 = new NetSuiteWsdlParser.WsdlHandler();

      try {
         SaxParsers.parseString(var1, var2, true);
      } catch (SAXException var4) {
         throw new ConnectorNetSuiteDefinitionError(var4);
      }
   }

   private class WsdlHandler extends XsdSaxHandler {
      boolean isMessage = false;
      boolean isHeaders = false;
      boolean isService = false;
      private String messageName;

      WsdlHandler() {
      }

      private void init() {
         this.isMessage = false;
         this.isHeaders = false;
         this.isService = false;
         this.messageName = null;
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.getLevel() == 1) {
            this.isMessage = var2.equals("message");
            if (this.isMessage) {
               this.messageName = var4.getValue("name");
               this.isHeaders = this.messageName.equals("headers");
            } else {
               this.isService = var2.equals("service");
            }
         } else if (this.isService && this.getLevel() == 3) {
            NetSuiteWsdlParser.this.wsdl.setServiceUrl(var4.getValue("location"));
         }

         this.processMessage(var2, var4);
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         if (this.getLevel() == 1) {
            this.init();
         }
      }

      private void processMessage(String var1, Attributes var2) {
         boolean var3 = this.isMessage && this.getLevel() == 2;
         if (var3) {
            var3 = var1.equals("part");
         }

         if (var3) {
            String var4 = var2.getValue("name");
            String var5 = var2.getValue("element");
            XsdGlobalElementName var6 = this.getNetSuiteSchemaGlobalElementName(var5);
            if (this.isHeaders) {
               NetSuiteWsdlParser.this.wsdl.getHeaders().put(var4, var6);
            } else {
               NetSuiteWsdlParser.this.wsdl.getMessages().put(this.messageName, var6);
            }
         }
      }
   }
}
