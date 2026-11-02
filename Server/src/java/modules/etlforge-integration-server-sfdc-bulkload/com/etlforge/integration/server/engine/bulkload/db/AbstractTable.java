package com.etlforge.integration.server.engine.bulkload.db;

import com.google.common.base.Preconditions;
import com.etlforge.integration.server.engine.bulkload.BulkLoadParams;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.TimeZone;
import org.etlforge.db.KongaDbUtils;

abstract class AbstractTable {
   protected final Connection connection;
   protected final BulkLoadParams params;

   public AbstractTable(Connection var1, BulkLoadParams var2) {
      Preconditions.checkNotNull(var1, "connection");
      Preconditions.checkNotNull(var2, "params");
      this.connection = var1;
      this.params = var2;
   }

   protected static final Timestamp getTimestamp() {
      return getTimestamp(System.currentTimeMillis());
   }

   protected static final Timestamp getTimestamp(long var0) {
      int var2 = TimeZone.getDefault().getOffset(var0);
      long var3 = var0 - var2;
      return new Timestamp(var3);
   }

   protected final boolean doesOpInstanceRecordExist(String var1) throws SQLException {
      Statement var2 = this.connection.createStatement();
      ResultSet var3 = null;

      try {
         var3 = var2.executeQuery("SELECT COUNT(*) FROM " + var1 + " WHERE operation_instance_id = " + this.params.getOperationInstance());
         if (var3.next()) {
            return var3.getInt(1) > 0;
         }
      } finally {
         KongaDbUtils.close(var3);
         KongaDbUtils.close(var2);
      }

      return false;
   }

   protected static final boolean closeStatement(Statement var0) {
      if (var0 == null) {
         return true;
      }

      try {
         var0.close();
         return true;
      } catch (SQLException var2) {
         return false;
      }
   }

   protected static final void closeResultSet(ResultSet var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (Exception var2) {
         }
      }
   }
}
