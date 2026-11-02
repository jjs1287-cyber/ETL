package org.etlforge.application.ui.cli;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.ViewPreferencesAction;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.ui.action.ActionUtils;

final class OpenPreferencesExecutor implements CommandExecutor {
   private final ApplicationWindow appWin;

   public OpenPreferencesExecutor(ApplicationWindow var1) {
      this.appWin = var1;
   }

   @Override
   public void execute(String[] var1) throws CommandException {
      ViewPreferencesAction var2 = new ViewPreferencesAction(this.appWin);
      if (var1.length > 0) {
         var2.setInitialPath(var1);
      }

      ActionUtils.run(var2, this, "prefs");
   }
}
