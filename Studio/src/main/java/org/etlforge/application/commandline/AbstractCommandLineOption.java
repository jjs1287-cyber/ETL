package org.etlforge.application.commandline;

public abstract class AbstractCommandLineOption implements CommandLineOption {
   private final String name;
   private final String longName;
   private final String description;
   private final boolean hasArgument;
   private final boolean required;

   protected AbstractCommandLineOption(String var1, String var2, String var3, boolean var4, boolean var5) {
      this.name = var1;
      this.longName = var2;
      this.description = var3;
      this.hasArgument = var4;
      this.required = var5;
   }

   @Override
   public final String getDescription() {
      return this.description;
   }

   @Override
   public final String getLongName() {
      return this.longName;
   }

   @Override
   public final String getName() {
      return this.name;
   }

   @Override
   public final boolean hasArgument() {
      return this.hasArgument;
   }

   @Override
   public final boolean isRequired() {
      return this.required;
   }
}
