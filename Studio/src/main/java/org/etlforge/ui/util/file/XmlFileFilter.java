package org.etlforge.ui.util.file;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.filechooser.FileFilter;
import org.etlforge.io.FileUtils;
import org.etlforge.util.file.ExtensionBasedFileFilter;

public class XmlFileFilter extends FileFilter implements ExtensionBasedFileFilter {
   @Override
   public boolean accept(File var1) {
      return var1.isDirectory() ? true : "xml".equalsIgnoreCase(FileUtils.getExtension(var1));
   }

   @Override
   public Iterator<String> getExtensions() {
      return Collections.singleton("xml").iterator();
   }

   @Override
   public String getDescription() {
      return "XML";
   }
}
