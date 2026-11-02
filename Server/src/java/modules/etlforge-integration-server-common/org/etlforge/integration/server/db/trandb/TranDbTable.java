package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.db.ServerDbInfo;

public abstract class TranDbTable {
   private Connection tranDbConnection;
   private final String tableName;

   public TranDbTable(String var1) {
      this(var1, null);
   }

   public TranDbTable(String var1, Connection var2) {
      this.tableName = var1;
      this.tranDbConnection = var2;
   }

   public final void open() throws SQLException {
      if (this.tranDbConnection == null) {
         this.tranDbConnection = ServerDbInfo.getTranDbConnectionInfo().getConnection();
      }
   }

   public final boolean isOpen() {
      return this.tranDbConnection != null;
   }

   public final void close() {
      KongaDbUtils.close(this.tranDbConnection);
      this.tranDbConnection = null;
   }

   protected final String getTableName() {
      return this.tableName;
   }

   public final Connection getConnection() {
      return this.tranDbConnection;
   }

   protected final Statement createStatement() throws SQLException {
      this.ensureOpen();
      return this.tranDbConnection.createStatement();
   }

   private void ensureOpen() {
      if (!this.isOpen()) {
         throw new IllegalStateException("Must open the connection first");
      }
   }

   protected final PreparedStatement prepareStatement(String var1) throws SQLException {
      this.ensureOpen();
      return this.tranDbConnection.prepareStatement(var1);
   }

   public final void lockTableForUpdate() throws SQLException {
      this.ensureOpen();
      Statement var1 = this.tranDbConnection.createStatement();

      try {
         var1.execute("LOCK " + this.tableName + " IN SHARE UPDATE EXCLUSIVE MODE");
      } finally {
         var1.close();
      }
   }
}
