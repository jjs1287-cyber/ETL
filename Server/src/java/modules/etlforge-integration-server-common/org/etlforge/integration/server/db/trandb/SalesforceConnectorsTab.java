package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class SalesforceConnectorsTab extends TranDbTable {
   public SalesforceConnectorsTab() {
      this(null);
   }

   public SalesforceConnectorsTab(Connection var1) {
      super("salesforceconnectorstab", var1);
   }

   public SalesforceConnectorsTabRow query(final int var1) throws SQLException {
      SalesforceConnectorsTab.PreparedStatementExecuterImpl var2 = new SalesforceConnectorsTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setInt(1, var1);
         }
      };
      return var2.selectSingleRowWithFilter("salesforceconnector_id = ?");
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<SalesforceConnectorsTabRow> {
      public PreparedStatementExecuterImpl() {
         super(SalesforceConnectorsTab.this);
      }

      protected SalesforceConnectorsTabRow createEmptyRow() {
         return new SalesforceConnectorsTabRow();
      }
   }
}
