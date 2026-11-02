package org.etlforge.integration.server.db.trandb;

import com.google.common.collect.Lists;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.db.KongaDbUtils;

public abstract class PreparedStatementExecuter<R extends TranDbTableRow> {
   private final TranDbTable table;

   protected PreparedStatementExecuter(TranDbTable var1) {
      this.table = var1;
   }

   protected abstract R createEmptyRow();

   public final R selectSingleRow(String var1) throws SQLException {
      return (new PreparedStatementExecuter<R>.Worker<R>() {
         protected R convertResultSet(ResultSet var1) throws SQLException {
            return (R)(var1.next() ? PreparedStatementExecuter.this.createAndPopulateRow(var1) : null);
         }
      }).run(var1);
   }

   public final R selectSingleRowWithFilter(String var1) throws SQLException {
      return this.selectSingleRow("SELECT * FROM " + this.table.getTableName() + " WHERE " + var1);
   }

   public final List<R> selectSeveralRows(String var1) throws SQLException {
      return (new PreparedStatementExecuter<R>.Worker<List<R>>() {
         protected List<R> convertResultSet(ResultSet var1) throws SQLException {
            ArrayList var2 = Lists.newArrayList();

            while (var1.next()) {
               var2.add(PreparedStatementExecuter.this.createAndPopulateRow(var1));
            }

            return var2;
         }
      }).run(var1);
   }

   protected abstract void bindParams(PreparedStatement var1) throws SQLException;

   private R createAndPopulateRow(ResultSet var1) throws SQLException {
      TranDbTableRow var2 = this.createEmptyRow();
      var2.populate(var1);
      return (R)var2;
   }

   private abstract class Worker<V> {
      private Worker() {
      }

      public final V run(String var1) throws SQLException {
         if (!PreparedStatementExecuter.this.table.isOpen()) {
            PreparedStatementExecuter.this.table.open();
         }

         PreparedStatement var2 = null;
         ResultSet var3 = null;

         try {
            var2 = PreparedStatementExecuter.this.table.prepareStatement(var1);
            PreparedStatementExecuter.this.bindParams(var2);
            var3 = var2.executeQuery();
            return this.convertResultSet(var3);
         } finally {
            KongaDbUtils.close(var3);
            KongaDbUtils.close(var2);
         }
      }

      protected abstract V convertResultSet(ResultSet var1) throws SQLException;
   }
}
