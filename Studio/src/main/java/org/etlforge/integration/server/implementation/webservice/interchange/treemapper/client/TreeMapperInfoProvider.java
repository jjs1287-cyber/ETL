package org.etlforge.integration.server.implementation.webservice.interchange.treemapper.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TreeMapperInfoProvider extends Remote {
   void stop(String var1, String var2, String var3) throws RemoteException;

   String getJtrForWS(String var1, String var2, WebServiceMethodData var3) throws RemoteException;

   String getJtrForCombinedRequestAndResponse(String var1, String var2, String var3, String var4, String var5, String var6, boolean var7) throws RemoteException;

   WsTestTransformationResult testTransformation(String var1, String var2, String var3, String var4, String var5, DebuggingParameters var6) throws RemoteException;

   String getJtrForStructureFile(String var1, String var2, String var3, String var4, boolean var5) throws RemoteException;

   WsTestResult testExpression(String var1, String var2, String var3, String var4, DebuggingParameters var5) throws RemoteException;

   WsTestOperationResult testOperation(String var1, String var2, String var3, String var4, DebuggingParameters var5) throws RemoteException;

   WsDataFile downloadDataFile(String var1, String var2, String var3, String var4, String var5, WsDataFileIdentifier var6) throws RemoteException;

   WsLoadSourceResult loadSourceData(String var1, String var2, WsLoadSourceDataInput var3) throws RemoteException;
}
