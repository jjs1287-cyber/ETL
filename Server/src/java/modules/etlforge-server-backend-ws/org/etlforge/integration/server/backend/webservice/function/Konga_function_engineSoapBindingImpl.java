package org.etlforge.integration.server.backend.webservice.function;

import com.google.common.annotations.VisibleForTesting;
import java.rmi.RemoteException;
import org.etlforge.util.exception.NotSupportedException;

public final class Konga_function_engineSoapBindingImpl implements FunctionEngine {
   private final Konga_function_engineImpl impl = new Konga_function_engineImpl();

   @Override
   public WsStartNewFunctionSessionResult startNewFunctionSession(String var1, String var2) throws RemoteException {
      NotSupportedException var3 = new NotSupportedException("Deprecated JIT-7307");
      WsFunctionError var4 = this.createForException(1, var3, "startNewSession");
      return new WsStartNewFunctionSessionResult(null, var4);
   }

   private WsFunctionError createForException(int var1, Exception var2, String var3) {
      return Konga_function_engineImpl.createForException(var1, var2, this.getClass().getName(), var3);
   }

   @Override
   public WsFunctionError closeFunctionSession(WsFunctionSessionId var1) throws RemoteException {
      NotSupportedException var2 = new NotSupportedException("Deprecated-JIT-7307");
      return this.createForException(2, var2, "closeSession");
   }

   @Override
   public WsGetSalesforceServerTimeStampResult getSalesforceServerTimestamp(WsFunctionSessionId var1, WsGetSalesforceServerTimeStampInput var2) throws RemoteException {
      return this.impl.getSalesforceServerTimestamp(var2);
   }

   @Override
   public WsFromDataLoaderDateOutput fromDataLoaderDate(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException {
      return this.impl.fromDataLoaderDate(var2);
   }

   @Override
   public WsFromDataLoaderDateOutput fromDataLoaderDateTime(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException {
      return this.impl.fromDataLoaderDateTime(var2);
   }

   @Override
   public WsFromDataLoaderDateOutput fromDataLoaderTime(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException {
      throw new RemoteException("fromDataLoaderTime not implemented yet.");
   }

   @Override
   public WsConvertTimeZoneOutput convertTimeZone(WsFunctionSessionId var1, WsConvertTimeZoneInput var2) throws RemoteException {
      return this.impl.convertTimeZone(var2);
   }

   @Override
   public WsTestFunctionOutput testFunction(WsFunctionSessionId var1, WsTestFunctionInput var2) throws RemoteException {
      return new WsTestFunctionOutput();
   }

   @Override
   public WsSendEmailOutput sendEmail(WsFunctionSessionId var1, WsSendEmailInput var2) throws RemoteException {
      return this.impl.sendEmail(var2);
   }

   @VisibleForTesting
   static final class ErrorCodes {
      public static final int UNEXPECTED_ERROR = -1;
      public static final int ERROR_HANDLING_FAILED = -2;
      public static final int START_SESSION_FAILED = 1;
      public static final int CLOSE_SESSION_FAILED = 2;
      public static final int MISSING_SESSION = 3;
      public static final int FUNCTION_EVALUATION_FAILED = 4;

      private ErrorCodes() {
      }
   }
}
