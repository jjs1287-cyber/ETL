package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.etlforge.integration.data.entity.id.TargetId;

public class TargetsTab extends TranDbTable {
   public TargetsTab() {
      this(null);
   }

   public TargetsTab(Connection var1) {
      super("targetstab", var1);
   }

   public TargetsTabRow query(final int var1) throws SQLException {
      TargetsTab.PreparedStatementExecuterImpl var2 = new TargetsTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setInt(1, var1);
         }
      };
      return var2.selectSingleRowWithFilter("target_id = ?");
   }

   public TargetsTabRow query(final TargetId var1) throws SQLException {
      TargetsTab.PreparedStatementExecuterImpl var2 = new TargetsTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setString(1, var1.toString());
         }
      };
      return var2.selectSingleRow("SELECT * FROM guidsfortargetsview WHERE guid = ?");
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<TargetsTabRow> {
      public PreparedStatementExecuterImpl() {
         super(TargetsTab.this);
      }

      protected TargetsTabRow createEmptyRow() {
         return new TargetsTabRow();
      }
   }
}
