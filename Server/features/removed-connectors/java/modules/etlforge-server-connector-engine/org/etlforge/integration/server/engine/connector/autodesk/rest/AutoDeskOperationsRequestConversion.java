package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Charsets;
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

public class AutoDeskOperationsRequestConversion {
   public AutoDeskOperationsRequestConversion.AutoDeskOperationsRequestInfo convertToKeyValuePairs(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = Files.newWriter(new File(var2), Charsets.UTF_8);
         return this.convertToKeyValuePairs(var1, var3);
      } catch (FileNotFoundException var8) {
         throw new ConnectorResponseProcessRuntimeException(var8);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   public AutoDeskOperationsRequestConversion.AutoDeskOperationsRequestInfo convertToKeyValuePairs(String var1, BufferedWriter var2) {
      AutoDeskOperationsRequestConversion.OperationRequstConversionHandler var3 = new AutoDeskOperationsRequestConversion.OperationRequstConversionHandler(var2);

      try {
         SaxParsers.parseFile(new File(var1), var3, false);
         return var3.requestInfo;
      } catch (SAXException var5) {
         throw new ConnectorResponseProcessRuntimeException("Failed to parse the request data: ", var5);
      } catch (IOException var6) {
         throw new ConnectorResponseProcessRuntimeException(var6);
      }
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/branches/v5/5.1.x/dev/ETLForgeConnectorProject/AUTODESK/Sample/";
      String var2 = var1 + "items_new_updateconversion.xml";
      String var3 = var1 + "items_new - update.xml";
      AutoDeskOperationsRequestConversion var4 = new AutoDeskOperationsRequestConversion();
      AutoDeskOperationsRequestConversion.AutoDeskOperationsRequestInfo var5 = var4.convertToKeyValuePairs(var3, var2);
      System.out.println("dmsid=" + var5.dmsID);
      System.out.println("alias=" + var5.alias);
   }

   public static class AutoDeskOperationsRequestInfo {
      String dmsID;
      String alias;
      String versionID;
   }

   private class OperationRequstConversionHandler extends DefaultHandler {
      private final BufferedWriter writer;
      private final AutoDeskOperationsRequestConversion.AutoDeskOperationsRequestInfo requestInfo;
      private StringBuilder elementValue;
      private boolean isInsideMetaFields;
      private boolean shouldWrite;

      private String value() {
         return this.elementValue.toString().trim();
      }

      public OperationRequstConversionHandler(BufferedWriter var2) {
         this.writer = var2;
         this.append(XmlUtils.xmlUtf8Header);
         this.isInsideMetaFields = false;
         this.shouldWrite = true;
         this.requestInfo = new AutoDeskOperationsRequestConversion.AutoDeskOperationsRequestInfo();
         this.elementValue = null;
      }

      private void append(String var1) {
         try {
            this.writer.append(var1);
         } catch (IOException var3) {
            throw new ConnectorServerRuntimeException(var3);
         }
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.elementValue = new StringBuilder();
         if (!this.isInsideMetaFields) {
            if (var3.equals("metaFields")) {
               this.isInsideMetaFields = true;
               this.shouldWrite = false;
               this.elementValue = null;
               this.append(XmlElementUtils.startElementTag(var3, var4));
            } else if (var3.equals("dmsID")) {
               this.shouldWrite = false;
            }
         }

         if (this.shouldWrite) {
            this.append(XmlElementUtils.startElementTag(var3, var4));
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (this.shouldWrite) {
            this.append(XmlElementUtils.endElementTag(var3));
         }

         if (this.isInsideMetaFields) {
            if (var3.equals("metaFields")) {
               this.isInsideMetaFields = false;
               this.shouldWrite = true;
               this.append(XmlElementUtils.endElementTag(var3));
            } else {
               this.append(XmlElementUtils.startElementTag("entry"));
               this.append(XmlElementUtils.elementValue("key", var3));
               this.append(XmlElementUtils.elementValue("value", this.value()));
               this.append(XmlElementUtils.endElementTag("entry"));
            }
         } else if (var3.equals("dmsID")) {
            this.requestInfo.dmsID = this.value();
            this.shouldWrite = true;
         }

         if (var3.equals("alias")) {
            this.requestInfo.alias = this.value();
         }

         if (var3.equals("versionID")) {
            this.requestInfo.versionID = this.value();
         }

         if (this.elementValue != null) {
            this.elementValue = null;
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         String var4 = new String(var1, var2, var3);
         if (this.shouldWrite) {
            this.append(StringEscapeUtils.escapeXml(var4));
         }

         if (this.elementValue != null) {
            this.elementValue.append(var4);
         }
      }
   }
}
