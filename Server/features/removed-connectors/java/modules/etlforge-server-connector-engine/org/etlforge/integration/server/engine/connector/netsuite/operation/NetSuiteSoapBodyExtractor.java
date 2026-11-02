package org.etlforge.integration.server.engine.connector.netsuite.operation;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.design.util.XmlSaxHandler;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSoapBodyExtractor {
   public void transform(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = Files.newWriter(new File(var2), Charsets.UTF_8);
         this.transform(var1, var3);
      } catch (SAXException var9) {
         throw new ConnectorServerRuntimeException(var9);
      } catch (IOException var10) {
         throw new ConnectorServerRuntimeException(var10);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   public void transform(String var1, BufferedWriter var2) throws SAXException, IOException {
      NetSuiteSoapBodyExtractor.NetSuiteSoapBodyExtractorHandler var3 = new NetSuiteSoapBodyExtractor.NetSuiteSoapBodyExtractorHandler(var2);
      SaxParsers.parseFile(new File(var1), var3, true);
   }

   public static void main(String[] var0) {
      String var1 = "Z:/etlforge/branches/v5/5.2.x/dev/ETLForgeConnectorProject/NETSUITE/Sample/";
      String var2 = var1 + "upsertlistres_2.xml";
      String var3 = var1 + "upsertlistres_2_body.xml";
      NetSuiteSoapBodyExtractor var4 = new NetSuiteSoapBodyExtractor();
      var4.transform(var2, var3);
   }

   private class NetSuiteSoapBodyExtractorHandler extends XmlSaxHandler {
      boolean isInsideBody = false;
      boolean shouldNoteWrite = true;
      int bodyLevel;
      final BufferedWriter writer;

      public NetSuiteSoapBodyExtractorHandler(BufferedWriter var2) {
         this.writer = var2;
         this.append(XmlUtils.xmlUtf8Header);
      }

      private void append(String var1) {
         try {
            this.writer.append(var1);
         } catch (IOException var3) {
            throw new ConnectorServerRuntimeException(var3);
         }
      }

      private boolean isSoapBody(String var1, String var2) {
         return var2.equals("Body") && var1.equals("http://schemas.xmlsoap.org/soap/envelope/");
      }

      private boolean isBodyRoot() {
         return this.isInsideBody && this.bodyLevel == this.getLevel();
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.isSoapBody(var1, var2)) {
            this.isInsideBody = true;
            this.bodyLevel = this.getLevel() + 1;
         } else if (this.isInsideBody) {
            if (this.isBodyRoot()) {
               Map var5 = this.getPrefixNamespace().getPrefixNamespaces();
               this.shouldNoteWrite = false;
               XmlElementUtils.startElementTag(this.writer, var3, var4, var5);
            } else {
               XmlElementUtils.startElementTag(this.writer, var3, var4, this.prefixNamespace.getCurrentPrefixNamespaces());
            }
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         if (this.isSoapBody(var1, var2)) {
            this.isInsideBody = false;
            this.shouldNoteWrite = true;
         } else if (this.isInsideBody) {
            XmlElementUtils.endElementTag(this.writer, var3);
            if (this.isBodyRoot()) {
               this.shouldNoteWrite = true;
            }
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         super.characters(var1, var2, var3);
         if (!this.shouldNoteWrite) {
            String var4 = new String(var1, var2, var3);
            XmlElementUtils.appendXmlValue(this.writer, var4);
         }
      }
   }
}
