package org.etlforge.application.ui.cli;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.cli.Command;
import org.etlforge.cli.CommandExecutor;
import org.etlforge.cli.CommandFactory;
import org.etlforge.cli.DefaultCommandCollection;
import org.etlforge.cli.SingleArgumentCommand;

public final class ApplicationCommands extends DefaultCommandCollection {
   private final ApplicationWindow appWin;

   public ApplicationCommands(ApplicationWindow var1) {
      super();
      this.appWin = var1;
      this.add(CommandFactory.newCommand("export errors to {clipboard|file} [{filename}]", new ExportErrorLogExecutor()));
      this.add(CommandFactory.newCommand("switch to {0}...", new SwitchToEditorExecutor(var1.getEditorService())));
      this.add(CommandFactory.newCommand("last error", new ShowLastErrorExecutor()));
      this.add(CommandFactory.newCommand("copy last error", new CopyLastErrorExecutor()));
      this.add(CommandFactory.newCommand("kill", new KillExecutor()));
      this.add(CommandFactory.newCommand("close [{0}]", new CloseEditorExecutor(var1)));
      this.add(CommandFactory.newCommand("preferences [{0}]...", new OpenPreferencesExecutor(var1)));
      this.add(CommandFactory.newCommand("set_sysprop {0}", new SetSystemPropertyExecutor()));
      this.add(this.switchView());
   }

   private Command switchView() {
      return new SingleArgumentCommand("view", new CommandExecutor() {
         @Override
         public void execute(String[] var1) {
            ApplicationCommands.this.appWin.getViewManager().showView(var1[0]);
         }
      });
   }
}
