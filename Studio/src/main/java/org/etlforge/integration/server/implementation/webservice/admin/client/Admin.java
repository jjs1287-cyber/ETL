package org.etlforge.integration.server.implementation.webservice.admin.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Admin extends Remote {
   void startEngine(String var1, String var2) throws RemoteException;

   void stopEngine(String var1, String var2) throws RemoteException;

   boolean isEngineRunning(String var1, String var2) throws RemoteException;

   ServerStatus getEngineStatus(String var1, String var2, int var3) throws RemoteException;

   void startScheduler(String var1, String var2) throws RemoteException;

   void stopScheduler(String var1, String var2) throws RemoteException;

   boolean isSchedulerRunning(String var1, String var2) throws RemoteException;

   ServerStatus getSchedulerStatus(String var1, String var2, int var3) throws RemoteException;

   void startJmsEngine(String var1, String var2) throws RemoteException;

   void stopJmsEngine(String var1, String var2) throws RemoteException;

   boolean isJmsEngineRunning(String var1, String var2) throws RemoteException;

   ServerStatus getJmsEngineStatus(String var1, String var2, int var3) throws RemoteException;

   WsProxySettings getProxySettings(String var1, String var2) throws RemoteException;

   void setProxySettings(String var1, String var2, WsProxySettings var3) throws RemoteException;
}
