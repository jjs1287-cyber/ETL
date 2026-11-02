package org.etlforge.integration.client.server.webservice;

import org.etlforge.integration.client.server.ServerManager;
import org.etlforge.integration.client.server.ServerPorts;

public final class CppWebServiceConfiguration extends WebServiceConfiguration {
   public CppWebServiceConfiguration(ServerManager var1, ServerPorts var2, WebServiceConfigurationCallback var3) {
      super("/axis/", "ETLForge Apache Server", var1, var2, var3);
   }
}
