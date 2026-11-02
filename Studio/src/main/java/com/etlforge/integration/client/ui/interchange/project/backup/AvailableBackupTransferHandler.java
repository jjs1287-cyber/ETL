package com.etlforge.integration.client.ui.interchange.project.backup;

import com.google.common.base.Function;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import org.etlforge.integration.client.project.backup.AvailableBackup;
import org.etlforge.lang.Arrays2;
import org.etlforge.system.SystemUtils;

public class AvailableBackupTransferHandler extends TransferHandler {
   private static final Function<AvailableBackup, String> TO_DISPLAY = new Function<AvailableBackup, String>() {
      public String apply(AvailableBackup var1) {
         return RendererUtils.display(var1);
      }
   };
   private final ProjectBackupBrowser browser;

   public AvailableBackupTransferHandler(ProjectBackupBrowser var1) {
      this.browser = var1;
   }

   @Override
   protected final Transferable createTransferable(JComponent var1) {
      AvailableBackup[] var2 = this.browser.getSelectedBackups();
      if (var2.length == 0) {
         return null;
      } else {
         return var2.length == 1 ? new AvailableBackupTransferable(var2[0]) : new StringSelection(Arrays2.join(var2, TO_DISPLAY, SystemUtils.LINE_SEPARATOR));
      }
   }

   @Override
   public final int getSourceActions(JComponent var1) {
      return 1;
   }
}
