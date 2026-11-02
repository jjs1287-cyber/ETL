package org.etlforge.integration.server.backend.webservice.connector;

import org.etlforge.integration.server.engine.connector.ConnectorDefaultValueResolver;

public class ConnectorDefaultValueResolverAdapter {
   private static ConnectorDefaultValueResolverAdapter instance = new ConnectorDefaultValueResolverAdapter();
   private final ConnectorDefaultValueResolver deResolver = new ConnectorDefaultValueResolver();

   static void resolve(WsConnectorInput var0) {
      instance.resolveImpl(var0);
   }

   static void resolve(WsEndpointParameters var0) {
      instance.resolveImpl(var0);
   }

   private ConnectorDefaultValueResolverAdapter() {
   }

   private String resolveImpl(String var1) {
      return this.deResolver.resolveImpl(var1);
   }

   private void resolveImpl(WsEndpointParameters var1) {
      var1.setHost(this.resolveImpl(var1.getHost()));
      var1.setPassword(this.resolveImpl(var1.getPassword()));
      var1.setUser(this.resolveImpl(var1.getUser()));
      WsNameValuePair[] var2 = var1.getAdditionalParameters();

      for (WsNameValuePair var6 : var2) {
         var6.setValue(this.resolveImpl(var6.getValue()));
      }
   }

   private void resolveImpl(WsConnectorInput var1) {
      WsEndpointParameters var2 = var1.getEndpointParameters();
      this.resolveImpl(var2);
   }
}
