package org.etlforge.application.ui.update;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.window.action.OpenLocalFolderAction;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;

final class DownloadCompleteUi {
   private final File downloadedFile;

   public DownloadCompleteUi(File var1) {
      this.downloadedFile = var1;
   }

   public JComponent layout() {
      JLabel var1 = this.createMessageLabel();
      JComponent var2;
      if (SystemUtils.IS_OS_WINDOWS) {
         JPanel var3 = InvisiblePanel.of(new DownloadCompleteUi.ShowInFolderAction());
         var3.setBorder(Empty.border(8, 8, 0, 0));
         var2 = new OneColumnPanel().stretchHorizontally().of(var1, var3).nonOpaque().container();
      } else {
         var2 = var1;
      }

      var2.setBorder(Empty.border(8));
      return var2;
   }

   private JLabel createMessageLabel() {
      String var1 = "The update was downloaded to: " + this.downloadedFile.getAbsolutePath();
      KongaLabel var2 = TextStyles.AdminHint.makeLabel(var1);
      var2.setIcon(CommonIcons.INFO_24);
      return var2;
   }

   private class ShowInFolderAction extends OpenLocalFolderAction {
      public ShowInFolderAction() {
         super("update.show_in_folder", "Show in folder", null);
      }

      @Override
      protected File getFolderToOpen() {
         return DownloadCompleteUi.this.downloadedFile.getParentFile();
      }
   }
}
