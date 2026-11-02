package org.etlforge.integration.data.script.de;

public enum AccessType {
   GET("Read"),
   SET("Write"),
   UNKNOWN("?");

   private final String display;

   AccessType(String var3) {
      this.display = var3;
   }

   @Override
   public String toString() {
      return this.display;
   }
}
