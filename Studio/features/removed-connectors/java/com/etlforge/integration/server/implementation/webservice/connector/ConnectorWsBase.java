package com.etlforge.integration.server.implementation.webservice.connector;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.rpc.ServiceException;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerCallback;
import org.etlforge.integration.client.server.webservice.WebServiceConfiguration;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.connector.ConnectorEndpoint;
import org.etlforge.integration.etlforgeconnector.ConnectorObject;
import org.etlforge.integration.etlforgeconnector.EndpointParameters;
import org.etlforge.integration.etlforgeconnector.UserCredentials;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;
import org.etlforge.integration.server.backend.webservice.connector.client.ETLForgeConnectorEngine;
import org.etlforge.integration.server.backend.webservice.connector.client.ETLForgeConnectorEngineServiceLocator;
import org.etlforge.integration.server.backend.webservice.connector.client.WsConnectorInput;
import org.etlforge.integration.server.backend.webservice.connector.client.WsEndpointParameters;
import org.etlforge.integration.server.backend.webservice.connector.client.WsNameValuePair;

abstract class ConnectorWsBase {
   private static final String WS_NAME = "konga_connector_engine";
   private final WebServiceConfiguration config;

   protected ConnectorWsBase(WebServiceConfiguration var1) {
      this.config = var1;
   }

   protected final ETLForgeConnectorEngine getServerProxy(ServerCallback var1) throws ServiceException {
      ETLForgeConnectorEngineServiceLocator var2 = new ETLForgeConnectorEngineServiceLocator();
      StringBuilder var3 = new StringBuilder();
      if (this.config.configureWebservice(var2, "konga_connector_engine", var3, var1)) {
         var2.setkonga_connector_engineEndpointAddress(var3.toString());
         return var2.getkonga_connector_engine();
      } else {
         return null;
      }
   }

   protected final WsEndpointParameters createWsEndpointParams(ConnectorEndpoint var1) {
      WsEndpointParameters var2 = new WsEndpointParameters();
      String var3 = this.config.getUserName();
      String var4 = this.config.getPasswordHash();
      var2.setEtlforgeUser(var3);
      var2.setEtlforgeMd5Pwd(var4);
      String var5 = var1.getConnectorTypeId();
      var2.setEndpointTypeName(var5);
      var2.setEndpointGUID(var1.getID().toString());
      EndpointParameters var6 = var1.getConnectionParameters();
      var2.setHost(var6.getServer().getAddress());
      UserCredentials var7 = var6.getUserCredentials();
      var2.setUser(var7.getUsername());
      Optional var8 = var7.getPassword();
      if (var8.isPresent()) {
         var2.setPassword(((Password)var8.get()).clearText());
      }

      var2.setIsPasswordEncrypted(false);
      ArrayList var9 = Lists.newArrayList();
      Properties var10 = var6.getAdditionalParams();

      for (String var12 : var10.stringPropertyNames()) {
         String var13 = var10.getProperty(var12);
         var9.add(new WsNameValuePair(var12, var13));
      }

      var2.setAdditionalParameters(Iterables.toArray(var9, WsNameValuePair.class));
      return var2;
   }

   protected final WsConnectorInput createWsConnectorInput(ConnectorEndpoint var1, List<ConnectorObject> var2) {
      WsEndpointParameters var3 = this.createWsEndpointParams(var1);
      WsConnectorInput var4 = new WsConnectorInput();
      var4.setEndpointParameters(var3);
      var4.setSelectedObjectNames(toPathNameArray(var2));
      return var4;
   }

   private static String[] toPathNameArray(List<ConnectorObject> var0) {
      ArrayList var1 = Lists.newArrayList();

      for (ConnectorObject var3 : var0) {
         if (var3 instanceof NetSuiteConnectorObject) {
            NetSuiteConnectorObject var4 = (NetSuiteConnectorObject)var3;
            var1.add(var4.getKey());
         } else {
            var1.add(var3.getName());
         }
      }

      return (String[]) var1.toArray(new String[var1.size()]);
   }

   protected final IntegrationServerException convert(RemoteException var1) {
      return this.config.convertToIntegrationServerException(var1);
   }
}
