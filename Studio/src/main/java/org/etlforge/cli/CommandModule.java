package org.etlforge.cli;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.etlforge.lang.KongaStringUtils;

public final class CommandModule implements CommandCollection {
   private final List<Command> commands = new CopyOnWriteArrayList<>();
   private CommandExecutor executor;
   private CommandException lastError;

   public void addCommand(Command var1) {
      this.commands.add(var1);
   }

   public void addCommands(Command... var1) {
      this.commands.addAll(Arrays.asList(var1));
   }

   public void addCommands(Iterable<? extends Command> var1) {
      Iterables.addAll(this.commands, var1);
   }

   public void removeCommand(Command var1) {
      this.commands.remove(var1);
   }

   public boolean isEmpty() {
      return this.commands.isEmpty();
   }

   public Result execute(String var1) {
      this.lastError = null;
      ListIterator var2 = this.commands.listIterator(this.commands.size());

      while (var2.hasPrevious()) {
         Command var3 = (Command)var2.previous();
         String[] var4 = var3.parse(var1);
         if (var4 != null) {
            try {
               CommandExecutor var5 = var3.executor();
               var5.execute(var4);
               return Result.SUCCESS;
            } catch (CommandException var6) {
               this.lastError = var6;
               return Result.ERROR;
            }
         }
      }

      return Result.UNKNOWN_COMMAND;
   }

   public CommandException lastError() {
      return this.lastError;
   }

   @Override
   public Iterator<Command> iterator() {
      return Collections.unmodifiableList(this.commands).iterator();
   }

   @Override
   public String getSyntax() {
      return null;
   }

   @Override
   public CommandExecutor executor() {
      return this.executor;
   }

   @Override
   public String[] parse(String var1) {
      this.executor = null;

      for (Command var3 : this.commands) {
         String[] var4 = var3.parse(var1);
         if (var4 != null) {
            this.executor = new CommandModule.ExecutorWrapper(var3.executor());
            return var4;
         }
      }

      return null;
   }

   @Override
   public Set<String> collectCompletions(String var1) {
      HashSet var2 = Sets.newHashSet();

      for (Command var4 : this.commands) {
         if (var4 instanceof CommandCollection) {
            var2.addAll(((CommandCollection)var4).collectCompletions(var1));
         } else {
            String var5 = var4.complete(var1);
            if (var5 != null) {
               var2.add(var5);
            }
         }
      }

      return var2;
   }

   @Override
   public String complete(String var1) {
      Set var2 = this.collectCompletions(var1);
      return var2.isEmpty() ? null : this.findLongestCompletion(var2);
   }

   private String findLongestCompletion(Set<String> var1) {
      String var2 = KongaStringUtils.findLongestCommonStartFragment(var1, true);
      return var2.isEmpty() ? null : var2;
   }

   private static class ExecutorWrapper implements CommandExecutor {
      private CommandExecutor wrappee;

      public ExecutorWrapper(CommandExecutor var1) {
         this.wrappee = var1;
      }

      @Override
      public void execute(String[] var1) throws CommandException {
         try {
            this.wrappee.execute(var1);
         } finally {
            this.wrappee = null;
         }
      }
   }
}
