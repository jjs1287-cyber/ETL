package org.etlforge.integration.database.info.retriever;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.etlforge.db.DBConnectionInfo;
import org.etlforge.db.KongaDbUtils;

public abstract class DatabaseAccessor {
   private Connection connection;

   public final void setConnection(Connection var1) {
      if (var1 != this.connection) {
         this.close();
         this.connection = var1;
      }
   }

   public final void open(String var1, String var2, String var3) throws SQLException {
      this.close();
      this.setConnection(DriverManager.getConnection(var1, var2, var3));
   }

   public final void open(DBConnectionInfo var1) throws SQLException {
      this.close();
      this.setConnection(var1.getConnection());
   }

   public final Connection getConnection() {
      return this.connection;
   }

   public void close() {
      KongaDbUtils.close(this.connection);
   }
}
