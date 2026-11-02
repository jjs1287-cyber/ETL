package org.etlforge.ui.util.file;

import com.google.common.base.Preconditions;

public abstract class FileSelectorPanelFactory {
   public abstract FileSelectorPanel recentFilesPanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4);

   public abstract FileSelectorPanel singleFilePanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4);

   public static FileSelectorPanelFactory newInstance() {
      return new FileSelectorPanelFactory() {
         @Override
         public FileSelectorPanel singleFilePanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4) {
            return new DefaultFileSelectorPanel(var1, var2, var3, var4);
         }

         @Override
         public FileSelectorPanel recentFilesPanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4) {
            Preconditions.checkNotNull(var4, "recentFiles");
            return new RecentFileSelectorPanel(var1, var2, var3, var4);
         }
      };
   }
}
