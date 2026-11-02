package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.db.ColumnData;

public class ItemPropertiesTabRow implements TranDbTableRow {
   private final ColumnData<Integer> table_id;
   private final ColumnData<Integer> item_id;
   private final ColumnData<String> keyname = ColumnData.newStringColumn("keyname");
   private final ColumnData<String> keyvalue = ColumnData.newStringColumn("keyvalue");

   public ItemPropertiesTabRow(String var1, String var2) {
      this.table_id = ColumnData.newIntColumn(var1);
      this.item_id = ColumnData.newIntColumn(var2);
   }

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.table_id.fetchValue(var1);
      this.item_id.fetchValue(var1);
      this.keyname.fetchValue(var1);
      this.keyvalue.fetchValue(var1);
   }

   public Integer get_properties_id() {
      return this.table_id.getValue();
   }

   public Integer get_item_id() {
      return this.item_id.getValue();
   }

   public String get_keyname() {
      return this.keyname.getValue();
   }

   public String get_keyvalue() {
      return this.keyvalue.getValue();
   }
}
