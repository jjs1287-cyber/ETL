package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AutoDeskUpdateRequestVersionIDReplacer {
   final String versionID;

   public static void replaceVersionID(String var0, String var1, String var2) {
      AutoDeskUpdateRequestVersionIDReplacer var3 = new AutoDeskUpdateRequestVersionIDReplacer(var0);
      var3.replaceVersionID(var1, var2);
   }

   AutoDeskUpdateRequestVersionIDReplacer(String var1) {
      this.versionID = var1;
   }

   public void replaceVersionID(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = Files.newWriter(new File(var2), Charsets.UTF_8);
         this.replaceVersionID(var1, var3);
      } catch (FileNotFoundException var8) {
         throw new ConnectorResponseProcessRuntimeException(var8);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   public void replaceVersionID(String var1, BufferedWriter var2) {
      AutoDeskUpdateRequestVersionIDReplacer.XmlCopyHandler var3 = new AutoDeskUpdateRequestVersionIDReplacer.XmlCopyHandler(var2, this.versionID);

      try {
         SaxParsers.parseFile(new File(var1), var3, false);
      } catch (SAXException var5) {
         throw new ConnectorResponseProcessRuntimeException("Failed to parse the response: ", var5);
      } catch (IOException var6) {
         throw new ConnectorResponseProcessRuntimeException(var6);
      }
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/connectors/etlforgeconnectorsample/AUTODESK/Data/";
      String var2 = var1 + "upsert.xml";
      String var3 = var1 + "upsert_copy.xml";
      AutoDeskUpdateRequestVersionIDReplacer var4 = new AutoDeskUpdateRequestVersionIDReplacer("123456789");
      var4.replaceVersionID(var2, var3);
   }

   private class XmlCopyHandler extends DefaultHandler {
      private final BufferedWriter writer;
      private final String versionID;
      private StringBuilder elementValue = new StringBuilder();
      private boolean versionIDNotSet = true;
      private static final String metaFieldsElementName = "metaFields";
      private static final String versionIDElementName = "versionID";

      public XmlCopyHandler(BufferedWriter var2, String var3) {
         this.versionID = var3;
         this.writer = var2;
         this.append(XmlUtils.xmlUtf8Header);
      }

      private void append(String var1) {
         try {
            if (!Strings.isNullOrEmpty(var1)) {
               this.writer.append(var1);
            }
         } catch (IOException var3) {
            throw new ConnectorServerRuntimeException(var3);
         }
      }

      private void appendValue() {
         if (this.elementValue.length() > 0) {
            String var1 = this.elementValue.toString();
            this.append(StringEscapeUtils.escapeXml(var1));
         }

         this.elementValue = new StringBuilder();
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.versionIDNotSet && var3.equals("metaFields")) {
            this.append(XmlElementUtils.startElementTag("versionID"));
            this.append(this.versionID);
            this.append(XmlElementUtils.endElementTag("versionID"));
            this.versionIDNotSet = false;
         }

         this.appendValue();
         String var5 = XmlElementUtils.startElementTag(var3, var4);
         this.append(var5);
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (this.versionIDNotSet && var3.equals("versionID")) {
            if (this.elementValue.length() == 0) {
               this.append(this.versionID);
               this.elementValue = new StringBuilder();
            } else {
               this.appendValue();
            }

            this.versionIDNotSet = false;
         } else {
            this.appendValue();
         }

         String var4 = XmlElementUtils.endElementTag(var3);
         this.append(var4);
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         String var4 = new String(var1, var2, var3);
         this.elementValue.append(var4);
      }
   }
}
