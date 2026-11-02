package org.etlforge.integration.server.log;

import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public class DebugLogDirectory extends LogDirectory {
   public DebugLogDirectory() throws ServerConfigurationException {
      super(ServerConfig.getDebugLogDir());
   }
}
