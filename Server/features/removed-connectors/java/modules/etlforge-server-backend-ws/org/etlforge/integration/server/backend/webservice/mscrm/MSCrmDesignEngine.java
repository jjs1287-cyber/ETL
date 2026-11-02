package org.etlforge.integration.server.backend.webservice.mscrm;

import java.rmi.Remote;
import java.rmi.RemoteException;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;

public interface MSCrmDesignEngine extends Remote {
   WsQueryResult runQuery(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException;

   WsQueryResponseSchema getQueryResponseSchema(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException;

   WsMetaDataResponse getObjectDescriptions(String var1, String var2, WsEndpointParameters var3) throws RemoteException;

   WsSavedQueries getSavedQueries(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException;

   WsMetaDataResponse getDetailedMetaData(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException;
}
