package org.etlforge.integration.server.db;

import com.google.common.collect.Maps;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.system.Disposable;

public final class PreparedStatementCache implements Disposable {
   private static final int MAX_SIZE = 30;
   private final Connection connection;
   private final Map<String, PreparedStatement> cache;
   private boolean mustBeClosedByCaller;
   private boolean setFetchDirection = true;
   private int fetchSize;

   public PreparedStatementCache(Connection var1) {
      this.connection = var1;
      this.cache = Maps.newHashMap();
   }

   public void setFetchSize(int var1) {
      this.fetchSize = var1;
   }

   public PreparedStatement getPreparedStatement(String var1) throws SQLException {
      PreparedStatement var2 = this.cache.get(var1);
      if (var2 != null) {
         this.mustBeClosedByCaller = false;
      } else {
         var2 = this.createNewStatement(var1);
         if (this.cache.size() < 30) {
            this.cache.put(var1, var2);
            this.mustBeClosedByCaller = false;
         } else {
            this.mustBeClosedByCaller = true;
         }
      }

      return var2;
   }

   public PreparedStatement createNewStatement(String var1) throws SQLException {
      PreparedStatement var2 = this.connection.prepareStatement(var1);
      if (this.fetchSize != 0) {
         try {
            var2.setFetchSize(this.fetchSize);
         } catch (SQLException var5) {
            var5.printStackTrace();
            this.fetchSize = 0;
         }
      }

      if (this.setFetchDirection) {
         try {
            var2.setFetchDirection(1000);
         } catch (SQLException var4) {
            var4.printStackTrace();
            this.setFetchDirection = false;
         }
      }

      return var2;
   }

   public boolean mustLastReturnedStatementBeClosedByCaller() {
      return this.mustBeClosedByCaller;
   }

   @Override
   public void dispose() {
      for (PreparedStatement var2 : this.cache.values()) {
         KongaDbUtils.close(var2);
      }
   }
}
