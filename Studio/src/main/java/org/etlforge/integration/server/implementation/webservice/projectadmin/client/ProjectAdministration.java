package org.etlforge.integration.server.implementation.webservice.projectadmin.client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.etlforge.integration.server.implementation.webservice.WsKeyValuePairs;

public interface ProjectAdministration extends Remote {
   void disableSchedules(String var1, String var2, String[] var3) throws RemoteException;

   void enableSchedules(String var1, String var2, String[] var3) throws RemoteException;

   void disableScheduleForOperation(String var1, String var2, String[] var3) throws RemoteException;

   void enableScheduleForOperation(String var1, String var2, String[] var3) throws RemoteException;

   FindScheduledOperationsResult findScheduledOperations(String var1, String var2, String[] var3, String[] var4, String var5, String var6) throws RemoteException;

   FindScheduledOperationsResult testScheduleWithKeyValues(String var1, String var2, WsKeyValuePairs var3, String var4, String var5, int var6) throws RemoteException;

   FindScheduledOperationsResult testScheduleWithGuid(String var1, String var2, String var3, String var4, String var5, int var6) throws RemoteException;
}
