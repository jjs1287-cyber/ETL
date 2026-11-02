package org.etlforge.application.ui.cli;

import java.io.File;
import java.io.IOException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.ui.util.file.DefaultFileChooser;
import org.etlforge.ui.util.file.FileChooser;
import org.etlforge.ui.widget.DefaultFileChooserFactory;

final class ExportErrorLogExecutor implements CommandExecutor {
   private FileChooser fileChooser;

   @Override
   public void execute(String[] var1) throws CommandException {
      String var2 = var1[0];
      if ("clipboard".equals(var2)) {
         ErrorLog.exportToClipboard();
      } else if ("file".equals(var2)) {
         this.exportToFile(var1);
      }
   }

   private void exportToFile(String[] var1) {
      String var2 = null;
      if (var1.length == 2) {
         var2 = var1[1];
      } else {
         var2 = this.askUserForPath();
      }

      if (var2 != null) {
         try {
            ErrorLog.exportToFile(new File(var2));
         } catch (IOException var4) {
            ErrorLog.attention(this.getClass(), "Could not save the error log to file: " + var4.getMessage(), var4);
         }
      }
   }

   private String askUserForPath() {
      if (this.fileChooser == null) {
         this.fileChooser = new DefaultFileChooser(new DefaultFileChooserFactory());
         this.fileChooser.setSelectedFile(new File(this.fileChooser.getCurrentDirectory(), "etlforge-client-errors.log"));
      }

      File var1 = this.fileChooser.getFileToSave(null, null, "Write all logged errors to file");
      return var1 != null ? var1.getAbsolutePath() : null;
   }
}
