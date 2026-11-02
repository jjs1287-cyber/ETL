package org.etlforge.integration.server.engine.connector.msgp.adapter;

import com.google.common.base.Strings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MsGPDocumentExtractor {
   void extractDocumentFile(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var2), XmlUtils.xmlUtf8Encoding));
         this.extractDocumentFile(var1, var3);
      } catch (FileNotFoundException var9) {
         throw new ConnectorResponseProcessRuntimeException(var9);
      } catch (SAXException var10) {
         throw new ConnectorResponseProcessRuntimeException(var10);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   void extractDocumentFile(String var1, BufferedWriter var2) throws SAXException {
      if (Strings.isNullOrEmpty(var1)) {
         throw new ConnectorServerRuntimeException("No input ");
      }

      File var3 = new File(var1);
      if (!var3.exists()) {
         throw new ConnectorServerRuntimeException(var1 + " does not exist");
      }

      MsGPDocumentExtractor.MsGPDocumentExtractorHanlder var4 = new MsGPDocumentExtractor.MsGPDocumentExtractorHanlder(var2);

      try {
         var2.write(XmlUtils.xmlUtf8Header);
         var2.newLine();
         SaxParsers.parseFile(var3, var4, true);
      } catch (IOException var6) {
         throw new ConnectorResponseProcessRuntimeException(var6);
      }
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/branches/v5/5.1.x/dev/ETLForgeConnectorProject/MSGP/Data/Request&Response/test.xml";
      String var2 = "Z:/etlforge/branches/v5/5.1.x/dev/ETLForgeConnectorProject/MSGP/Data/Request&Response/result.xml";
      MsGPDocumentExtractor var3 = new MsGPDocumentExtractor();
      var3.extractDocumentFile(var1, var2);
   }

   private static class MsGPDocumentExtractorHanlder extends DefaultHandler {
      private int level = 0;
      private static final String eConnectRoot = "eConnect";
      private static final int eConnectLevel = 4;
      private boolean iseConnectTree = false;
      private final BufferedWriter writer;

      MsGPDocumentExtractorHanlder(BufferedWriter var1) {
         this.writer = var1;
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         this.level++;
         if (this.level == 4) {
            this.iseConnectTree = var3.equals("eConnect");
         }

         if (this.iseConnectTree) {
            boolean var5 = this.level == 4;

            try {
               this.writer.append("<").append(var3);

               for (int var6 = 0; var6 < var4.getLength(); var6++) {
                  String var7 = var4.getQName(var6);
                  this.writer.append(' ').append(var7);
                  this.writer.append("=\"").append(var4.getValue(var6)).append('"');
                  if (var5 && var7.equals("xmlns:xsi")) {
                     var5 = false;
                  }
               }

               if (var5) {
                  this.writer.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
               }

               this.writer.append('>');
            } catch (IOException var8) {
               throw new ConnectorResponseProcessRuntimeException(var8);
            }
         }
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         if (this.iseConnectTree) {
            if (this.level <= 4) {
               this.iseConnectTree = false;
            }

            try {
               this.writer.append("</").append(var3);
               this.writer.append('>');
            } catch (IOException var5) {
               throw new ConnectorResponseProcessRuntimeException(var5);
            }
         }

         this.level--;
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         if (this.iseConnectTree) {
            try {
               String var4 = new String(var1, var2, var3);
               this.writer.write(StringEscapeUtils.escapeXml(var4));
            } catch (IOException var5) {
               throw new ConnectorResponseProcessRuntimeException(var5);
            }
         }
      }
   }
}
