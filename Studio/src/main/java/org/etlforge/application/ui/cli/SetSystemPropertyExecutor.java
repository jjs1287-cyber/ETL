package org.etlforge.application.ui.cli;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import org.etlforge.cli.CommandException;
import org.etlforge.cli.CommandExecutor;

public final class SetSystemPropertyExecutor implements CommandExecutor {
   @Override
   public void execute(String[] var1) throws CommandException {
      if (var1.length == 1) {
         try {
            StringReader var2 = new StringReader(var1[0]);
            Properties var3 = new Properties();
            var3.load(var2);
            System.setProperties(var3);
         } catch (IOException var4) {
            throw new CommandException(var4);
         }
      }
   }
}
