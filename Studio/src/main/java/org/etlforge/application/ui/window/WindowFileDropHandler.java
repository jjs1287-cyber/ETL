package org.etlforge.application.ui.window;

import java.io.File;
import java.util.List;

public interface WindowFileDropHandler {
   boolean isAvailable();

   boolean canImport(List<File> var1);

   boolean doImport(List<File> var1);
}
