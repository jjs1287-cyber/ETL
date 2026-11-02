package com.etlforge.integration.server.implementation.webservice.bulkloadinfo;

import com.google.common.base.Function;
import java.rmi.RemoteException;
import java.util.List;
import java.util.TimeZone;
import org.etlforge.integration.common.server.ServerDateFormat;
import org.etlforge.integration.server.ExternalApiSupport;
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

   public final T[] execute() throws RemoteException {
      ExternalApiSupport var1 = null;

      try {
         var1 = ExternalApiSupport.forCall(this.etlforgeUser, this.etlforgePassword);
         return this.runTask(var1);
      } catch (Exception var7) {
         WsUtils.convertError(var7, this.getClass(), this.name);
      } finally {
         ExternalApiSupport.safeDispose(var1);
      }

      return null;
   }

   protected abstract T[] runTask(ExternalApiSupport var1) throws Exception;

   protected final <F> void populateResponse(T[] var1, List<F> var2, Function<F, T> var3) {
      int var4 = 0;

      for (F var6 : var2) {
         var1[var4++] = var3.apply(var6);
      }
   }

   protected static final ServerDateFormat getDateFormat() {
      return new ServerDateFormat(TimeZone.getDefault());
   }
}
