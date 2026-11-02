package org.etlforge.integration.client.applications.deployer;

import org.etlforge.integration.client.server.ServerPorts;

public final class ServerPortsImpl implements ServerPorts {
   private final int port;

   public ServerPortsImpl(int var1) {
      this.port = var1;
   }

   @Override
   public int getDefaultPort() {
      return this.port;
   }

   @Override
   public int getDefaultHttpsPort() {
      return this.port;
   }
}
