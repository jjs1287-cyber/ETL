package org.etlforge.integration.server.implementation.webservice.sfdc;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.etlforge.integration.server.implementation.webservice.WsCredentials;

public interface SalesforceInfoProvider extends Remote {
   WsLoginResult openConnection(WsCredentials var1, WsOrgIdentifier var2) throws RemoteException;

   WsQueryResult query(WsCredentials var1, WsSessionData var2, String var3) throws RemoteException;

   WsQueryResult queryAll(WsCredentials var1, WsSessionData var2, String var3) throws RemoteException;

   WsDescribeObjectsResult describeObjects(WsCredentials var1, WsSessionData var2, String[] var3) throws RemoteException;

   WsSalesforceError queryAllToFile(WsCredentials var1, WsSessionData var2, String var3, String var4) throws RemoteException;

   WsDescribeObjectsResult listAllObjects(WsCredentials var1, WsSessionData var2) throws RemoteException;

   WsDescribeObjectsResult describeObject(WsCredentials var1, WsSessionData var2, String var3) throws RemoteException;

   WsSerializedObjectList listAllObjects2(WsCredentials var1, WsSessionData var2) throws RemoteException;
}
