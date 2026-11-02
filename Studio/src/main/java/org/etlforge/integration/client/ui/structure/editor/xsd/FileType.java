package org.etlforge.integration.client.ui.structure.editor.xsd;

import java.io.File;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public enum FileType {
   XML(".xml") {
      @Override
      public SampleFile createSampleFile(File var1) throws Exception {
         XmlParser var2 = new XmlParser(var1);
         ExpandedName var3 = var2.validate();
         return new SampleFile(var1, var3);
      }
   },
   JSON(".json") {
      @Override
      public SampleFile createSampleFile(File var1) throws Exception {
         ExpandedName var2 = XmlNameFactory.conforming().newExpandedName("", "json");
         return new SampleFile(var1, var2);
      }
   };

   private final String fileExtension;

   FileType(String var3) {
      this.fileExtension = var3;
   }

   final String getFileExtension() {
      return this.fileExtension;
   }

   protected abstract SampleFile createSampleFile(File var1) throws Exception;
}
