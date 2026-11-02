package org.etlforge.integration.server.engine.jdbc;

import com.google.common.base.Optional;
import java.sql.SQLException;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ExternalDatabaseException;
import org.etlforge.integration.server.db.target.WriteTargetParams;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.SessionExecutionWrapper;

final class WriteTargetExecutor extends SessionExecutionWrapper<JdbcSession, Void> {
   private final WriteTargetParams params;
   private String warnings = "";

   public WriteTargetExecutor(JdbcSession var1, WriteTargetParams var2) {
      super(var1);
      this.params = var2;
   }

   protected Void doWork(JdbcSession var1) throws EngineSessionException, ServerAccessException {
      try {
         StringBuilder var2 = new StringBuilder("");
         var1.writeTarget(this.params, Optional.of(var2));
         this.warnings = var2.toString();
         return null;
      } catch (ExternalDatabaseException var3) {
         throw new EngineSessionException(var3);
      } catch (SQLException var4) {
         throw new EngineSessionException(var4);
      }
   }

   public String getWarnings() {
      return this.warnings;
   }
}
