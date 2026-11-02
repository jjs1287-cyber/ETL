package org.etlforge.integration.server.db.lookup;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;

public class DbExecuteResult {
   private final ImmutableList<String[]> rows;
   private final int updateCount;

   public DbExecuteResult(int var1) {
      this.rows = ImmutableList.of();
      this.updateCount = var1;
   }

   public DbExecuteResult(List<String[]> var1) {
      this.rows = ImmutableList.copyOf(var1);
      this.updateCount = -1;
   }

   public List<String[]> getResultSet() {
      return Lists.newArrayList(this.rows);
   }

   public int getUpdateCount() {
      return this.updateCount;
   }
}
