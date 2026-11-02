package org.etlforge.integration.server;

import org.etlforge.integration.common.server.ServerGuid;
import org.etlforge.integration.server.exception.ServerConfigurationException;

public interface ServerGuidLookup {
   ServerGuid lookupGuid() throws ServerConfigurationException;
}
