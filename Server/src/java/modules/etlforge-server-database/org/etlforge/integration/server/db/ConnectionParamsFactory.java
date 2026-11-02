package org.etlforge.integration.server.db;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.SQLException;
import org.etlforge.db.TransactionIsolationLevel;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.crypto.Crypto;
import org.etlforge.integration.server.db.trandb.SourcePropertiesTab;
import org.etlforge.integration.server.db.trandb.SourcesTab;
import org.etlforge.integration.server.db.trandb.SourcesTabRow;
import org.etlforge.integration.server.db.trandb.TargetPropertiesTab;
import org.etlforge.integration.server.db.trandb.TargetsTab;
import org.etlforge.integration.server.db.trandb.TargetsTabRow;
import org.etlforge.util.net.Port;

public class ConnectionParamsFactory {
   private final Connection tranDbConn;

   public ConnectionParamsFactory(Connection var1) {
      Preconditions.checkNotNull(var1);
      this.tranDbConn = var1;
   }

   public ConnectionParams createParams(IntegrationEntityId var1) throws ServerDbException {
      if (var1 instanceof SourceId) {
         return this.forSource((SourceId)var1);
      } else if (var1 instanceof TargetId) {
         return this.forTarget((TargetId)var1);
      } else {
         throw new IllegalArgumentException("entityId must be the ID of a Source or a Target (was " + (var1 != null ? var1.getEntityType() : "null") + ")");
      }
   }

   public ConnectionParams forSource(SourceId var1) throws ServerDbException {
      Preconditions.checkNotNull(var1, "sourceId");
      return new ConnectionParamsFactory.SourceParamsFactory(var1).getParams();
   }

   public ConnectionParams forTarget(TargetId var1) throws ServerDbException {
      Preconditions.checkNotNull(var1, "targetId");
      return new ConnectionParamsFactory.TargetParamsFactory(var1).getParams();
   }

   private static String decryptPassword(String var0) {
      return var0 == null ? null : Crypto.decryptString(var0);
   }

   private static TransactionIsolationLevel parseTransactionIsolationLevel(String var0) {
      if (var0 != null) {
         try {
            int var1 = Integer.parseInt(var0);
            return TransactionIsolationLevel.fromCode(var1);
         } catch (NumberFormatException var2) {
            var2.printStackTrace();
         }
      }

      return TransactionIsolationLevel.DEFAULT;
   }

   private class SourceParamsFactory {
      private final SourceId sourceId;

      public SourceParamsFactory(SourceId var2) {
         this.sourceId = var2;
      }

      public ConnectionParams getParams() throws ServerDbException {
         try {
            SourcesTabRow var1 = this.getDefinition();
            if (var1 == null) {
               throw new ServerDbException("Could not find the deployed definition of the Source with GUID " + this.sourceId);
            }

            ConnectionParams var2 = new ConnectionParams();
            var2.setServer(var1.get_locator())
               .setDatabase(var1.get_dbname())
               .setUser(var1.get_login())
               .setPassword(ConnectionParamsFactory.decryptPassword(var1.get_password()))
               .setPort(Port.valueOf(var1.get_port()))
               .setDriverName(var1.get_driver())
               .setTransactionIsolationLevel(this.getTransactionIsolationLevel(var1))
               .setManualConnectionString(var1.get_connectstring())
               .setAdditionalParams(var1.get_optional_connectstring());
            return var2;
         } catch (SQLException var3) {
            throw new ServerDbException(var3.getMessage(), var3);
         }
      }

      private SourcesTabRow getDefinition() throws SQLException {
         SourcesTab var1 = new SourcesTab(ConnectionParamsFactory.this.tranDbConn);
         return var1.query(this.sourceId);
      }

      private TransactionIsolationLevel getTransactionIsolationLevel(SourcesTabRow var1) throws SQLException {
         SourcePropertiesTab var2 = new SourcePropertiesTab(ConnectionParamsFactory.this.tranDbConn);
         String var3 = var2.query(var1.get_source_id(), "db_transaction_isolation_level");
         return ConnectionParamsFactory.parseTransactionIsolationLevel(var3);
      }
   }

   private class TargetParamsFactory {
      private final TargetId targetId;

      public TargetParamsFactory(TargetId var2) {
         this.targetId = var2;
      }

      public ConnectionParams getParams() throws ServerDbException {
         try {
            TargetsTabRow var1 = this.getDefinition();
            if (var1 == null) {
               throw new ServerDbException("Could not find the deployed definition of the Target with GUID " + this.targetId);
            }

            ConnectionParams var2 = new ConnectionParams();
            var2.setServer(var1.get_locator())
               .setDatabase(var1.get_dbname())
               .setUser(var1.get_login())
               .setPassword(ConnectionParamsFactory.decryptPassword(var1.get_password()))
               .setPort(Port.valueOf(var1.get_port()))
               .setDriverName(var1.get_driver())
               .setTransactionIsolationLevel(this.getTransactionIsolationLevel(var1))
               .setManualConnectionString(var1.get_connectstring())
               .setAdditionalParams(var1.get_optional_connectstring());
            return var2;
         } catch (SQLException var3) {
            throw new ServerDbException(var3.getMessage(), var3);
         }
      }

      private TargetsTabRow getDefinition() throws SQLException {
         TargetsTab var1 = new TargetsTab(ConnectionParamsFactory.this.tranDbConn);
         return var1.query(this.targetId);
      }

      private TransactionIsolationLevel getTransactionIsolationLevel(TargetsTabRow var1) throws SQLException {
         TargetPropertiesTab var2 = new TargetPropertiesTab(ConnectionParamsFactory.this.tranDbConn);
         String var3 = var2.query(var1.get_target_id(), "db_transaction_isolation_level");
         return ConnectionParamsFactory.parseTransactionIsolationLevel(var3);
      }
   }
}
