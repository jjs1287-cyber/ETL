package com.etlforge.integration.server.implementation.webservice.connector;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.connector.serverapi.TestConnectionCallback;
import org.etlforge.integration.client.connector.serverapi.TestConnectorConnectionApi;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.webservice.JavaWebServiceConfiguration;
import org.etlforge.integration.client.server.webservice.JavaWebServiceServerCall;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.server.backend.webservice.connector.client.ETLForgeConnectorEngine;
import org.etlforge.integration.server.backend.webservice.connector.client.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.connector.client.WsStatus;
import org.etlforge.lang.exception.NotImplementedYetException;

public final class ConnectorConnectionTesterWsImpl extends JavaWebServiceServerCall implements TestConnectorConnectionApi {
   public ConnectorConnectionTesterWsImpl(JavaWebServiceConfiguration var1) {
      super(var1);
   }

   @Override
   public void testConnection(ConnectorEndpoint var1, TestConnectionCallback var2) {
      try {
         ConnectorConnectionTesterWsImpl.Worker var3 = new ConnectorConnectionTesterWsImpl.Worker(this.getConfiguration());
         var3.testConnection(var1, var2);
      } catch (RemoteException var4) {
         var2.caught(this.convert(var4));
      } catch (ServiceException var5) {
         var2.caught(new IntegrationServerException("Failed to call webservice: " + var5.getMessage(), var5));
      }
   }

   private static class Worker extends ConnectorWsBase {
      public Worker(WebServiceConfiguration var1) {
         super(var1);
      }

      public void testConnection(ConnectorEndpoint var1, TestConnectionCallback var2) throws RemoteException, ServiceException {
         ETLForgeConnectorEngine var3 = this.getServerProxy(var2);
         if (var3 != null) {
            WsEndpointParameters var4 = this.createWsEndpointParams(var1);
            WsStatus var5 = var3.testConnection(var4);
            if (var5 == null) {
               var2.caught(new NotImplementedYetException("This functionality has not been implemented on the server side."));
            } else if (var5.getStatusCode() < 0) {
               var2.caught(new Exception(var5.getMessage()));
            } else {
               var2.success(var5.getMessage());
            }
         }
      }
   }
}
