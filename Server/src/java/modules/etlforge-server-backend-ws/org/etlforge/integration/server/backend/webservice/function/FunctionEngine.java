package org.etlforge.integration.server.backend.webservice.function;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FunctionEngine extends Remote {
   WsSendEmailOutput sendEmail(WsFunctionSessionId var1, WsSendEmailInput var2) throws RemoteException;

   WsStartNewFunctionSessionResult startNewFunctionSession(String var1, String var2) throws RemoteException;

   WsGetSalesforceServerTimeStampResult getSalesforceServerTimestamp(WsFunctionSessionId var1, WsGetSalesforceServerTimeStampInput var2) throws RemoteException;

   WsFromDataLoaderDateOutput fromDataLoaderDateTime(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException;

   WsFromDataLoaderDateOutput fromDataLoaderDate(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException;

   WsFunctionError closeFunctionSession(WsFunctionSessionId var1) throws RemoteException;

   WsFromDataLoaderDateOutput fromDataLoaderTime(WsFunctionSessionId var1, FromDataLoaderDateInput var2) throws RemoteException;

   WsConvertTimeZoneOutput convertTimeZone(WsFunctionSessionId var1, WsConvertTimeZoneInput var2) throws RemoteException;

   WsTestFunctionOutput testFunction(WsFunctionSessionId var1, WsTestFunctionInput var2) throws RemoteException;
}
