package org.etlforge.integration.server.log;

import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public final class OperationLogDirectory extends LogDirectory {
   public OperationLogDirectory() throws ServerConfigurationException {
      super(ServerConfig.getOperationLogDir());
   }
}
