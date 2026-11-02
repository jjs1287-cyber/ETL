package org.etlforge.integration.client.ui.structure.editor.xsd;

import java.io.File;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.xsd.GeneratedXsds;

public final class CreateXsdResult {
   private final SampleFile sampleFile;
   private final GeneratedXsds xsds;

   public CreateXsdResult(SampleFile var1, GeneratedXsds var2) {
      this.sampleFile = var1;
      this.xsds = var2;
   }

   public ExpandedName getRootName() {
      return this.sampleFile.getRootName();
   }

   GeneratedXsds getXsds() {
      return this.xsds;
   }

   public File getMainXsd() {
      return this.xsds.getMainXsd();
   }

   public File getParentFolder() {
      return this.getMainXsd().getParentFile();
   }
}
