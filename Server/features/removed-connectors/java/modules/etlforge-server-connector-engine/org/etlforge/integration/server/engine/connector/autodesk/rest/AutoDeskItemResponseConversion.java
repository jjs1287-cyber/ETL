package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
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

public class AutoDeskItemResponseConversion {
   public void convertFromKeyValuePairsFile(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = Files.newWriter(new File(var2), Charsets.UTF_8);
         this.convertFromKeyValuePairsFile(var1, var3);
      } catch (FileNotFoundException var8) {
         throw new ConnectorResponseProcessRuntimeException(var8);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   private void convertFromKeyValuePairsFile(String var1, BufferedWriter var2) {
      AutoDeskItemResponseConversion.ItemPayloadResponseHandler var3 = new AutoDeskItemResponseConversion.ItemPayloadResponseHandler(var2);

      try {
         SaxParsers.parseFile(new File(var1), var3, true);
      } catch (SAXException var5) {
         throw new ConnectorResponseProcessRuntimeException("Failed to parse the response: ", var5);
      } catch (IOException var6) {
         throw new ConnectorResponseProcessRuntimeException(var6);
      }
   }

   public String convertFromKeyValuePairs(String var1, int var2) {
      StringWriter var3 = new StringWriter(var1.length());
      BufferedWriter var4 = new BufferedWriter(var3);
      this.convertFromKeyValuePairs(var1, var4, var2);
      ConnectorExecutionSystem.close(var4);
      return var3.toString();
   }

   private void convertFromKeyValuePairs(String var1, BufferedWriter var2, int var3) {
      AutoDeskItemResponseConversion.ItemPayloadResponseHandler var4 = new AutoDeskItemResponseConversion.ItemPayloadResponseHandler(var2, var3);

      try {
         SaxParsers.parseString(var1, var4, true);
      } catch (SAXException var6) {
         throw new ConnectorResponseProcessRuntimeException("Failed to parse the response: ", var6);
      }
   }

   public static void main(String[] var0) {
      String var1 = "Z:/tmp/data/";
      String var2 = var1 + "source.xml";
      String var3 = var1 + "target1.xml";
      AutoDeskItemResponseConversion var4 = new AutoDeskItemResponseConversion();
      String var5 = var1 + "PayloadItem.xml";
      String var6 = var4.convertFromKeyValuePairs(ConnectorExecutionSystem.readFileUTF8(var5), 2);
      System.out.println(var6);
   }

   private class ItemPayloadResponseHandler extends DefaultHandler {
      private final BufferedWriter writer;
      private String key;
      private StringBuilder elementValue;
      private boolean isInsideMetaFields;
      private boolean shouldWrite;
      private final int startLevel;
      private int level = 0;

      public ItemPayloadResponseHandler(BufferedWriter var2) {
         this.writer = var2;
         this.startLevel = 0;
         this.init();
      }

      public ItemPayloadResponseHandler(BufferedWriter var2, int var3) {
         this.writer = var2;
         this.startLevel = var3;
         this.init();
      }

      private void init() {
         this.isInsideMetaFields = false;
         this.level = 0;
         this.shouldWrite = true;
         if (this.startLevel == 0) {
            this.append(XmlUtils.xmlUtf8Header);
         }

         this.elementValue = new StringBuilder();
         this.key = null;
      }

      private void append(String var1) {
         if (this.level >= this.startLevel) {
            try {
               this.writer.append(var1);
            } catch (IOException var3) {
               throw new ConnectorServerRuntimeException(var3);
            }
         }
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.level++;
         if (this.shouldWrite) {
            this.append(XmlElementUtils.startElementTag(var3, var4));
         } else {
            this.elementValue = new StringBuilder();
         }

         if (!this.isInsideMetaFields && var3.equals("metaFields")) {
            this.isInsideMetaFields = true;
            this.shouldWrite = false;
         }

         if (!this.shouldWrite) {
            assert this.isInsideMetaFields;
            if (var3.equals("fieldData")) {
               this.shouldWrite = true;
               this.append(XmlElementUtils.startElementTag(this.key));
            }
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (this.isInsideMetaFields) {
            if (var3.equals("metaFields")) {
               this.isInsideMetaFields = false;
               this.shouldWrite = true;
            } else if (var3.equals("key")) {
               this.key = this.elementValue.toString();
            } else if (var3.equals("fieldData")) {
               this.shouldWrite = false;
               this.append(XmlElementUtils.endElementTag(this.key));
            }
         }

         if (this.shouldWrite) {
            this.append(XmlElementUtils.endElementTag(var3));
         }

         this.level--;
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         String var4 = new String(var1, var2, var3);
         if (this.shouldWrite) {
            this.append(StringEscapeUtils.escapeXml(var4));
         } else {
            this.elementValue.append(var4);
         }
      }
   }
}
