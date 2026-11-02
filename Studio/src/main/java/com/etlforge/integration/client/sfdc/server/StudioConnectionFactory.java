package com.etlforge.integration.client.sfdc.server;

import com.google.common.base.Preconditions;
import org.etlforge.integration.client.server.ServerConnectionEvent;
import org.etlforge.integration.client.server.ServerConnectionListener;
import org.etlforge.integration.client.server.ServerConnectionService;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.PoolingConnectionFactory;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.system.Disposable;

public final class StudioConnectionFactory implements PoolingConnectionFactory, Disposable {
   private final PoolingConnectionFactory impl;
   private final ServerConnectionService studioConnectionService;
   private final ServerConnectionListener connectionEraser = new ServerConnectionListener() {
      @Override
      public void serverConnectionChanged(ServerConnectionEvent var1) {
         StudioConnectionFactory.this.forgetPooledConnections();
      }
   };

   public static StudioConnectionFactory create(PoolingConnectionFactory var0, ServerConnectionService var1) {
      StudioConnectionFactory var2 = new StudioConnectionFactory(var0, var1);
      var2.activate();
      return var2;
   }

   private StudioConnectionFactory(PoolingConnectionFactory var1, ServerConnectionService var2) {
      this.impl = Preconditions.checkNotNull(var1);
      this.studioConnectionService = Preconditions.checkNotNull(var2);
   }

   private void activate() {
      this.studioConnectionService.addServerConnectionListener(this.connectionEraser);
   }

   @Override
   public SalesforceConnection newConnection(OrgIdentifier var1) {
      return this.impl.newConnection(var1);
   }

   @Override
   public void forgetPooledConnections() {
      this.impl.forgetPooledConnections();
   }

   @Override
   public String getPooledConnectionsReport() {
      return this.impl.getPooledConnectionsReport();
   }

   @Override
   public void dispose() {
      this.studioConnectionService.removeServerConnectionListener(this.connectionEraser);
   }
}
