package org.etlforge.application.commandline;

import org.etlforge.system.SystemUtils;

public class CommandLineOptionFormatter {
   private final int leftMargin;
   private final String rn;

   public CommandLineOptionFormatter() {
      this(30);
   }

   public CommandLineOptionFormatter(int var1) {
      this.leftMargin = var1;
      this.rn = SystemUtils.LINE_SEPARATOR;
   }

   public String getUsageMessage(String var1, CommandLineOptionCollection var2) {
      StringBuilder var3 = new StringBuilder(var1);
      var3.append(this.rn).append("Options:").append(this.rn);
      this.appendOptions(var2.getRequiredOptions(), "Required", var3);
      this.appendOptions(var2.getOptionalOptions(), "Optional", var3);
      return var3.toString();
   }

   private void appendOptions(CommandLineOption[] var1, String var2, StringBuilder var3) {
      if (var1 != null && var1.length != 0) {
         var3.append(var2).append(":").append(this.rn);

         for (CommandLineOption var7 : var1) {
            String var8 = var7.getName();
            String var9 = var7.getLongName();
            var3.append("  -");
            var3.append(var8);
            var3.append(" [--");
            var3.append(var9);
            var3.append("] ");

            for (int var10 = var8.length() + var9.length() + 10; var10 < this.leftMargin; var10++) {
               var3.append(" ");
            }

            var3.append(var7.getDescription());
            var3.append(this.rn);
         }
      }
   }
}
