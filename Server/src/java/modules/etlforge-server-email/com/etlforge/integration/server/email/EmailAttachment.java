package com.etlforge.integration.server.email;

import java.io.File;

public class EmailAttachment {
   private final File path;
   private final String name;

   public EmailAttachment(String var1, String var2) {
      this.path = new File(var1);
      if (!this.path.exists()) {
         throw new IllegalArgumentException("The file to attach does not exist (" + var1 + ")");
      }

      this.name = var2 == null ? new String("") : var2;
   }

   File getPath() {
      return this.path;
   }

   String getName() {
      return this.name;
   }
}
