package org.etlforge.util.kongaprotocol;

import java.util.Iterator;
import org.etlforge.util.net.Arguments;
import org.etlforge.util.net.Command;

public class KongaCommand implements Command {
   private final String commandName;
   private final KongaArguments arguments;

   public KongaCommand(String var1) {
      this(var1, new String[0]);
   }

   public KongaCommand(String var1, String[] var2) {
      this.commandName = var1;
      this.arguments = new KongaArguments();

      for (String var6 : var2) {
         this.arguments.add(var6);
      }
   }

   @Override
   public String getCommandName() {
      return this.commandName;
   }

   @Override
   public Arguments getArguments() {
      return this.arguments;
   }

   public Iterator<String> iterator() {
      return this.arguments.iterator();
   }
}
