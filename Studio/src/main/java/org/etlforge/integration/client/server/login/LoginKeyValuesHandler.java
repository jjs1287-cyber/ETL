package org.etlforge.integration.client.server.login;

import java.util.Map;
import org.etlforge.integration.client.server.IntegrationServerException;
import org.etlforge.integration.client.server.ServerInfo;

public interface LoginKeyValuesHandler {
   LoginKeyValuesHandler DO_NOTHING = new LoginKeyValuesHandler() {
      @Override
      public void handle(ServerInfo var1, Map<String, String> var2) throws IntegrationServerException {
      }
   };

   void handle(ServerInfo var1, Map<String, String> var2) throws IntegrationServerException;
}
