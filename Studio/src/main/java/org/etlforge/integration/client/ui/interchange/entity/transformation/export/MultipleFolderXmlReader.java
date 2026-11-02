package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import java.io.File;
import java.util.Map;
import org.etlforge.util.file.FileProducer;
import org.etlforge.xml.sax.KongaSaxParser;

final class MultipleFolderXmlReader {
   private final FileProducer fileFactory;

   MultipleFolderXmlReader(FileProducer var1) {
      this.fileFactory = var1;
   }

   Map<String, String> read() throws Exception {
      File var1 = this.fileFactory.getFile();
      if (var1 == null) {
         return null;
      }

      MultipleFolderHandler var2 = new MultipleFolderHandler("multiple_folders/folder");
      KongaSaxParser var3 = new KongaSaxParser(var2);
      var3.parseFile(var1);
      return var2.getFolders();
   }
}
