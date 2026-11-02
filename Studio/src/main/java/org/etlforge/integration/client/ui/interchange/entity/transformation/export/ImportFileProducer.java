package org.etlforge.integration.client.ui.interchange.entity.transformation.export;

import com.google.common.base.Supplier;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.util.file.FileProducer;

final class ImportFileProducer implements FileProducer {
   private final ApplicationWindow appWin;
   private final FileFilter supportedFormats;
   private final RecentFileStore recentFiles;

   public ImportFileProducer(ApplicationWindow var1, FileFilter var2, RecentFileStore var3) {
      this.appWin = var1;
      this.supportedFormats = var2;
      this.recentFiles = var3;
   }

   @Override
   public File getFile() {
      return UiThreadSupplier.call(new Supplier<File>() {
         public File get() {
            FileChooser var1 = ApplicationFileChooser.getInstance(ImportFileProducer.this.appWin);
            return var1.getFileToOpen(null, ImportFileProducer.this.supportedFormats, ImportFileProducer.this.recentFiles, "Import mappings from file:");
         }
      });
   }
}
