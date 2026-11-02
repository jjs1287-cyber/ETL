package org.etlforge.integration.server.ws;

import java.rmi.RemoteException;
import java.sql.SQLException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.server.ServerDebugLog;
import org.etlforge.integration.server.ServerUiBackendLog;
import org.etlforge.integration.server.authentication.AuthenticateUser;

public final class WsUtils {
   public static void authenticate(String var0, String var1) throws RemoteException {
      try {
         if (!AuthenticateUser.isValidUser(var0, var1)) {
            throw new RemoteException("Invalid user name and/or password.");
         }
      } catch (SQLException var3) {
         throw new RemoteException(var3.getMessage(), var3);
      }
   }

   public static void authenticateAdmin(String var0, String var1) throws RemoteException {
      try {
         if (!AuthenticateUser.isValidAdminUser(var0, var1)) {
            throw new RemoteException("User " + var0 + " is not an administrator.");
         }
      } catch (SQLException var3) {
         throw new RemoteException(var3.getMessage(), var3);
      }
   }

   public static void appendStackTrace(Exception var0, StringBuilder var1) {
      Throwable var2 = var0.getCause();
      if (var2 != null) {
         ServerErrors.appendStacktrace(var2, var1);
      }
   }

   private static String escapeExceptionMessage(String var0) {
      return var0 == null ? "" : var0.replace('\u0000', ' ');
   }

   public static String escapeExceptionMessageAndGetCause(Throwable var0) {
      StringBuilder var1 = new StringBuilder();
      String var2 = var0.getMessage();
      if (var2 != null) {
         var1.append(escapeExceptionMessage(var2));
      } else {
         var1.append("[The error was of type " + var0.getClass().getName() + ", but no additional error information was provided]");
      }

      Throwable var3 = var0.getCause();
      if (var3 != null && var3.getMessage() != null) {
         var1.append("\nCaused by: ").append(var3.getMessage());
      }

      return var1.toString();
   }

   public static void convertError(Throwable var0, Class var1, String var2) throws RemoteException {
      var0.printStackTrace();
      if (var0 instanceof RemoteException) {
         throw (RemoteException)var0;
      }

      RemoteException var3 = null;

      try {
         StringBuilder var4 = new StringBuilder(escapeExceptionMessageAndGetCause(var0));
         var4.append("\n[The error occurred in ").append(var2).append("]");
         ServerErrors.appendStacktrace(var0, var4);
         var3 = new RemoteException(var4.toString());
         ServerUiBackendLog.LOG.throwing(var1.getName(), var2, var3);
         throw var3;
      } catch (RuntimeException var5) {
         handleUnexpectedErrorFromErrorHandling(var5, var1, var2);
         throw var5;
      }
   }

   public static void handleUnexpectedErrorFromErrorHandling(Exception var0, Class<?> var1, String var2) throws RemoteException {
      String var3 = "An error occurred and then another error occurred while we tried to log that error: ";
      var3 = var3 + var0.getClass().getName() + "\n" + ExceptionUtils.getFullStackTrace(var0);
      RemoteException var4 = new RemoteException(var3);
      ServerDebugLog.LOG.throwing(var1.getName(), var2, var4);
      throw var4;
   }

   private WsUtils() {
   }
}
