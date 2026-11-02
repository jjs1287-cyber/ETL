package org.etlforge.integration.server.implementation.webservice.interchange.jobcontrol.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JobControl extends Remote {
   void runOperation(String var1, String var2, String var3) throws RemoteException;

   void runOperationMD5Password(String var1, String var2, String var3) throws RemoteException;

   WsRunOperationResult runOperationWithSourceData(String var1, String var2, String var3, String var4) throws RemoteException;

   WsRunOperationResult runOperationInstanceMD5Password(String var1, String var2, String var3) throws RemoteException;
}
