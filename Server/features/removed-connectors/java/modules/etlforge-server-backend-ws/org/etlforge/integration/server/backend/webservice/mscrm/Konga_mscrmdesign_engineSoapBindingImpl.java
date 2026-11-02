package org.etlforge.integration.server.backend.webservice.mscrm;

import java.rmi.RemoteException;
import org.etlforge.integration.server.backend.webservice.connector.WsEndpointParameters;

public final class Konga_mscrmdesign_engineSoapBindingImpl implements MSCrmDesignEngine {
   @Override
   public WsMetaDataResponse getObjectDescriptions(String var1, String var2, WsEndpointParameters var3) throws RemoteException {
      GetObjectDescriptionsTask var4 = new GetObjectDescriptionsTask(var1, var2, var3);
      return var4.execute();
   }

   @Override
   public WsMetaDataResponse getDetailedMetaData(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException {
      GetDetailedMetaDataTask var5 = new GetDetailedMetaDataTask(var1, var2, var3, var4);
      return var5.execute();
   }

   @Override
   public WsQueryResponseSchema getQueryResponseSchema(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException {
      GetQueryResponseSchemaTask var5 = new GetQueryResponseSchemaTask(var1, var2, var3, var4);
      return var5.execute();
   }

   @Override
   public WsSavedQueries getSavedQueries(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException {
      GetSavedQueriesTask var5 = new GetSavedQueriesTask(var1, var2, var3, var4);
      return var5.execute();
   }

   @Override
   public WsQueryResult runQuery(String var1, String var2, WsEndpointParameters var3, String var4) throws RemoteException {
      throw new RemoteException("Not implemented yet.");
   }
}
