package org.etlforge.integration.server.engine.jdbc;

import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.engine.EngineSessionFactory;

final class JdbcSessionFactory implements EngineSessionFactory<JdbcSession> {
   public JdbcSession newSession(ExternalApiSupport var1) {
      return new JdbcSession(var1);
   }
}
