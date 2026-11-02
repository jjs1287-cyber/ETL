package org.jitterbit.util.persist.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jitterbit.db.KongaDbUtils;

public abstract class AbstractSQLiteTable {
   protected final Connection connection;
   protected final String tableName;

   protected AbstractSQLiteTable(Connection var1, String var2) {
      this.connection = var1;
      this.tableName = var2;
   }

   public final boolean exists() {
      PreparedStatement var1 = null;
      ResultSet var2 = null;

      try {
         var1 = this.connection.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name=?;");
         var1.setString(1, this.tableName);
         var2 = var1.executeQuery();
         if (var2.next()) {
            return true;
         }
      } catch (SQLException var7) {
         var7.printStackTrace();
      } finally {
         KongaDbUtils.close(var2);
         KongaDbUtils.close(var1);
      }

      return false;
   }
}
