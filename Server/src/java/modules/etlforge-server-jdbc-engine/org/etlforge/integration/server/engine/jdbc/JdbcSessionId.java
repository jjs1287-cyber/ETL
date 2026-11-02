package org.etlforge.integration.server.engine.jdbc;

import org.etlforge.integration.server.engine.EngineSessionId;

public final class JdbcSessionId extends EngineSessionId<JdbcSession> {
   public JdbcSessionId() {
   }

   public JdbcSessionId(String var1) {
      super(var1);
   }
}
