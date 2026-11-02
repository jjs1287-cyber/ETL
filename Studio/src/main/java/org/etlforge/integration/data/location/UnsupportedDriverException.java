package org.etlforge.integration.data.location;

public class UnsupportedDriverException extends Exception {
   private final String driverName;

   public UnsupportedDriverException(String var1) {
      super("The driver \"" + var1 + "\" is not supported by the integration system.");
      this.driverName = var1;
   }

   public String getDriverName() {
      return this.driverName;
   }
}
