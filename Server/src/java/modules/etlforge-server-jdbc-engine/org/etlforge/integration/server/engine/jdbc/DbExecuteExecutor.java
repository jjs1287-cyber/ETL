package org.etlforge.integration.server.engine.jdbc;

import java.sql.SQLException;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.lookup.DbExecute;
import org.etlforge.integration.server.db.lookup.DbExecuteResult;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.SessionExecutionWrapper;

final class DbExecuteExecutor extends SessionExecutionWrapper<JdbcSession, DbExecuteResult> {
   private final IntegrationEntityId entityId;
   private final ConnectionParams connParams;
   private final String sql;
   private final boolean autoCommit;
   private final int maxNumberOfRows;

   public DbExecuteExecutor(JdbcSession var1, IntegrationEntityId var2, ConnectionParams var3, String var4, boolean var5, int var6) {
      super(var1);
      this.entityId = var2;
      this.connParams = var3;
      this.sql = var4;
      this.autoCommit = var5;
      this.maxNumberOfRows = var6;
   }

   protected DbExecuteResult doWork(JdbcSession var1) throws EngineSessionException, ServerAccessException, ServerDbException {
      try {
         DbExecute var2 = var1.getDbExecute(this.entityId, this.connParams);
         var2.setAutoCommit(this.autoCommit);
         return var2.execute(this.sql, this.maxNumberOfRows);
      } catch (SQLException var3) {
         throw new EngineSessionException(var3);
      }
   }
}
