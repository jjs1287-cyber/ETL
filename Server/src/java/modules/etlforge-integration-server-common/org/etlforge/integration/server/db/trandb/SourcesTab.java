package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.etlforge.integration.data.entity.id.SourceId;

public class SourcesTab extends TranDbTable {
   public SourcesTab() {
      this(null);
   }

   public SourcesTab(Connection var1) {
      super("sourcestab", var1);
   }

   public SourcesTabRow query(final int var1) throws SQLException {
      SourcesTab.PreparedStatementExecuterImpl var2 = new SourcesTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setInt(1, var1);
         }
      };
      return var2.selectSingleRowWithFilter("source_id = ?");
   }

   public SourcesTabRow query(final SourceId var1) throws SQLException {
      SourcesTab.PreparedStatementExecuterImpl var2 = new SourcesTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setString(1, var1.toString());
         }
      };
      return var2.selectSingleRow("SELECT * FROM guidsforsourcesview WHERE guid = ?");
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<SourcesTabRow> {
      public PreparedStatementExecuterImpl() {
         super(SourcesTab.this);
      }

      protected SourcesTabRow createEmptyRow() {
         return new SourcesTabRow();
      }
   }
}
