package org.etlforge.ui.util.file;

import javax.swing.JTextField;
import org.etlforge.ui.widget.text.KongaTextField;

public final class DefaultFileSelectorPanel extends AbstractFileSelectorPanel {
   public DefaultFileSelectorPanel(FileChooser var1, FileSelectorPanel.Mode var2, String var3, RecentFileStore var4) {
      super(var1, var2, var3, var4);
   }

   @Override
   protected JTextField createTextField() {
      return new KongaTextField(40);
   }

   @Override
   public void displayLastFile() {
      RecentFileStore var1 = this.getRecentFiles();
      if (var1 != null) {
         FileUi var2 = var1.latestFile();
         if (var2 != null) {
            this.setSelectedFile(var2.getFile());
         }
      }
   }
}
