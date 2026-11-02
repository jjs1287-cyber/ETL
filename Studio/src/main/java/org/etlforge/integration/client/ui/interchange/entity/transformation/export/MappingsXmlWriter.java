package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.util.file.FileProducer;
import org.etlforge.xml.XmlStringBuilder;

final class MappingsXmlWriter {
   private final List<String> mappings;
   private final Map<String, String> multipleFolders;
   private final FileProducer fileProvider;

   MappingsXmlWriter(List<String> var1, Map<String, String> var2, FileProducer var3) {
      this.mappings = var1 == null ? new ArrayList<>() : var1;
      this.multipleFolders = var2;
      this.fileProvider = var3;
   }

   void write() throws IOException {
      File var1 = this.fileProvider.getFile();
      if (var1 != null) {
         KongaIoUtils.createAndWrite(var1, this.getXml());
      }
   }

   private String getXml() {
      XmlStringBuilder var1 = new XmlStringBuilder();
      var1.addElement("mappings");
      this.writeMappings(var1);
      this.writeMultipleFolders(var1);
      var1.closeElement();
      return var1.getXml();
   }

   private void writeMappings(XmlStringBuilder var1) {
      for (String var3 : this.mappings) {
         var1.addElement("mapping");
         var1.setElementValue(var3);
         var1.closeElement();
      }
   }

   private void writeMultipleFolders(XmlStringBuilder var1) {
      MultipleFoldersXmlWriter.appendXml(this.multipleFolders, var1);
   }
}
