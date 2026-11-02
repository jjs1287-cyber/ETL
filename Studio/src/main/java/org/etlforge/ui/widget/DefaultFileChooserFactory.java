package org.etlforge.ui.widget;

import java.io.File;
import javax.swing.JFileChooser;

public class DefaultFileChooserFactory implements FileChooserFactory {
   @Override
   public JFileChooser newFileChooser() {
      return new KongaFileChooser();
   }

   @Override
   public JFileChooser newFileChooser(File var1) {
      return new KongaFileChooser(var1);
   }
}
