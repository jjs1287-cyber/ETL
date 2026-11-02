package com.etlforge.integration.server.engine.bulkload;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.salesforce.StartAtRowProperty;
import org.etlforge.integration.server.db.trandb.ItemPropertiesTabRow;
import org.etlforge.integration.server.db.trandb.PreparedStatementExecuter;
import org.etlforge.integration.server.db.trandb.TranDbTable;

final class SalesforceBulkLoadPropertiesTab extends TranDbTable {
   public SalesforceBulkLoadPropertiesTab() {
      super("salesforcebulkloadpropertiestab");
   }

   public SalesforceBulkLoadPropertiesTab(Connection var1) {
      super("salesforcebulkloadpropertiestab", var1);
   }

   public String query(final long var1, final String var3) throws SQLException {
      SalesforceBulkLoadPropertiesTab.PreparedStatementExecuterImpl var4 = new SalesforceBulkLoadPropertiesTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setLong(1, var1);
            var1x.setString(2, var3);
         }
      };
      ItemPropertiesTabRow var5 = var4.selectSingleRow("SELECT * FROM " + this.getTableName() + " WHERE salesforcebulkload_id = ? AND keyname = ?");
      return var5 != null ? var5.get_keyvalue() : null;
   }

   public boolean getBoolean(long var1, String var3, boolean var4) throws SQLException {
      String var5 = this.query(var1, var3);
      return var5 == null ? var4 : var5.equals("1") || var5.equalsIgnoreCase("true");
   }

   public StartAtRowProperty getStartAtRow(long var1) throws SQLException {
      String var3 = this.query(var1, "start_at_row");
      if (StringUtils.isNotEmpty(var3)) {
         try {
            long var4 = Long.parseLong(var3);
            return new StartAtRowProperty(var4);
         } catch (NumberFormatException var6) {
            var6.printStackTrace();
         }
      }

      return StartAtRowProperty.DEFAULT;
   }

   public Charset getFileEncoding(long var1) throws SQLException {
      String var3 = this.query(var1, "file_encoding");
      return StringUtils.isEmpty(var3) ? Charset.defaultCharset() : Charset.forName(var3);
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<ItemPropertiesTabRow> {
      public PreparedStatementExecuterImpl() {
         super(SalesforceBulkLoadPropertiesTab.this);
      }

      protected ItemPropertiesTabRow createEmptyRow() {
         return new ItemPropertiesTabRow("salesforcebulkloadproperties_id", "salesforcebulkload_id");
      }
   }
}
