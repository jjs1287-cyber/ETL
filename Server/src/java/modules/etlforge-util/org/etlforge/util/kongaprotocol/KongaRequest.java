package org.etlforge.util.kongaprotocol;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.etlforge.util.net.Command;

public class KongaRequest extends KongaPacket implements Iterable<Command> {
   private final String userName;
   private final String password;
   private final List<Command> commands;

   public KongaRequest(String var1, String var2, String var3) {
      super(var1);
      this.userName = var2;
      this.password = var3;
      this.commands = new ArrayList<>();
   }

   public void addCommand(Command var1) {
      this.commands.add(var1);
   }

   public String getUserName() {
      return this.userName;
   }

   public String getPassword() {
      return this.password;
   }

   @Override
   public Iterator<Command> iterator() {
      return this.commands.iterator();
   }

   public int getNumberOfCommands() {
      return this.commands.size();
   }
}
