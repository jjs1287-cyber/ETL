package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JdbcInfoProvider extends Remote {
   WsJdbcDatabaseInfo getAllDatabaseObjects(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6) throws RemoteException;

   WsJdbcDatabaseInfo getDatabaseObjects(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6, String[] var7, String var8) throws RemoteException;

   WsJdbcDatabaseInfo getTableInfo(
      String var1, String var2, WsConnectionParams var3, String var4, WsJdbcBeginEndQuote var5, String var6, String var7, WsJdbcDatabaseObject[] var8
   ) throws RemoteException;

   WsJdbcDriverInfo getDrivers(String var1, String var2) throws RemoteException;

   WsJdbcBeginEndQuote testConnection(String var1, String var2, WsConnectionParams var3, String var4, String var5, String var6) throws RemoteException;
}
