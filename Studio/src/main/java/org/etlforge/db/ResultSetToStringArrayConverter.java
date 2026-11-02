package org.etlforge.db;

import com.google.common.collect.Lists;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class ResultSetToStringArrayConverter implements ResultSetConverter {
   private final ResultSetToStringListConverter worker;
   private int maxNumberOfRows;

   public ResultSetToStringArrayConverter() {
      this(new ResultSetToStringListConverter());
   }

   public ResultSetToStringArrayConverter(ResultSetToStringListConverter var1) {
      this.worker = var1;
   }

   public String[] convert(ResultSet var1) throws SQLException {
      List var2 = this.worker.convert(var1);
      return var2 != null ? (String[]) var2.toArray(new String[var2.size()]) : null;
   }

   public List<String[]> convertAll(ResultSet var1) throws SQLException {
      ArrayList var2 = Lists.newArrayList();

      String[] var3;
      while (!this.isMaxNumberOfRowsReached(var2) && (var3 = this.convert(var1)) != null) {
         var2.add(var3);
      }

      return var2;
   }

   private boolean isMaxNumberOfRowsReached(List<String[]> var1) {
      return this.maxNumberOfRows > 0 && var1.size() >= this.maxNumberOfRows;
   }

   public void setMaxNumberOfRows(int var1) {
      this.maxNumberOfRows = var1;
   }
}
