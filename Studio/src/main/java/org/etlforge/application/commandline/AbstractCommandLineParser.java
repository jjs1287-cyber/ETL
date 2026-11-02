package org.etlforge.application.commandline;

import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;

public abstract class AbstractCommandLineParser implements CommandLineParser {
   private final CommandLineOptionCollection options;
   private List<CommandLineOptionException> errors;

   protected AbstractCommandLineParser(CommandLineOptionCollection var1) {
      this.options = var1;
   }

   @Override
   public void addOption(CommandLineOption var1) {
      this.options.add(var1);
   }

   @Override
   public CommandLineOptionCollection getOptions() {
      return this.options;
   }

   @Override
   public void parse(String[] var1) throws CommandLineParsingException {
      try {
         this.parseImpl(var1);
         this.checkErrors();
      } catch (RuntimeException var3) {
         throw new CommandLineParsingException("An unexpected error occurred.", var3);
      }
   }

   protected abstract void parseImpl(String[] var1) throws CommandLineParsingException;

   private void checkErrors() {
      if (this.errors != null) {
         ArrayList var1 = new ArrayList();

         for (CommandLineOptionException var3 : this.errors) {
            var1.add(var3.getMessage());
         }

         StringBuilder var5 = new StringBuilder();

         for (Object __etlItem47 : var1) {
            String var4 = (String) __etlItem47;
            var5.append(var4).append("\n\n");
         }

         ErrorLog.log(this.getClass(), var5.toString(), new Exception(var5.toString()));
      }
   }

   protected void handleException(CommandLineOptionException var1) throws CommandLineParsingException {
      if (var1.fatal()) {
         throw var1;
      }

      if (this.errors == null) {
         this.errors = new ArrayList<>();
      }

      this.errors.add(var1);
   }
}
