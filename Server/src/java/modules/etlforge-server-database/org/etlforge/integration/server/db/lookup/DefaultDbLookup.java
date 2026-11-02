package org.etlforge.integration.server.db.lookup;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.lang.StringUtils;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.db.source.SourceValueReader;

public final class DefaultDbLookup implements DbLookup {
   private final Connection connection;
   private boolean autoCommit;
   private final SourceValueReader valueReader;

   public DefaultDbLookup(Connection var1) {
      Preconditions.checkNotNull(var1);
      this.connection = var1;
      this.valueReader = new SourceValueReader();
      this.autoCommit = this.checkAutoCommit();
   }

   private boolean checkAutoCommit() {
      try {
         return this.connection.getAutoCommit();
      } catch (SQLException var2) {
         return true;
      }
   }

   @Override
   public DbLookupResult lookup(String var1) throws SQLException {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "sql may not be null or empty");
      Statement var2 = null;
      ResultSet var3 = null;

      try {
         var2 = this.connection.createStatement();
         boolean var4 = var2.execute(var1);
         DbLookupResult var5;
         if (var4) {
            var3 = var2.getResultSet();
            String var6 = null;
            if (var3.next()) {
               var6 = this.valueReader.readColumnValueAsString(var3, 1);
            }

            var5 = new DbLookupResult(var6);
         } else {
            var5 = new DbLookupResult(var2.getUpdateCount());
         }

         return var5;
      } finally {
         KongaDbUtils.close(var3);
         KongaDbUtils.close(var2);
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
