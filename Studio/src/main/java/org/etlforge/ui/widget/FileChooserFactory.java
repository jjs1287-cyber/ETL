package org.etlforge.ui.widget;

import java.io.File;
import javax.swing.JFileChooser;

public interface FileChooserFactory {
   JFileChooser newFileChooser();

   JFileChooser newFileChooser(File var1);
}
