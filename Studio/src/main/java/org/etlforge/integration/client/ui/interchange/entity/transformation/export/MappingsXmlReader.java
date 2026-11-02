package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.etlforge.util.file.FileProducer;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;

final class MappingsXmlReader {
   private List<String> mappings;
   private Map<String, String> multipleFolders;

   public void read(FileProducer var1) throws Exception {
      File var2 = var1.getFile();
      if (var2 != null) {
         this.read(var2);
      }
   }

   public void read(File var1) throws Exception {
      this.mappings = Lists.newArrayList();
      MultipleFolderHandler var2 = new MultipleFolderHandler("mappings/multiple_folders/folder");
      KongaSaxParser var3 = new KongaSaxParser(new MappingsXmlReader.MappingHandler(), var2);
      var3.parseFile(var1);
      this.multipleFolders = var2.getFolders();
   }

   List<String> getMappings() {
      return this.mappings;
   }

   Map<String, String> getMultipleFolders() {
      return this.multipleFolders;
   }

   @RelativePathRoot("mappings")
   public final class MappingHandler {
      private MappingHandler() {
      }

      @EndElementHandler
      public void mapping(String var1) {
         String var2 = var1.trim();
         if (!var2.isEmpty()) {
            MappingsXmlReader.this.mappings.add(var2);
         }
      }
   }
}
