package org.jitterbit.integration.server.db.sqlite;

import java.sql.Connection;
import java.sql.SQLException;

abstract class Executor<T> {
   private final Connection conn;

   Executor(Connection var1) {
      this.conn = var1;
   }

   abstract T run() throws SQLException;

   public final T executeWithRetry() throws SQLException {
      for (int var1 = 0; var1 < 40; var1++) {
         try {
            return this.run();
         } catch (SQLException var3) {
            if (!SQLiteUtils.canRetryLocally(this.conn, var3)) {
               throw var3;
            }
         }
      }

      throw new SQLException("Database has been locked for a long time, possible database deadlock.");
   }
}
