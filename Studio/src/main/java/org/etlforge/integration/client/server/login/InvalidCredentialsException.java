package org.etlforge.integration.client.server.login;

import java.rmi.RemoteException;
import org.apache.commons.lang.StringUtils;

public final class InvalidCredentialsException extends Exception {
   public static boolean isInvalidCredentialsError(RemoteException var0) {
      String var1 = var0.getMessage();
      if (StringUtils.isEmpty(var1)) {
         return false;
      }

      String var2 = "java.rmi.RemoteException: ";
      if (var1.startsWith(var2)) {
         var1 = StringUtils.substringAfter(var1, var2);
      }

      return StringUtils.startsWithIgnoreCase(var1, "Invalid user name and/or password");
   }

   public InvalidCredentialsException(Throwable var1) {
      super("Invalid user name and/or password.", var1);
   }
}
