package org.etlforge.integration.server.backend.webservice.jdbc;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JdbcEngine extends Remote {
   WsJdbcStartNewSessionResult startNewSession(String var1, String var2) throws RemoteException;

   WsJdbcError closeSession(WsJdbcSessionId var1) throws RemoteException;

   WsDbLookupResult dbLookup(WsJdbcSessionId var1, WsDbLookupParams var2) throws RemoteException;

   WsDbLookupResult cachedDbLookup(WsJdbcSessionId var1, WsDbLookupParams var2) throws RemoteException;

   WsDbExecuteResult dbExecute(WsJdbcSessionId var1, WsDbLookupParams var2, int var3) throws RemoteException;

   WsJdbcError commitTransactions(WsJdbcSessionId var1) throws RemoteException;

   WsJdbcError rollbackTransactions(WsJdbcSessionId var1) throws RemoteException;

   WsJdbcError readSource(WsJdbcSessionId var1, WsReadSourceParams var2) throws RemoteException;

   WsJdbcError writeTarget(WsJdbcSessionId var1, WsWriteTargetParams var2) throws RemoteException;

   WsSPResult callStoredProcedure(WsJdbcSessionId var1, WsSPParams var2) throws RemoteException;
}
