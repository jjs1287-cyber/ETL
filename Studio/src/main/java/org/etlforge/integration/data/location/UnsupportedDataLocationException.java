package org.etlforge.integration.data.location;

import org.etlforge.integration.data.SourceTarget;

public class UnsupportedDataLocationException extends RuntimeException {
   public UnsupportedDataLocationException(SourceTarget var1, DataLocationType var2) {
      super(getErrorMessage(var1, var2));
   }

   public UnsupportedDataLocationException(DataLocationType var1) {
      super(getErrorMessage(var1));
   }

   private static String getErrorMessage(SourceTarget var0, DataLocationType var1) {
      String var2 = "The data location type '";
      var2 = var2 + var1.toString();
      var2 = var2 + "' is not supported for ";
      return var2 + var0.name() + "s.";
   }

   private static String getErrorMessage(DataLocationType var0) {
      String var1 = "The data location type '";
      var1 = var1 + var0.toString();
      return var1 + "' is not supported.";
   }
}
