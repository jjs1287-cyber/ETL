package org.etlforge.integration.server.engine.connector.autodesk.rest;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorResponseProcessRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlCopySaxParser {
   public void Copy(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = Files.newWriter(new File(var2), Charsets.UTF_8);
         this.Copy(var1, var3);
      } catch (FileNotFoundException var8) {
         throw new ConnectorResponseProcessRuntimeException(var8);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   public void Copy(String var1, BufferedWriter var2) {
      XmlCopySaxParser.XmlCopyHandler var3 = new XmlCopySaxParser.XmlCopyHandler(var2);

      try {
         SaxParsers.parseFile(new File(var1), var3, false);
      } catch (SAXException var5) {
         throw new ConnectorResponseProcessRuntimeException("Failed to parse the response: ", var5);
      } catch (IOException var6) {
         throw new ConnectorResponseProcessRuntimeException(var6);
      }
   }

   public static void main(String[] var0) {
      String var1 = "Z:/tmp/schema/";
      String var2 = var1 + "snippet.xsd";
      String var3 = var1 + "nsSimple_copy.xsd";
      XmlCopySaxParser var4 = new XmlCopySaxParser();
      var4.Copy(var2, var3);
   }

   private class XmlCopyHandler extends DefaultHandler {
      private final Logger logger = Logger.getLogger(XmlCopySaxParser.XmlCopyHandler.class);
      private final BufferedWriter writer;

      public XmlCopyHandler(BufferedWriter var2) {
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

      @Override
      public void startPrefixMapping(String var1, String var2) throws SAXException {
         this.logger.info("startPrefixMapping " + var1 + ":" + var2);
      }

      @Override
      public void endPrefixMapping(String var1) throws SAXException {
         this.logger.info("endPrefixMapping " + var1);
      }

      @Override
      public void startElement(String var1, String var2, String var3, Attributes var4) throws SAXException {
         String var5 = XmlElementUtils.startElementTag(var3, var4);
         this.append(var5);
         this.logger.info("startElement:" + var2 + " q=" + var3 + "   u=" + var1);
      }

      @Override
      public void endElement(String var1, String var2, String var3) throws SAXException {
         String var4 = XmlElementUtils.endElementTag(var3);
         this.append(var4);
         this.logger.info("endElement:" + var2 + " q=" + var3 + "   u=" + var1);
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         String var4 = new String(var1, var2, var3);
         this.append(StringEscapeUtils.escapeXml(var4));
      }
   }
}
