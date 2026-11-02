package org.etlforge.integration.server.engine.connector.netsuite.search;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import org.etlforge.integration.design.netsuite.definition.ConnectorNetSuiteNamespaces;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.design.util.XmlSaxHandler;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.integration.server.engine.connector.util.XmlUtils;
import org.etlforge.xml.sax.SaxParsers;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NetSuiteSearchResponseMerger {
   private final int totalPages;
   private final String responseFilename;
   private final NetSuiteSearchNames nameUtil;

   NetSuiteSearchResponseMerger(int var1, String var2, boolean var3, ConnectorNetSuiteNamespaces var4) {
      this(var1, var2, new NetSuiteSearchNames(var3, var4));
   }

   NetSuiteSearchResponseMerger(int var1, String var2, NetSuiteSearchNames var3) {
      this.totalPages = var1;
      this.responseFilename = var2;
      this.nameUtil = var3;
   }

   private void renameFirstFile() {
      String var1 = NetSuiteSearchMoreWithIdOperation.getNewFilename(this.responseFilename, 1);

      try {
         Files.move(new File(this.responseFilename), new File(var1));
      } catch (IOException var3) {
         throw new ConnectorServerRuntimeException(var3);
      }
   }

   void merge() {
      this.renameFirstFile();
      String var1 = NetSuiteSearchMoreWithIdOperation.getNewFilename(this.responseFilename, 1);
      this.transform(var1, this.responseFilename);
   }

   void transform(String var1, String var2) {
      BufferedWriter var3 = null;

      try {
         var3 = Files.newWriter(new File(var2), Charsets.UTF_8);
         this.transform(var1, var3, false);
      } catch (IOException var8) {
         throw new ConnectorServerRuntimeException(var8);
      } finally {
         ConnectorExecutionSystem.close(var3);
      }
   }

   public void transform(String var1, BufferedWriter var2, boolean var3) {
      NetSuiteSearchResponseMerger.NetSuiteSearchResponseMergerHandler var4 = new NetSuiteSearchResponseMerger.NetSuiteSearchResponseMergerHandler(var2, var3);

      try {
         SaxParsers.parseFile(new File(var1), var4, true);
      } catch (SAXException var6) {
         throw new ConnectorServerRuntimeException(var6);
      } catch (IOException var7) {
         throw new ConnectorServerRuntimeException(var7);
      }
   }

   public static void main(String[] var0) throws SAXException, IOException {
   }

   private class NetSuiteSearchResponseMergerHandler extends XmlSaxHandler {
      final BufferedWriter writer;
      final boolean writeRecordOnly;
      boolean shouldWrite;

      public NetSuiteSearchResponseMergerHandler(BufferedWriter var2, boolean var3) {
         this.writer = var2;
         this.writeRecordOnly = var3;
         this.shouldWrite = !this.writeRecordOnly;
         if (this.shouldWrite) {
            this.append(XmlUtils.xmlUtf8Header);
         }
      }

      private void append(String var1) {
         try {
            this.writer.append(var1);
         } catch (IOException var3) {
            throw new ConnectorServerRuntimeException(var3);
         }
      }

      @Override
      public void startElementImpl(String var1, String var2, String var3, Attributes var4) throws SAXException {
         if (this.shouldWrite) {
            XmlElementUtils.startElementTag(this.writer, var3, var4, this.prefixNamespace.getCurrentPrefixNamespaces());
         }

         if (this.writeRecordOnly && NetSuiteSearchResponseMerger.this.nameUtil.isRecordListElement(var1, var2)) {
            this.shouldWrite = true;
         }
      }

      @Override
      public void endElementImpl(String var1, String var2, String var3) throws SAXException {
         if (NetSuiteSearchResponseMerger.this.nameUtil.isRecordListElement(var1, var2)) {
            if (this.writeRecordOnly) {
               this.shouldWrite = false;
            } else {
               for (int var4 = 2; var4 <= NetSuiteSearchResponseMerger.this.totalPages; var4++) {
                  String var5 = NetSuiteSearchMoreWithIdOperation.getNewFilename(NetSuiteSearchResponseMerger.this.responseFilename, var4);
                  NetSuiteSearchResponseMerger.this.transform(var5, this.writer, true);
               }
            }
         }

         if (this.shouldWrite) {
            XmlElementUtils.endElementTag(this.writer, var3);
         }
      }

      @Override
      public void characters(char[] var1, int var2, int var3) throws SAXException {
         super.characters(var1, var2, var3);
         if (this.shouldWrite) {
            String var4 = new String(var1, var2, var3);
            XmlElementUtils.appendXmlValue(this.writer, var4);
         }
      }
   }
}
