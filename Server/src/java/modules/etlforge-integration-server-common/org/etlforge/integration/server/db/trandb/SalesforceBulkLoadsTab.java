package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.etlforge.integration.data.entity.id.SalesforceBulkLoadId;

public final class SalesforceBulkLoadsTab extends TranDbTable {
   public SalesforceBulkLoadsTab() {
      this(null);
   }

   public SalesforceBulkLoadsTab(Connection var1) {
      super("salesforcebulkloadstab", var1);
   }

   public SalesforceBulkLoadsTabRow query(final long var1) throws SQLException {
      SalesforceBulkLoadsTab.PreparedStatementExecuterImpl var3 = new SalesforceBulkLoadsTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setLong(1, var1);
         }
      };
      return var3.selectSingleRowWithFilter("salesforcebulkload_id = ?");
   }

   public SalesforceBulkLoadsTabRow query(final SalesforceBulkLoadId var1) throws SQLException {
      SalesforceBulkLoadsTab.PreparedStatementExecuterImpl var2 = new SalesforceBulkLoadsTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setString(1, var1.toString());
         }
      };
      return var2.selectSingleRow("SELECT * FROM guidsforsalesforcebulkloadsview WHERE guid = ?");
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<SalesforceBulkLoadsTabRow> {
      public PreparedStatementExecuterImpl() {
         super(SalesforceBulkLoadsTab.this);
      }

      protected SalesforceBulkLoadsTabRow createEmptyRow() {
         return new SalesforceBulkLoadsTabRow();
      }
   }
}
