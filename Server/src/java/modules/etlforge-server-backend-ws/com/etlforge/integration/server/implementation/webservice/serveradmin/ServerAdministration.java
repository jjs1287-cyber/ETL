package com.etlforge.integration.server.implementation.webservice.serveradmin;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerAdministration extends Remote {
   String downloadFile(String var1, String var2, String var3) throws RemoteException;

   String downloadServerConfigurationFile(String var1, String var2) throws RemoteException;

   void uploadServerConfigurationFile(String var1, String var2, String var3) throws RemoteException;

   WsServerFileLocators listServerLogFiles(String var1, String var2) throws RemoteException;

   WsServerFileLocators listPluginLogFiles(String var1, String var2) throws RemoteException;

   WsServerFileLocators listDebugLogFiles(String var1, String var2, String var3, String var4) throws RemoteException;

   WsServerFileLocators listOperationLogFiles(String var1, String var2, String var3, String var4) throws RemoteException;
}
