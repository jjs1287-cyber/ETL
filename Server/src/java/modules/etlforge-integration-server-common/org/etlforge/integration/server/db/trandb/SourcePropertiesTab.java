package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SourcePropertiesTab extends TranDbTable {
   public SourcePropertiesTab() {
      this(null);
   }

   public SourcePropertiesTab(Connection var1) {
      super("sourcepropertiestab", var1);
   }

   public String query(final int var1, final String var2) throws SQLException {
      SourcePropertiesTab.PreparedStatementExecuterImpl var3 = new SourcePropertiesTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setInt(1, var1);
            var1x.setString(2, var2);
         }
      };
      ItemPropertiesTabRow var4 = var3.selectSingleRow("SELECT * FROM sourcepropertiestab WHERE source_id = ? AND keyname = ?");
      return var4 != null ? var4.get_keyvalue() : null;
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<ItemPropertiesTabRow> {
      public PreparedStatementExecuterImpl() {
         super(SourcePropertiesTab.this);
      }

      protected ItemPropertiesTabRow createEmptyRow() {
         return new ItemPropertiesTabRow("sourceproperties_id", "source_id");
      }
   }
}
