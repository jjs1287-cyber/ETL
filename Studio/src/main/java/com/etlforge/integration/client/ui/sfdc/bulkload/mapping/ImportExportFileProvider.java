package com.etlforge.integration.client.ui.sfdc.bulkload.mapping;

import java.io.File;
import org.etlforge.application.ui.widget.ApplicationFileChooser;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.util.file.PreferencesFileStore;
import org.etlforge.ui.util.file.RecentFileStore;
import org.etlforge.ui.util.file.SuffixFileChooserFilter;
import org.etlforge.util.preference.PreferencesKey;

final class ImportExportFileProvider {
   private FileChooser fc;

   public File getImportFile() {
      if (this.fc == null) {
         this.fc = this.createFileChooser();
      }

      SuffixFileChooserFilter var1 = new SuffixFileChooserFilter(".jdl", ".txt", ".properties", ".sdl");
      return this.fc.getFileToOpen(null, var1, this.getRecentFileStore(), "Select a file");
   }

   private FileChooser createFileChooser() {
      FileChooser var1 = ApplicationFileChooser.getInstance(null);
      var1.setSelectionMode(FileChooser.SelectionMode.FILES_ONLY);
      return var1;
   }

   private RecentFileStore getRecentFileStore() {
      PreferencesKey var1 = new PreferencesKey(ImportExportFileProvider.class, "MappingFile");
      return new PreferencesFileStore(var1, 1);
   }

   public File getExportFile() {
      if (this.fc == null) {
         this.fc = this.createFileChooser();
      }

      SuffixFileChooserFilter var1 = new SuffixFileChooserFilter(".jdl");
      return this.fc.getFileToSave(null, var1, this.getRecentFileStore(), "Select a file");
   }
}
