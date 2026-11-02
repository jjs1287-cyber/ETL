package org.etlforge.integration.server.engine;

import org.etlforge.integration.server.ExternalApiSupport;

public interface EngineSessionFactory<S extends EngineSession<S>> {
   S newSession(ExternalApiSupport var1);
}
