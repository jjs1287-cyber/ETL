package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OdbcInfoProvider extends Remote {
   WsDatabaseInfo getDatabaseObjects(String var1, String var2, String var3, String var4, String var5, String var6, String[] var7, String var8) throws RemoteException;

   WsDatabaseInfo getTableInfo(String var1, String var2, String var3, String var4, String var5, String var6, WsDatabaseObject[] var7) throws RemoteException;

   WsDatabaseDriverInfo getDrivers(String var1, String var2) throws RemoteException;
}
