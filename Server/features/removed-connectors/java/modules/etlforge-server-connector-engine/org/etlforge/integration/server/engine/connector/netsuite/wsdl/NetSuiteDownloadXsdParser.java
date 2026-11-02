package org.etlforge.integration.server.engine.connector.netsuite.wsdl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.util.Map;
import org.etlforge.integration.design.exception.ConnectorNetSuiteDefinitionError;
import org.etlforge.integration.design.util.XsdSaxHandler;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteDownloadXsdParser {
   Map<String, String> locations = Maps.newConcurrentMap();

   NetSuiteDownloadXsdParser() {
   }

   void parseString(String var1) {
      NetSuiteDownloadXsdParser.Handler var2 = new NetSuiteDownloadXsdParser.Handler();

      try {
         SaxParsers.parseString(var1, var2, true);
      } catch (SAXException var4) {
         throw new ConnectorNetSuiteDefinitionError(var4);
      }
   }

   private class Handler extends XsdSaxHandler {
      private int importLevel = -1;

      private Handler() {
      }

      private boolean isImport(String var1, String var2) {
         return var2.equals("import") && var1.equals("http://www.w3.org/2001/XMLSchema");
      }

      private boolean isXsdSchema(String var1, String var2) {
         return var2.equals("schema") && var1.equals("http://www.w3.org/2001/XMLSchema");
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.importLevel < 0) {
            if (this.isXsdSchema(var1, var2)) {
               this.importLevel = this.getLevel() + 1;
            }
         } else {
            if (this.importLevel == this.getLevel() && this.isImport(var1, var2)) {
               String var5 = var4.getValue("schemaLocation");
               String var6 = var4.getValue("namespace");
               if (!Strings.isNullOrEmpty(var5)) {
                  NetSuiteDownloadXsdParser.this.locations.put(var5, var6);
               }
            }
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
      }
   }
}
