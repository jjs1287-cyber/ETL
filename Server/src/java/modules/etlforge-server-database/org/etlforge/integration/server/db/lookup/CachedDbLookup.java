package org.etlforge.integration.server.db.lookup;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.sql.SQLException;
import java.util.Map;

public class CachedDbLookup implements DbLookup {
   private final DbLookup impl;
   private final Map<String, DbLookupResult> cache;
   private final Object lock;

   public CachedDbLookup(DbLookup var1) {
      Preconditions.checkNotNull(var1, "impl");
      this.impl = var1;
      this.cache = Maps.newHashMap();
      this.lock = new Object();
   }

   @Override
   public DbLookupResult lookup(String var1) throws SQLException {
      synchronized (this.lock) {
         DbLookupResult var3 = this.cache.get(var1);
         if (var3 == null) {
            var3 = this.impl.lookup(var1);
            this.cache.put(var1, var3);
         }

         return var3;
      }
   }

   @Override
   public void setAutoCommit(boolean var1) throws SQLException {
      this.impl.setAutoCommit(var1);
   }
}
