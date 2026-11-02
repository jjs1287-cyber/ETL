package org.etlforge.ui.util.file;

import java.io.File;
import java.util.EventListener;

public interface FileSelectionListener extends EventListener {
   void selectionChanged(File var1);
}
