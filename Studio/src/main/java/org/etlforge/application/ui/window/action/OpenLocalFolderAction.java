package org.etlforge.application.ui.window.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.action.ActionDictionary;

public abstract class OpenLocalFolderAction extends AbstractApplicationAction {
   protected OpenLocalFolderAction(String var1, String var2, Icon var3) {
      super(var1, var2, var3);
   }

   protected OpenLocalFolderAction(String var1, ActionDictionary var2) {
      super(var1, var2);
   }

   protected abstract File getFolderToOpen();

   @Override
   public final void execute(ActionEvent var1) {
      if (SystemUtils.IS_OS_WINDOWS) {
         this.openFolder();
      } else {
         ErrorLog.attention(
            OpenLocalFolderAction.class,
            "This action is available only when running on Windows. In fact, you should not even be able to see it!",
            new Exception()
         );
      }
   }

   private void openFolder() {
      File var1 = this.getFolderToOpen();
      if (var1 != null) {
         try {
            Runtime.getRuntime().exec("explorer.exe \"" + var1.getAbsolutePath() + "\"");
         } catch (IOException var3) {
            ErrorLog.attention(OpenLocalFolderAction.class, "Failed to open the folder " + var1.getAbsolutePath(), var3);
         }
      }
   }

   @Override
   public final boolean isSupported() {
      return SystemUtils.IS_OS_WINDOWS;
   }
}
