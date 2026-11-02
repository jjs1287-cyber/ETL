package org.etlforge.integration.client.server;

import org.etlforge.integration.client.ClientApplicationProperties;
import org.etlforge.util.properties.ReadOnlyProperties;

public final class ClientServerPorts implements ServerPorts {
   private static final String APACHE_PORT_SSL = "APACHE_PORT_SSL";
   private static final String APACHE_PORT_NON_SSL = "APACHE_PORT_NON_SSL";
   private final int standard;
   private final int https;

   public ClientServerPorts() {
      ReadOnlyProperties var1 = ClientApplicationProperties.getInstance();
      this.standard = var1.getInt("APACHE_PORT_NON_SSL", 80);
      this.https = var1.getInt("APACHE_PORT_SSL", 80);
   }

   @Override
   public int getDefaultPort() {
      return this.standard;
   }

   @Override
   public int getDefaultHttpsPort() {
      return this.https;
   }
}
