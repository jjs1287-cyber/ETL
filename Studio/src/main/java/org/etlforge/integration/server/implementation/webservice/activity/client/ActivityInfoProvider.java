package org.etlforge.integration.server.implementation.webservice.activity.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ActivityInfoProvider extends Remote {
   WsOperationLogInstance[] getOperationLog(String var1, String var2, WsOperationInstanceFilter var3) throws RemoteException;

   WsOperationFileStatus[] getOperationFiles(String var1, String var2, WsOperationInstanceFilter var3) throws RemoteException;

   long deleteOperationFiles(String var1, String var2, WsOperationInstanceFilter var3) throws RemoteException;

   WsOperationProgress[] getOperationProgress(String var1, String var2, WsOperationInstanceFilter var3) throws RemoteException;

   String[] getSourceFileHistoryLog(String var1, String var2, WsOperationInstanceFilter var3) throws RemoteException;

   long[] deleteSourceFileHistory(String var1, String var2, long[] var3) throws RemoteException;

   long[] deleteOperationLogEntry(String var1, String var2, long[] var3) throws RemoteException;

   long[] cancelOperations(String var1, String var2, long[] var3) throws RemoteException;
}
