package org.etlforge.integration.server.engine.connector.util;

import com.google.common.base.Strings;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorFailedResponseRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorIllegalResponseRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorRuntimeException;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

final class ConnectorResponseSanityChecker {
   public static void checkXml(String var0, boolean var1) {
      if (XmlUtils.isHtml(var0)) {
         throw new ConnectorRuntimeException(" Fatal Error because the root is html!\n the content: " + var0);
      }

      parseXml(var0, false, var1);
   }

   public static void checkXmlFile(String var0) {
      try {
         boolean var1 = XmlUtils.isHtmlFile(var0);
         if (var1) {
            throw new ConnectorIllegalResponseRuntimeException(" Fatal Error because the root is html!\n the file:" + var0);
         }
      } catch (IOException var3) {
         throw new ConnectorResponseProcessRuntimeException(var3);
      }

      parseXml(var0, true, true);
   }

   private static void parseXml(String var0, boolean var1, boolean var2) {
      ConnectorResponseSanityChecker.ResponseSanityCheckerHanlder var3 = new ConnectorResponseSanityChecker.ResponseSanityCheckerHanlder(var0, var1);

      try {
         if (var1) {
            SaxParsers.parseFile(new File(var0), var3, false);
         } else {
            SaxParsers.parseString(var0, var3, false);
         }
      } catch (SAXException var5) {
         throw new ConnectorResponseProcessRuntimeException("Failed to parse the response: ", var5);
      } catch (IOException var6) {
         throw new ConnectorResponseProcessRuntimeException(var6);
      }
   }

   private ConnectorResponseSanityChecker() {
   }

   private static class ResponseSanityCheckerHanlder extends DefaultHandler {
      private boolean isSanityResponse = false;
      private boolean success;
      private String message = null;
      private StringBuilder elementValue;
      private int level = 0;
      private final boolean isFile;
      private final String xml;

      ResponseSanityCheckerHanlder(String var1, boolean var2) {
         this.xml = var1;
         this.isFile = var2;
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.elementValue = new StringBuilder();
         this.level++;
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         this.level--;
         if (this.level == 1) {
            if ("success".equals(var3)) {
               this.isSanityResponse = true;
               this.success = this.booleanValue();
               return;
            }

            if ("message".equals(var3)) {
               this.message = this.value();
               return;
            }
         }

         if (this.isSanityResponse) {
            if (!this.success) {
               String var5 = Strings.isNullOrEmpty(this.message) ? "No Message" : this.message;
               throw new ConnectorFailedResponseRuntimeException(
                  "The fatal error : " + var5 + "\n the response from CRM :" + (this.isFile ? "in the file " + this.xml : this.xml)
               );
            }
         } else {
            String var4 = "The response is unexpected : missing grand success element under the root!";
            if (this.isFile) {
               var4 = var4 + "\n the response is in the file " + this.xml;
            }

            throw new ConnectorIllegalResponseRuntimeException(var4);
         }
      }

      private String value() {
         return this.elementValue.toString();
      }

      private boolean booleanValue() {
         return KongaXmlUtils.isTrue(this.value().toLowerCase());
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         String var4 = new String(var1, var2, var3);
         this.elementValue.append(var4);
      }
   }
}
