package org.etlforge.integration.server.engine.connector.autodesk.rest;

import java.io.File;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorQuickStopExeception;
import org.etlforge.integration.server.engine.connector.util.HttpCallResponse;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AutoDeskErrorResponse {
   private static boolean shouldNotParseErrorMessage(HttpCallResponse var0) {
      return var0.hasNoResponseContent() || var0.isGoodResponse();
   }

   static String getErrorMessage(HttpCallResponse var0) throws ClientProtocolException, IOException, SAXException {
      return shouldNotParseErrorMessage(var0) ? "" : parseXmlErrorMessage(var0.getResponse(), false);
   }

   static String getErrorMessageFile(HttpCallResponse var0, String var1) throws ClientProtocolException, IOException, SAXException {
      return shouldNotParseErrorMessage(var0) ? "" : parseXmlErrorMessage(var1, true);
   }

   private static String parseXmlErrorMessage(String var0, boolean var1) throws SAXException, IOException {
      AutoDeskErrorResponse.AutoDeskResponseSanityCheckerHanlder var2 = new AutoDeskErrorResponse.AutoDeskResponseSanityCheckerHanlder();

      try {
         if (var1) {
            SaxParsers.parseFile(new File(var0), var2, false);
         } else {
            SaxParsers.parseString(var0, var2, true);
         }

         return "";
      } catch (ConnectorQuickStopExeception var4) {
         return var2.errorMessage();
      }
   }

   private static class AutoDeskResponseSanityCheckerHanlder extends DefaultHandler {
      StringBuilder elementValue = new StringBuilder();
      boolean isRoot = true;
      boolean isMessageELement = false;

      private AutoDeskResponseSanityCheckerHanlder() {
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.isRoot) {
            if (!var3.equals("errors")) {
               throw new ConnectorQuickStopExeception();
            }

            this.isRoot = false;
         } else {
            this.isMessageELement = var3.equals("message");
         }

         if (this.isMessageELement) {
            this.elementValue = new StringBuilder();
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (var3.equals("message")) {
            throw new ConnectorQuickStopExeception();
         }
      }

      String errorMessage() {
         return this.elementValue.toString();
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (this.isMessageELement) {
            this.elementValue.append(new String(var1, var2, var3));
         }
      }
   }
}
