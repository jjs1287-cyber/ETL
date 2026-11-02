package org.etlforge.integration.client.ui.structure.editor.xsd;

import com.google.common.base.Preconditions;
import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.etlforge.xml.ExpandedName;

public final class SampleFile {
   private final File file;
   private final ExpandedName rootName;
   private String suggestedXsdName;

   public SampleFile(File var1, ExpandedName var2) {
      Preconditions.checkNotNull(var1, "file");
      Preconditions.checkNotNull(var2, "rootName");
      this.file = var1;
      this.rootName = var2;
      this.suggestedXsdName = FilenameUtils.getBaseName(var1.getName()) + ".xsd";
   }

   public File getFile() {
      return this.file;
   }

   public String getSuggestedXsdName() {
      return this.suggestedXsdName;
   }

   void useRootAsNameOfXsd() {
      this.suggestedXsdName = this.rootName.getLocalName() + ".xsd";
   }

   public ExpandedName getRootName() {
      return this.rootName;
   }

   @Override
   public String toString() {
      return this.file.getAbsolutePath() + " [" + this.rootName + "]";
   }
}
