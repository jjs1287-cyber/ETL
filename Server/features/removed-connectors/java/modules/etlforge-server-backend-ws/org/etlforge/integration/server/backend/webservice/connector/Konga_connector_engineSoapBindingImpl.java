package org.etlforge.integration.server.backend.webservice.connector;

import java.rmi.RemoteException;

public final class Konga_connector_engineSoapBindingImpl implements ETLForgeConnectorEngine {
   private final Konga_connector_Engine_WsImpl impl = new Konga_connector_Engine_WsImpl();

   @Override
   public WsStatus testConnection(WsEndpointParameters var1) throws RemoteException {
      return this.impl.testConnection(var1);
   }

   @Override
   public WsConnectorObjectList getConnectorObjectList(WsConnectorInput var1, String var2) throws RemoteException {
      return this.impl.getConnectorObjectList(var1, var2);
   }

   @Override
   public WsConnectorSchemas getConnectorSchemas(WsConnectorInput var1) throws RemoteException {
      return this.impl.getConnectorSchemas(var1);
   }

   @Override
   public WsConnectorSchemaFiles getConnectorSchemaFiles(boolean var1, WsConnectorInput var2) throws RemoteException {
      return this.impl.getConnectorSchemaFiles(var1, var2);
   }

   @Override
   public WsStatus executeConnectorFile(WsConnectorInput var1, String var2, String var3) throws RemoteException {
      return this.impl.executeConnectorFile(var1, var2, var3);
   }

   @Override
   public WsConnectorAutoDeskItemInfo getAutoDeskItemVersionId(WsEndpointParameters var1, String var2, String var3) throws RemoteException {
      return this.impl.getAutoDeskItemVersionId(var1, var2, var3);
   }

   @Override
   public WsConnectorSessionResult login(WsEndpointParameters var1) throws RemoteException {
      return this.impl.login(var1);
   }

   @Override
   public WsStatus logout(WsEndpointParameters var1) throws RemoteException {
      return this.impl.logout(var1);
   }

   @Override
   public WsConnectorExecutionResponse executeConnector(WsConnectorInput var1, String var2) throws RemoteException {
      return this.impl.executeConnector(var1, var2);
   }
}
