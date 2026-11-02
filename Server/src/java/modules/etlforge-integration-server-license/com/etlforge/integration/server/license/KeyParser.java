package com.etlforge.integration.server.license;

import java.util.zip.DataFormatException;
import org.etlforge.integration.server.ServerUiBackendLog;
import org.etlforge.util.zip.ZipUtils;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class KeyParser {
   private String xml;

   public String parse(String var1) {
      this.xml = null;

      try {
         Document var2 = DomParser.parseXmlString(var1);
         String var3 = this.extractKeyFromXml(var2);
         this.xml = var1;
         return var3;
      } catch (Exception var4) {
         return var1;
      }
   }

   public String parseCompressedData(String var1) throws DataFormatException {
      String var2 = ZipUtils.base64DecodeAndInflateString(var1);
      return this.parse(var2);
   }

   private String extractKeyFromXml(Document var1) {
      Element var2 = Elements.matching("key-list/key", var1).item(0);
      return KongaXmlUtils.getElementValue(var2).trim();
   }

   public void saveLicenseToDisk() {
      if (this.xml != null) {
         try {
            new LicenseFolder().writeLicenseXmlFile(this.xml);
         } catch (Exception var2) {
            ServerUiBackendLog.LOG.throwing(this.getClass().getName(), "saveXmlFileInLicenseFolder", var2);
         }
      }
   }
}
