package org.etlforge.integration.server.engine.jdbc;

import java.sql.SQLException;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.storedproc.CallStoredProcedure;
import org.etlforge.integration.server.db.storedproc.SPResult;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.SessionExecutionWrapper;

final class CallStoredProcedureExecutor extends SessionExecutionWrapper<JdbcSession, SPResult> {
   private final IntegrationEntityId entityId;
   private final ConnectionParams connParams;
   private final String spName;
   private final String[] spParams;
   private final boolean autoCommit;
   private final int maxNumberOfRows;

   public CallStoredProcedureExecutor(JdbcSession var1, IntegrationEntityId var2, ConnectionParams var3, String var4, String[] var5, boolean var6, int var7) {
      super(var1);
      this.entityId = var2;
      this.connParams = var3;
      this.spName = var4;
      this.spParams = var5;
      this.autoCommit = var6;
      this.maxNumberOfRows = var7;
   }

   protected SPResult doWork(JdbcSession var1) throws EngineSessionException, ServerAccessException, ServerDbException {
      try {
         CallStoredProcedure var2 = var1.getCallStoredProcedure(this.entityId, this.connParams);
         var2.setAutoCommit(this.autoCommit);
         return var2.execute(this.spName, this.spParams, this.maxNumberOfRows);
      } catch (SQLException var3) {
         throw new EngineSessionException(var3);
      }
   }
}
