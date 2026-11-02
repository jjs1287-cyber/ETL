package org.etlforge.integration.server.engine.jdbc;

import java.sql.SQLException;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.lookup.DbLookup;
import org.etlforge.integration.server.db.lookup.DbLookupResult;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.SessionExecutionWrapper;

final class DbLookupExecutor extends SessionExecutionWrapper<JdbcSession, DbLookupResult> {
   private final IntegrationEntityId entityId;
   private final ConnectionParams connParams;
   private final String sql;
   private final boolean autoCommit;

   public DbLookupExecutor(JdbcSession var1, IntegrationEntityId var2, ConnectionParams var3, String var4, boolean var5) {
      super(var1);
      this.entityId = var2;
      this.connParams = var3;
      this.sql = var4;
      this.autoCommit = var5;
   }

   protected DbLookupResult doWork(JdbcSession var1) throws EngineSessionException, ServerAccessException {
      try {
         DbLookup var2 = var1.getDbLookup(this.entityId, this.connParams);
         var2.setAutoCommit(this.autoCommit);
         return var2.lookup(this.sql);
      } catch (ServerDbException var3) {
         throw new EngineSessionException(var3);
      } catch (SQLException var4) {
         throw new EngineSessionException(var4);
      }
   }
}
