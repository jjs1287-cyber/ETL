package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.rmi.RemoteException;
import org.etlforge.integration.server.ws.WsUtils;

abstract class Task<T> {
   private final String name;
   private final String etlforgeUser;
   private final String etlforgePassword;

   protected Task(String var1, String var2, String var3) {
      this.name = var1;
      this.etlforgeUser = var2;
      this.etlforgePassword = var3;
   }

   public final T execute() throws RemoteException {
      try {
         WsUtils.authenticateAdmin(this.etlforgeUser, this.etlforgePassword);
         return this.runTask();
      } catch (Exception var2) {
         WsUtils.convertError(var2, this.getClass(), this.name);
         return null;
      }
   }

   protected abstract T runTask() throws Exception;
}
