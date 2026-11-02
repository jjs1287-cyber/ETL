package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TargetPropertiesTab extends TranDbTable {
   public TargetPropertiesTab() {
      this(null);
   }

   public TargetPropertiesTab(Connection var1) {
      super("targetpropertiestab", var1);
   }

   public String query(final int var1, final String var2) throws SQLException {
      TargetPropertiesTab.PreparedStatementExecuterImpl var3 = new TargetPropertiesTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setInt(1, var1);
            var1x.setString(2, var2);
         }
      };
      ItemPropertiesTabRow var4 = var3.selectSingleRow("SELECT * FROM targetpropertiestab WHERE target_id = ? AND keyname = ?");
      return var4 != null ? var4.get_keyvalue() : null;
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<ItemPropertiesTabRow> {
      public PreparedStatementExecuterImpl() {
         super(TargetPropertiesTab.this);
      }

      protected ItemPropertiesTabRow createEmptyRow() {
         return new ItemPropertiesTabRow("targetproperties_id", "target_id");
      }
   }
}
