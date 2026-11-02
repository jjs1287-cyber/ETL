package org.etlforge.integration.server.db;

import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.etlforge.collections.Pair;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.db.TransactionIsolationLevel;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.database.driver.ConnectionParams;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.infoprovider.DefaultConnectionFactory;
import org.etlforge.system.Disposable;

public final class SourceAndTargetConnections implements Disposable {
   private final Map<Pair<IntegrationEntityId, ConnectionParams>, Connection> connections = Maps.newHashMap();
   private final Map<Pair<IntegrationEntityId, ConnectionParams>, String> driverNames = Maps.newHashMap();

   @Override
   public void dispose() {
      synchronized (this.connections) {
         for (Connection var3 : this.connections.values()) {
            KongaDbUtils.close(var3);
         }
      }
   }

   public void commit() {
      synchronized (this.connections) {
         for (Connection var3 : this.connections.values()) {
            try {
               var3.commit();
            } catch (SQLException var6) {
               var6.printStackTrace();
            }
         }
      }
   }

   public void rollback() {
      synchronized (this.connections) {
         for (Connection var3 : this.connections.values()) {
            try {
               var3.rollback();
            } catch (SQLException var6) {
               var6.printStackTrace();
            }
         }
      }
   }

   public Connection getConnection(IntegrationEntityId var1, ConnectionParams var2) throws ServerDbException, SQLException {
      Pair var3 = Pair.of(var1, var2);
      synchronized (this.connections) {
         Connection var5 = this.connections.get(var3);
         if (var5 == null || var5.isClosed()) {
            var5 = this.createConnection(var2);
            this.connections.put(var3, var5);
            this.driverNames.put(var3, var2.getDriverName());
         }

         return var5;
      }
   }

   private Connection createConnection(ConnectionParams var1) throws ServerDbException, SQLException {
      try {
         DefaultConnectionFactory var2 = new DefaultConnectionFactory(var1, null, null);
         Connection var3 = var2.newConnection();
         if (var1.getTransactionIsolationLevel() != TransactionIsolationLevel.DEFAULT) {
            var1.getTransactionIsolationLevel().apply(var3);
         }

         return var3;
      } catch (ServerAccessException var4) {
         throw new RuntimeException(var4.getMessage(), var4);
      }
   }
}
