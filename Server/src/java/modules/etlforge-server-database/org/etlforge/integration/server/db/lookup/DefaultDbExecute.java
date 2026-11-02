package org.etlforge.integration.server.db.lookup;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.db.ResultSetToStringArrayConverter;
import org.etlforge.db.ResultSetToStringListConverter;
import org.etlforge.integration.server.db.source.SourceValueReader;

public final class DefaultDbExecute implements DbExecute {
   private final Connection connection;
   private final ResultSetToStringArrayConverter converter;
   private boolean autoCommit;
   private final SourceValueReader valueReader;

   public DefaultDbExecute(Connection var1) {
      Preconditions.checkNotNull(var1);
      this.connection = var1;
      this.autoCommit = this.checkAutoCommit();
      this.valueReader = new SourceValueReader();
      this.converter = new ResultSetToStringArrayConverter(new ResultSetToStringListConverter() {
         @Override
         protected String readValueAsString(ResultSet var1, int var2) throws SQLException {
            return DefaultDbExecute.this.valueReader.readColumnValueAsString(var1, var2);
         }
      });
   }

   private boolean checkAutoCommit() {
      try {
         return this.connection.getAutoCommit();
      } catch (SQLException var2) {
         return true;
      }
   }

   @Override
   public DbExecuteResult execute(String var1, int var2) throws SQLException {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "sql may not be null or empty");
      Statement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = this.connection.createStatement();
         if (var2 > 0) {
            var3.setMaxRows(var2);
         }

         boolean var6 = var3.execute(var1);
         DbExecuteResult var5;
         if (var6) {
            var4 = var3.getResultSet();
            List var7 = this.converter.convertAll(var4);
            var5 = new DbExecuteResult(var7);
         } else {
            int var11 = var3.getUpdateCount();
            var5 = new DbExecuteResult(var11);
         }

         return var5;
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }
   }

   @Override
   public void setAutoCommit(boolean var1) throws SQLException {
      if (var1 != this.autoCommit) {
         if (var1) {
            try {
               this.connection.setAutoCommit(var1);
            } catch (SQLException var3) {
            }
         } else {
            this.connection.setAutoCommit(var1);
         }

         this.autoCommit = var1;
      }
   }
}
