package org.etlforge.integration.server.engine.jdbc;

import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ExternalDatabaseException;
import org.etlforge.integration.server.db.source.ReadSourceParams;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.SessionExecutionWrapper;

final class ReadSourceExecutor extends SessionExecutionWrapper<JdbcSession, Void> {
   private final ReadSourceParams params;

   public ReadSourceExecutor(JdbcSession var1, ReadSourceParams var2) {
      super(var1);
      this.params = var2;
   }

   protected Void doWork(JdbcSession var1) throws EngineSessionException, ServerAccessException {
      try {
         var1.readSource(this.params);
         return null;
      } catch (ExternalDatabaseException var3) {
         throw new EngineSessionException(var3);
      }
   }
}
