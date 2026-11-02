package org.etlforge.integration.server.backend.webservice.connector;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ETLForgeConnectorEngine extends Remote {
   WsConnectorSchemas getConnectorSchemas(WsConnectorInput var1) throws RemoteException;

   WsStatus testConnection(WsEndpointParameters var1) throws RemoteException;

   WsConnectorAutoDeskItemInfo getAutoDeskItemVersionId(WsEndpointParameters var1, String var2, String var3) throws RemoteException;

   WsConnectorSessionResult login(WsEndpointParameters var1) throws RemoteException;

   WsStatus logout(WsEndpointParameters var1) throws RemoteException;

   WsConnectorSchemaFiles getConnectorSchemaFiles(boolean var1, WsConnectorInput var2) throws RemoteException;

   WsConnectorExecutionResponse executeConnector(WsConnectorInput var1, String var2) throws RemoteException;

   WsStatus executeConnectorFile(WsConnectorInput var1, String var2, String var3) throws RemoteException;

   WsConnectorObjectList getConnectorObjectList(WsConnectorInput var1, String var2) throws RemoteException;
}
