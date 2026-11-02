package org.etlforge.integration.server.engine.jdbc;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.etlforge.collections.Pair;
import org.etlforge.integration.data.access.Permission;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.ExternalApiSupport;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ExternalDatabaseException;
import org.etlforge.integration.server.db.ServerDbException;
import org.etlforge.integration.server.db.SourceAndTargetConnections;
import org.etlforge.integration.server.db.lookup.CachedDbLookup;
import org.etlforge.integration.server.db.lookup.DbExecute;
import org.etlforge.integration.server.db.lookup.DbLookup;
import org.etlforge.integration.server.db.lookup.DefaultDbExecute;
import org.etlforge.integration.server.db.lookup.DefaultDbLookup;
import org.etlforge.integration.server.db.source.DatabaseSource;
import org.etlforge.integration.server.db.source.ReadSourceParams;
import org.etlforge.integration.server.db.storedproc.CallStoredProcedure;
import org.etlforge.integration.server.db.target.DatabaseTarget;
import org.etlforge.integration.server.db.target.WriteTargetParams;
import org.etlforge.integration.server.engine.EngineSession;

public final class JdbcSession extends EngineSession<JdbcSession> {
   private final Map<JdbcSession.Key, DbLookup> dbLookups = Maps.newHashMap();
   private final Map<JdbcSession.Key, DbLookup> cachedDbLookups = Maps.newHashMap();
   private final Map<JdbcSession.Key, DbExecute> dbExecutes = Maps.newHashMap();
   private final Map<JdbcSession.Key, CallStoredProcedure> callStoredProcedures = Maps.newHashMap();
   private final SourceAndTargetConnections externalConnections = new SourceAndTargetConnections();
   private boolean checkAccess;
   private final Object lock = new Object();

   JdbcSession(ExternalApiSupport var1) {
      super(new JdbcSessionId(), var1);
   }

   public JdbcSessionId getId() {
      return (JdbcSessionId)super.getId();
   }

   @Override
   protected void preCloseConnectionCleanup() {
      synchronized (this.lock) {
         this.externalConnections.dispose();
      }
   }

   public void setCheckAccess(boolean var1) {
      this.checkAccess = var1;
   }

   public void readSource(ReadSourceParams var1) throws ServerAccessException, ExternalDatabaseException {
      if (this.checkAccess) {
         this.verifyPermission(var1.sourceId, Permission.EXECUTE);
      }

      this.closeBackendConnection();
      DatabaseSource.createSourceTextDocumentFromJDBC(var1);
   }

   public void writeTarget(WriteTargetParams var1, Optional<StringBuilder> var2) throws ExternalDatabaseException, ServerAccessException, SQLException {
      if (this.checkAccess) {
         this.verifyPermission(var1.targetId, Permission.EXECUTE);
      }

      this.closeBackendConnection();
      DatabaseTarget.writeToTargetDatabase(var1, var2);
   }

   public void commitTransactions() {
      synchronized (this.lock) {
         this.externalConnections.commit();
      }
   }

   public void rollbackTransactions() {
      synchronized (this.lock) {
         this.externalConnections.rollback();
      }
   }

   public DbLookup getDbLookup(IntegrationEntityId var1, ConnectionParams var2) throws ServerDbException, ServerAccessException, SQLException {
      Preconditions.checkArgument(var1 instanceof SourceId || var1 instanceof TargetId, "entityId must be the ID of a Source or a Target");
      JdbcSession.Key var3 = new JdbcSession.Key(var1, var2);
      synchronized (this.lock) {
         DbLookup var5 = this.dbLookups.get(var3);
         if (var5 == null) {
            var5 = this.createAndStoreDbLookup(var3);
         }

         return var5;
      }
   }

   public DbLookup getCachedDbLookup(IntegrationEntityId var1, ConnectionParams var2) throws ServerDbException, ServerAccessException, SQLException {
      JdbcSession.Key var3 = new JdbcSession.Key(var1, var2);
      synchronized (this.lock) {
         DbLookup var5 = this.cachedDbLookups.get(var3);
         if (var5 == null) {
            var5 = new CachedDbLookup(this.createAndStoreDbLookup(var3));
            this.cachedDbLookups.put(var3, var5);
         }

         return var5;
      }
   }

   public DbExecute getDbExecute(IntegrationEntityId var1, ConnectionParams var2) throws ServerDbException, ServerAccessException, SQLException {
      JdbcSession.Key var3 = new JdbcSession.Key(var1, var2);
      synchronized (this.lock) {
         DbExecute var5 = this.dbExecutes.get(var3);
         if (var5 == null) {
            var5 = this.createAndStoreDbExecute(var3);
         }

         return var5;
      }
   }

   private DbLookup createAndStoreDbLookup(JdbcSession.Key var1) throws ServerDbException, ServerAccessException, SQLException {
      Connection var2 = this.connectToExternalDB(var1);
      DefaultDbLookup var3 = new DefaultDbLookup(var2);
      this.dbLookups.put(var1, var3);
      return var3;
   }

   private DbExecute createAndStoreDbExecute(JdbcSession.Key var1) throws ServerDbException, ServerAccessException, SQLException {
      Connection var2 = this.connectToExternalDB(var1);
      DefaultDbExecute var3 = new DefaultDbExecute(var2);
      this.dbExecutes.put(var1, var3);
      return var3;
   }

   public CallStoredProcedure getCallStoredProcedure(IntegrationEntityId var1, ConnectionParams var2) throws ServerDbException, ServerAccessException, SQLException {
      JdbcSession.Key var3 = new JdbcSession.Key(var1, var2);
      synchronized (this.lock) {
         CallStoredProcedure var5 = this.callStoredProcedures.get(var3);
         if (var5 == null) {
            var5 = this.createAndStoreCallStoredProcedure(var3);
         }

         return var5;
      }
   }

   private CallStoredProcedure createAndStoreCallStoredProcedure(JdbcSession.Key var1) throws ServerDbException, ServerAccessException, SQLException {
      Connection var2 = this.connectToExternalDB(var1);
      CallStoredProcedure var3 = new CallStoredProcedure(var2);
      this.callStoredProcedures.put(var1, var3);
      return var3;
   }

   private Connection connectToExternalDB(JdbcSession.Key var1) throws ServerAccessException, ServerDbException, SQLException {
      if (this.checkAccess) {
         this.verifyPermission(var1.data.first, Permission.EXECUTE);
      }

      this.closeBackendConnection();
      return this.externalConnections.getConnection(var1.data.first, var1.data.second);
   }

   private static final class Key {
      public final Pair<IntegrationEntityId, ConnectionParams> data;

      public Key(IntegrationEntityId var1, ConnectionParams var2) {
         this.data = Pair.of(var1, var2);
      }

      @Override
      public boolean equals(Object var1) {
         return var1 == this ? true : var1 instanceof JdbcSession.Key && this.data.equals(((JdbcSession.Key)var1).data);
      }

      @Override
      public int hashCode() {
         return this.data.hashCode();
      }
   }
}
