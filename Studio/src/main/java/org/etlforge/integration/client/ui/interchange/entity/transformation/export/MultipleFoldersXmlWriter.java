package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.util.file.FileProducer;
import org.etlforge.xml.XmlStringBuilder;

class MultipleFoldersXmlWriter {
   private final Map<String, String> multipleFolders;
   private final FileProducer fileProvider;

   MultipleFoldersXmlWriter(Map<String, String> var1, FileProducer var2) {
      this.multipleFolders = var1;
      this.fileProvider = var2;
   }

   void write() throws IOException {
      File var1 = this.fileProvider.getFile();
      if (var1 != null) {
         KongaIoUtils.createAndWrite(var1, this.getXml());
      }
   }

   private String getXml() {
      XmlStringBuilder var1 = new XmlStringBuilder();
      appendXml(this.multipleFolders, var1);
      return var1.getXml();
   }

   static void appendXml(Map<String, String> var0, XmlStringBuilder var1) {
      var1.addElement("multiple_folders");

      for (Entry var3 : var0.entrySet()) {
         writeMultipleFolder(var1, var3);
      }

      var1.closeElement();
   }

   private static void writeMultipleFolder(XmlStringBuilder var0, Entry<String, String> var1) {
      var0.addElement("folder");
      var0.addElement("path");
      var0.setElementValue((String)var1.getKey());
      var0.closeElement();
      String var2 = (String)var1.getValue();
      if (var2 != null) {
         var0.addElement("name");
         var0.setElementValue(var2);
         var0.closeElement();
      }

      var0.closeElement();
   }
}
