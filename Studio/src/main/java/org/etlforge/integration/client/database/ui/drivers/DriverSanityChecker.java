package org.etlforge.integration.client.database.ui.drivers;

import org.etlforge.application.ui.Alert;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.lang.exception.ExceptionHandler;

public final class DriverSanityChecker {
   public static boolean isDriverValid(DatabaseLocation var0, SourceTarget var1, ExceptionHandler<Throwable> var2) {
      boolean var3 = true;
      if (var0.getDriver() == null) {
         String var4 = var0.getDriverName();
         if (var4 != null && var4.length() != 0) {
            onNoDriverInformationFound(var1, var2);
         } else {
            onNoDriverSpecified(var1, var2);
         }

         var3 = false;
      }

      return var3;
   }

   public static boolean isDriverValid(DatabaseLocation var0, SourceTarget var1) {
      return isDriverValid(var0, var1, null);
   }

   private static void onNoDriverSpecified(SourceTarget var0, ExceptionHandler<Throwable> var1) {
      String var2 = "Missing driver information";
      StringBuilder var3 = new StringBuilder("No driver has been specified for the selected ");
      var3.append(var0 == SourceTarget.Source ? "source." : "target.");
      handleError(var2, var3.toString(), var1);
   }

   private static void onNoDriverInformationFound(SourceTarget var0, ExceptionHandler<Throwable> var1) {
      String var2 = "Missing driver information";
      StringBuilder var3 = new StringBuilder("Cannot find the driver information for the selected ");
      var3.append(var0 == SourceTarget.Source ? "source" : "target");
      var3.append(". Please make sure that the specified driver comes from the correct server.");
      handleError(var2, var3.toString(), var1);
   }

   private static void handleError(String var0, String var1, ExceptionHandler<Throwable> var2) {
      if (var2 != null) {
         var2.caught(new Exception(var0 + ": " + var1));
      } else {
         Alert.error(var1, var0);
      }
   }

   private DriverSanityChecker() {
   }
}
