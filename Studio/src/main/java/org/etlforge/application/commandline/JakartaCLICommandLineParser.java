package org.etlforge.application.commandline;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.etlforge.application.log.ApplicationLog;

public final class JakartaCLICommandLineParser extends AbstractCommandLineParser {
   public JakartaCLICommandLineParser() {
      super(new JakartaCLICommandLineParser.OptionsCollection());
   }

   public JakartaCLICommandLineParser(Collection<? extends CommandLineOption> var1) {
      this();

      for (CommandLineOption var3 : var1) {
         this.addOption(var3);
      }
   }

   @Override
   public void parseImpl(String[] var1) throws CommandLineParsingException {
      try {
         BasicParser var2 = new BasicParser();
         JakartaCLICommandLineParser.OptionsCollection var9 = this.getOptions();
         CommandLine var4 = var2.parse(var9.internalCollection, var1);
         this.processCommandLine(var4);
      } catch (MissingOptionException var5) {
         String var3 = "Missing required options: " + var5.getMessage();
         throw new CommandLineParsingException(var3);
      } catch (MissingArgumentException var6) {
         throw new CommandLineParsingException(var6.getMessage());
      } catch (UnrecognizedOptionException var7) {
         throw new CommandLineParsingException(var7.getMessage());
      } catch (ParseException var8) {
         throw new CommandLineParsingException(var8);
      }
   }

   public JakartaCLICommandLineParser.OptionsCollection getOptions() {
      return (JakartaCLICommandLineParser.OptionsCollection)super.getOptions();
   }

   private void processCommandLine(CommandLine var1) throws CommandLineParsingException {
      this.processRequiredOptions(var1);
      this.processOptionalOptions(var1);
   }

   private void processRequiredOptions(CommandLine var1) throws CommandLineParsingException {
      JakartaCLICommandLineParser.OptionsCollection var2 = this.getOptions();

      for (CommandLineOption var6 : var2.getRequiredOptions()) {
         try {
            this.processRequiredOption(var1, var6);
         } catch (CommandLineOptionException var8) {
            this.handleException(var8);
         }
      }
   }

   private void processRequiredOption(CommandLine var1, CommandLineOption var2) throws CommandLineOptionException {
      String var3 = var2.getName();
      if (var1.hasOption(var3)) {
         String var4 = var1.getOptionValue(var3);
         ApplicationLog.getMainLogger().info("Processing startup option \"" + var3 + "\": " + var4);
         var2.handleArgument(var4);
      } else {
         throw new CommandLineOptionException("Missing required startup argument: " + var3, true);
      }
   }

   private void processOptionalOptions(CommandLine var1) throws CommandLineParsingException {
      JakartaCLICommandLineParser.OptionsCollection var2 = this.getOptions();

      for (CommandLineOption var6 : var2.getOptionalOptions()) {
         try {
            this.processOptionalOption(var1, var6);
         } catch (CommandLineOptionException var8) {
            this.handleException(var8);
         }
      }
   }

   private void processOptionalOption(CommandLine var1, CommandLineOption var2) throws CommandLineOptionException {
      String var3 = var2.getName();
      String var4 = var1.getOptionValue(var3);
      ApplicationLog.getMainLogger().info("Processing startup option \"" + var3 + "\": " + var4);
      if (var1.hasOption(var2.getName())) {
         var2.handleArgument(var4);
      }
   }

   private static class OptionsCollection implements CommandLineOptionCollection {
      private final Options internalCollection = new Options();
      private final Map<Option, CommandLineOption> required = Maps.newIdentityHashMap();
      private final Map<Option, CommandLineOption> optional = Maps.newIdentityHashMap();

      public OptionsCollection() {
      }

      @Override
      public void add(CommandLineOption var1) {
         Option var2 = this.convert(var1);
         this.internalCollection.addOption(var2);
         if (var1.isRequired()) {
            this.required.put(var2, var1);
         } else {
            this.optional.put(var2, var1);
         }
      }

      @Override
      public CommandLineOption[] getOptionalOptions() {
         return this.getOptionsFromMap(this.optional);
      }

      @Override
      public CommandLineOption[] getRequiredOptions() {
         return this.getOptionsFromMap(this.required);
      }

      private CommandLineOption[] getOptionsFromMap(Map<Option, CommandLineOption> var1) {
         ArrayList var2 = new ArrayList();

         for (Object var4 : this.internalCollection.getOptions()) {
            if (var4 instanceof Option && var1.containsKey(var4)) {
               var2.add(var1.get(var4));
            }
         }

         return (CommandLineOption[]) var2.toArray(new CommandLineOption[var2.size()]);
      }

      private Option convert(CommandLineOption var1) {
         Option var2 = new Option(var1.getName(), var1.getLongName(), var1.hasArgument(), var1.getDescription());
         var2.setRequired(var1.isRequired());
         return var2;
      }
   }
}
