package org.etlforge.integration.loadsource.client;

import java.io.File;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.loadsource.client.resources.LoadSourceResources;
import org.etlforge.ui.util.file.RecentFileStore;

public final class SimpleSourceDataFileInputProvider implements LoadSourceDataInputProvider {
   private final ApplicationWindow window;
   private final RecentFileStore recentFiles;

   public SimpleSourceDataFileInputProvider(ApplicationWindow var1) {
      this.window = var1;
      this.recentFiles = new TestCenterFileStore();
   }

   public LoadSourceDataFileInput getInput() {
      File var1 = this.getSourceFile();
      return var1 != null ? new LoadSourceDataFileInput(var1) : null;
   }

   private File getSourceFile() {
      return ApplicationFileChooser.getInstance(this.window)
         .getFileToOpen(null, null, this.recentFiles, LoadSourceResources.getString("LoadSource.BrowseDialogTitle"));
   }
}
