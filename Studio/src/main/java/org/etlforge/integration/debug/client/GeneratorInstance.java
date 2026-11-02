package org.etlforge.integration.debug.client;

public final class GeneratorInstance {
   private final String path;
   private final String instanceString;

   public GeneratorInstance(String var1, String var2) {
      if (var1 != null && !var1.startsWith("[")) {
         this.path = "[" + var1 + "]";
      } else {
         this.path = var1;
      }

      this.instanceString = var2;
   }

   public String getPath() {
      return this.path;
   }

   public String getInstanceString() {
      return this.instanceString;
   }
}
