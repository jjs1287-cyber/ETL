package org.jitterbit.integration.server.db.sqlite;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteUtils {
   private static final String LOCKED = "locked";

   public static boolean isDbLockedException(SQLException var0) {
      return var0.getMessage().contains("locked");
   }

   public static boolean canRetryLocally(Connection var0, SQLException var1) throws SQLException {
      return var0.getAutoCommit() && isDbLockedException(var1);
   }
}
