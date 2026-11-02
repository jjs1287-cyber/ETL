package org.etlforge.application.ui.cli;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;

final class CloseEditorExecutor implements CommandExecutor {
   private final ApplicationWindow appWin;

   public CloseEditorExecutor(ApplicationWindow var1) {
      this.appWin = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      switch (var1.length) {
         case 0:
            this.closeActiveEditor();
            break;
         case 1:
            String var2 = var1[0];
            if ("all".equals(var2)) {
               this.appWin.getEditorService().closeAllEditors();
            } else if ("others".equals(var2)) {
               this.closeOtherEditors();
            }
      }
   }

   private void closeActiveEditor() {
      Editor var1 = this.appWin.getEditorService().getActiveEditor();
      if (var1 != null) {
         this.appWin.getEditorService().closeEditor(var1);
      }
   }

   private void closeOtherEditors() {
      Editor var1 = this.appWin.getEditorService().getActiveEditor();
      if (var1 != null) {
         this.appWin.getEditorService().closeOtherEditors(var1);
      }
   }
}
