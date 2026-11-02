package org.etlforge.integration.client.runtime.lock;

import javax.swing.JFrame;
import org.etlforge.application.commandline.CommandLineParsingException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.runtime.ClientPlatform;
import org.etlforge.integration.client.runtime.FileInputArgumentHandler;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.util.kongaprotocol.KongaCommandResult;
import org.etlforge.util.net.Command;
import org.etlforge.util.net.CommandHandler;
import org.etlforge.util.net.CommandResult;

final class RunningInstanceDispatcher implements CommandHandler {
   private final FileInputArgumentHandler argHandler;

   public RunningInstanceDispatcher(FileInputArgumentHandler var1) {
      this.argHandler = var1;
   }

   @Override
   public CommandResult handleCommand(Command var1) {
      String[] var2 = var1.getArguments().toArray();
      if (var2.length > 0) {
         this.handleArguments(var2);
      }

      JFrame var3 = UiUtils.getActiveFrame();
      if (var3 != null) {
         var3.toFront();
      }

      KongaCommandResult var4 = new KongaCommandResult("to_front");
      var4.addReturnValue("OK");
      return var4;
   }

   private void handleArguments(String[] var1) {
      try {
         ClientPlatform.parseCommandLineArguments(var1);
         this.argHandler.process();
      } catch (CommandLineParsingException var3) {
         ErrorLog.attention(this, "Failed to parse the command-line arguments.", var3);
      }
   }
}
