package org.etlforge.integration.data.script;

import org.etlforge.system.ArgCheck;

public final class ArgumentDescriptor {
   private final String label;
   private final String description;
   private final String defaultValue;
   private final boolean optional;

   public ArgumentDescriptor(String var1, String var2, String var3, boolean var4) {
      this.label = ArgCheck.checkNotEmpty(var1, "Must provide a non-empty label");
      this.description = var2 == null ? "" : var2;
      this.defaultValue = var3;
      this.optional = var4;
   }

   public String getLabel() {
      return this.label;
   }

   public String getDescription() {
      return this.description;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public boolean isOptional() {
      return this.optional;
   }
}
