package com.etlforge.integration.server.implementation.webservice.license;

import java.rmi.RemoteException;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.ws.WsUtils;

abstract class LicenseTask {
   private final String name;
   private final String user;
   private final String password;

   protected LicenseTask(String var1, String var2, String var3) {
      this.name = var1;
      this.user = var2;
      this.password = var3;
   }

   public final WsKeyValue[] execute() throws RemoteException {
      ExternalApiSupport var1 = null;

      try {
         var1 = ExternalApiSupport.forCall(this.user, this.password);
         if (!var1.isAdmin()) {
            throw new RemoteException("User " + this.user + " is not an administrator.");
         }

         return this.runTask(var1);
      } catch (Exception var7) {
         WsUtils.convertError(var7, this.getClass(), this.name);
      } finally {
         ExternalApiSupport.safeDispose(var1);
      }

      return null;
   }

   protected abstract WsKeyValue[] runTask(ExternalApiSupport var1) throws Exception;
}
