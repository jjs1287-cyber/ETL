package org.etlforge.cli;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.Action;
import org.etlforge.cli.ui.ActionCommand;

public class DefaultCommandCollection implements CommandCollection {
   protected final CommandModule commands = new CommandModule();

   public DefaultCommandCollection(Command... var1) {
      for (Command var5 : var1) {
         this.add(var5);
      }
   }

   public final void add(Command... var1) {
      for (Command var5 : var1) {
         this.commands.addCommand(var5);
      }
   }

   public final void add(Iterable<Command> var1) {
      for (Command var3 : var1) {
         this.commands.addCommand(var3);
      }
   }

   public final void setActions(Map<String, Action> var1) {
      for (Entry var3 : var1.entrySet()) {
         String var4 = (String)var3.getKey();
         Action var5 = (Action)var3.getValue();
         this.add(new ActionCommand(var4, var5));
      }
   }

   @Override
   public final Iterator<Command> iterator() {
      return this.commands.iterator();
   }

   @Override
   public final String getSyntax() {
      return null;
   }

   @Override
   public final CommandExecutor executor() {
      return this.commands.executor();
   }

   @Override
   public final String[] parse(String var1) {
      return this.commands.parse(var1);
   }

   @Override
   public final String complete(String var1) {
      return this.commands.complete(var1);
   }

   @Override
   public final Set<String> collectCompletions(String var1) {
      return this.commands.collectCompletions(var1);
   }
}
