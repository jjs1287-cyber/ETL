package org.etlforge.integration.server.backend.webservice.jdbc;

import com.google.common.collect.Lists;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.db.TransactionIsolationLevel;
import org.etlforge.integration.common.server.ServerErrors;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.DbInsertMode;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.db.lookup.DbExecuteResult;
import org.etlforge.integration.server.db.lookup.DbLookupResult;
import org.etlforge.integration.server.db.source.ReadSourceParams;
import org.etlforge.integration.server.db.storedproc.SPParameter;
import org.etlforge.integration.server.db.storedproc.SPResult;
import org.etlforge.integration.server.db.target.WriteTargetParams;
import org.etlforge.integration.server.engine.EngineSessionException;
import org.etlforge.integration.server.engine.jdbc.JdbcEngineLog;
import org.etlforge.integration.server.engine.jdbc.JdbcSession;
import org.etlforge.integration.server.engine.jdbc.JdbcSessionCredentials;
import org.etlforge.integration.server.engine.jdbc.JdbcSessionId;
import org.etlforge.integration.server.ws.WsUtils;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.net.Port;

public final class Konga_jdbc_engineSoapBindingImpl implements JdbcEngine {
   @Override
   public WsJdbcStartNewSessionResult startNewSession(String var1, String var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.jdbc.JdbcEngine var3 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
         JdbcSession var4 = var3.newSession(var1, var2);
         return new WsJdbcStartNewSessionResult(createForSuccess(), var4.getId().toString());
      } catch (Exception var5) {
         return new WsJdbcStartNewSessionResult(this.createForException(var5, "startNewSession"), null);
      }
   }

   @Override
   public WsJdbcError closeSession(WsJdbcSessionId var1) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.jdbc.JdbcEngine var2 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
         var2.closeSession(new JdbcSessionId(var1.getSessionGuid()), var1.getEtlforgeUser(), var1.getMd5Pwd());
         return createForSuccess();
      } catch (Exception var3) {
         return this.createForException(var3, "closeSession");
      }
   }

   @Override
   public WsDbLookupResult dbLookup(WsJdbcSessionId var1, WsDbLookupParams var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.jdbc.JdbcEngine var3 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
         DbLookupResult var4 = var3.dbLookup(
            toCredentials(var1),
            this.getSourceOrTargetId(var2),
            toConnectionParams(var2.getConnectionParams()),
            JdbcEngineUtils.getSqlStatement(var2),
            var2.isAutoCommit()
         );
         return new WsDbLookupResult(createForSuccess(), JdbcEngineUtils.getResult(var4.getValue()), var4.getUpdateCount());
      } catch (Exception var5) {
         return new WsDbLookupResult(this.createForException(var5, "dbLookup"), null, 0);
      }
   }

   @Override
   public WsDbLookupResult cachedDbLookup(WsJdbcSessionId var1, WsDbLookupParams var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.jdbc.JdbcEngine var3 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
         DbLookupResult var4 = var3.cachedDbLookup(
            toCredentials(var1),
            this.getSourceOrTargetId(var2),
            toConnectionParams(var2.getConnectionParams()),
            JdbcEngineUtils.getSqlStatement(var2),
            var2.isAutoCommit()
         );
         return new WsDbLookupResult(createForSuccess(), JdbcEngineUtils.getResult(var4.getValue()), var4.getUpdateCount());
      } catch (Exception var5) {
         return new WsDbLookupResult(this.createForException(var5, "cachedDbLookup"), null, 0);
      }
   }

   @Override
   public WsDbExecuteResult dbExecute(WsJdbcSessionId var1, WsDbLookupParams var2, int var3) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.jdbc.JdbcEngine var4 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
         DbExecuteResult var5 = var4.dbExecute(
            toCredentials(var1),
            this.getSourceOrTargetId(var2),
            toConnectionParams(var2.getConnectionParams()),
            JdbcEngineUtils.getSqlStatement(var2),
            var3,
            var2.isAutoCommit()
         );
         return this.convertToWsDbExecuteResult(var5);
      } catch (Exception var6) {
         return new WsDbExecuteResult(this.createForException(var6, "dbExecute"), new WsResultSet(), 0);
      }
   }

   @Override
   public WsSPResult callStoredProcedure(WsJdbcSessionId var1, WsSPParams var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.jdbc.JdbcEngine var3 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
         SPResult var4 = var3.callStoredProcedure(
            toCredentials(var1),
            this.getSourceOrTargetId(var2),
            toConnectionParams(var2.getConnectionParams()),
            var2.getSpName(),
            var2.getSpParameters(),
            var2.getMaxNumberOfRows(),
            var2.isAutoCommit()
         );
         return this.convertToWsSPResult(var4);
      } catch (Exception var5) {
         return new WsSPResult(this.createForException(var5, "callStoredProcedure"), new WsResultSet(), new WsSPParameter[0], "", 0);
      }
   }

   @Override
   public WsJdbcError commitTransactions(WsJdbcSessionId var1) throws RemoteException {
      try {
         JdbcSession var2 = this.getSession(var1);
         var2.commitTransactions();
         return createForSuccess();
      } catch (Exception var3) {
         return this.createForException(var3, "commitTransactions");
      }
   }

   @Override
   public WsJdbcError rollbackTransactions(WsJdbcSessionId var1) throws RemoteException {
      try {
         JdbcSession var2 = this.getSession(var1);
         var2.rollbackTransactions();
         return createForSuccess();
      } catch (Exception var3) {
         return this.createForException(var3, "rollbackTransactions");
      }
   }

   @Override
   public WsJdbcError readSource(WsJdbcSessionId var1, WsReadSourceParams var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.jdbc.JdbcEngine var3 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
         var3.readSource(toCredentials(var1), toReadSourceParams(var2));
         return createForSuccess();
      } catch (Exception var4) {
         return this.createForException(var4, "readSource");
      }
   }

   @Override
   public WsJdbcError writeTarget(WsJdbcSessionId var1, WsWriteTargetParams var2) throws RemoteException {
      try {
         org.etlforge.integration.server.engine.jdbc.JdbcEngine var3 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
         StringBuilder var4 = new StringBuilder("");
         var3.writeTarget(toCredentials(var1), toWriteTargetParams(var2), var4);
         return createForSuccess(var4.toString());
      } catch (Exception var5) {
         return this.createForException(var5, "writeTarget");
      }
   }

   private IntegrationEntityId getSourceOrTargetId(WsDbLookupParams var1) {
      if (StringUtils.isNotEmpty(var1.getSourceGuid())) {
         return new SourceId(var1.getSourceGuid());
      } else if (StringUtils.isNotEmpty(var1.getTargetGuid())) {
         return new TargetId(var1.getTargetGuid());
      } else {
         throw new IllegalArgumentException("Must specify a Source or Target GUID.");
      }
   }

   private IntegrationEntityId getSourceOrTargetId(WsSPParams var1) {
      if (StringUtils.isNotEmpty(var1.getSourceGuid())) {
         return new SourceId(var1.getSourceGuid());
      } else if (StringUtils.isNotEmpty(var1.getTargetGuid())) {
         return new TargetId(var1.getTargetGuid());
      } else {
         throw new IllegalArgumentException("Must specify a Source or Target GUID.");
      }
   }

   private JdbcSession getSession(WsJdbcSessionId var1) throws EngineSessionException {
      org.etlforge.integration.server.engine.jdbc.JdbcEngine var2 = org.etlforge.integration.server.engine.jdbc.JdbcEngine.getEngine();
      JdbcSessionCredentials var3 = new JdbcSessionCredentials(new JdbcSessionId(var1.getSessionGuid()), var1.getEtlforgeUser(), var1.getMd5Pwd());
      return var2.getSession(var3);
   }

   private WsDbExecuteResult convertToWsDbExecuteResult(DbExecuteResult var1) {
      List var2 = var1.getResultSet();
      ArrayList var3 = Lists.newArrayList();

      for (Object __etlItem199 : var2) {
         String[] var5 = (String[]) __etlItem199;
         int var6 = 0;

         for (String var10 : var5) {
            var5[var6++] = JdbcEngineUtils.getResult(var10);
         }

         var3.add(new WsResultSetRow(var5));
      }

      WsResultSet var11 = new WsResultSet((WsResultSetRow[]) var3.toArray(new WsResultSetRow[var3.size()]));
      return new WsDbExecuteResult(createForSuccess(), var11, var1.getUpdateCount());
   }

   private WsSPResult convertToWsSPResult(SPResult var1) {
      List var2 = var1.GetResultSet();
      ArrayList var3 = Lists.newArrayList();

      for (Object __etlItem217 : var2) {
         String[] var5 = (String[]) __etlItem217;
         int var6 = 0;

         for (String var10 : var5) {
            var5[var6++] = JdbcEngineUtils.getResult(var10);
         }

         var3.add(new WsResultSetRow(var5));
      }

      new WsResultSet((WsResultSetRow[]) var3.toArray(new WsResultSetRow[var3.size()]));
      List var11 = var1.GetOutputPrms();
      WsSPParameter[] var12 = new WsSPParameter[var11.size()];
      int var13 = 0;

      for (Object __etlItem232 : var11) {
         SPParameter var15 = (SPParameter) __etlItem232;
         var12[var13++] = this.createWsSPParameter(var15);
      }

      return new WsSPResult(createForSuccess(), null, var12, var1.GetReturnVal(), var1.GetUpdateCount());
   }

   private WsSPParameter createWsSPParameter(SPParameter var1) {
      return new WsSPParameter(var1.Name, var1.Ordinal, var1.DataType, var1.InputOutput, var1.Value);
   }

   private static JdbcSessionCredentials toCredentials(WsJdbcSessionId var0) {
      return new JdbcSessionCredentials(new JdbcSessionId(var0.getSessionGuid()), var0.getEtlforgeUser(), var0.getMd5Pwd());
   }

   private static ReadSourceParams toReadSourceParams(WsReadSourceParams var0) {
      ReadSourceParams var1 = new ReadSourceParams(
         new SourceId(var0.getSourceGuid()), toConnectionParams(var0.getConnectionParams()), var0.getJtrFileName(), var0.getDataFileName()
      );
      var1.adHocFilter = var0.getAdHocFilter();
      var1.fetchSize = var0.getFetchSize();
      var1.structureFile = var0.getStructureFileName();
      return var1;
   }

   private static WriteTargetParams toWriteTargetParams(WsWriteTargetParams var0) {
      WriteTargetParams var1 = new WriteTargetParams(
         new TargetId(var0.getTargetGuid()), toConnectionParams(var0.getConnectionParams()), var0.getJtrFileName(), var0.getDataFileName()
      );
      var1.isTransaction = var0.isTransaction();
      var1.insertMode = EnumWithIdLookup.lookup(DbInsertMode.class, var0.getInsertUpdateMode());
      KeyValues var2 = new KeyValues(var0.getKeyValues());
      var1.preTargetSql = var2.getString("etlforge.target.db.pre_target_sql", null);
      var1.includeNulls = var2.getBoolean("etlforge.target.db.include_null_in_sql_statement", false);
      return var1;
   }

   private static ConnectionParams toConnectionParams(WsConnectionParams var0) {
      ConnectionParams var1 = new ConnectionParams();
      var1.setDriverName(var0.getDriverName())
         .setServer(var0.getServer())
         .setDatabase(var0.getDatabase())
         .setPort(Port.valueOf(var0.getPort()))
         .setUser(var0.getUser())
         .setPassword(var0.getPassword())
         .setManualConnectionString(var0.getManualConnectionString())
         .setAdditionalParams(var0.getAdditionalParams())
         .setTransactionIsolationLevel(TransactionIsolationLevel.fromCode(var0.getTransactionIsolationLevel()));
      return var1;
   }

   private WsJdbcError createForException(Exception var1, String var2) {
      try {
         var1.printStackTrace();
         StringBuilder var3 = new StringBuilder(WsUtils.escapeExceptionMessageAndGetCause(var1));
         ServerErrors.appendStacktrace(var1, var3);
         JdbcEngineLog.LOG.throwing(this.getClass().getName(), var2, var1);
         return new WsJdbcError(-1, var3.toString(), "");
      } catch (RuntimeException var4) {
         var4.printStackTrace();
         return new WsJdbcError(-2, var1.getMessage(), "");
      }
   }

   private static WsJdbcError createForSuccess(String var0) {
      return new WsJdbcError(0, "", var0 == null ? "" : var0);
   }

   private static WsJdbcError createForSuccess() {
      return createForSuccess(null);
   }
}
