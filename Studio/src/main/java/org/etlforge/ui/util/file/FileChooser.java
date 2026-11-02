package org.etlforge.ui.util.file;

import java.awt.Component;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public interface FileChooser {
   FileChooser.SelectionMode FILES_ONLY = FileChooser.SelectionMode.FILES_ONLY;
   FileChooser.SelectionMode DIRECTORIES_ONLY = FileChooser.SelectionMode.DIRECTORIES_ONLY;
   FileChooser.SelectionMode FILES_AND_DIRECTORIES = FileChooser.SelectionMode.FILES_AND_DIRECTORIES;
   FileChooser.OverwriteConfirmer ALWAYS_ALLOW_OVERWRITES = new FileChooser.OverwriteConfirmer() {
      @Override
      public boolean confirmOverwrite(File var1) {
         return true;
      }
   };

   void setOverwriteConfirmer(FileChooser.OverwriteConfirmer var1);

   void setSelectedFile(File var1);

   void setSelectionMode(FileChooser.SelectionMode var1);

   FileChooser.SelectionMode getSelectionMode();

   File getCurrentDirectory();

   File getFileToSave(Component var1, FileFilter var2, String var3);

   File getFileToSave(Component var1, File var2, FileFilter var3, String var4);

   File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3);

   File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3, String var4);

   File getFileToSave(Component var1, FileFilter var2, RecentFileStore var3, String var4, File var5);

   File getFileToOpen(Component var1, FileFilter var2, String var3);

   File getFileToOpen(Component var1, FileFilter var2, RecentFileStore var3);

   File getFileToOpen(Component var1, FileFilter var2, RecentFileStore var3, String var4);

   interface OverwriteConfirmer {
      boolean confirmOverwrite(File var1);
   }

   enum SelectionMode {
      FILES_ONLY,
      DIRECTORIES_ONLY,
      FILES_AND_DIRECTORIES;
   }
}
