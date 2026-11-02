package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.db.ColumnData;

public class UserGroupTabRow implements TranDbTableRow {
   private final ColumnData<Integer> user_groupid = ColumnData.newIntColumn("user_groupid");
   private final ColumnData<Integer> user_id = ColumnData.newIntColumn("user_id");
   private final ColumnData<Integer> group_id = ColumnData.newIntColumn("group_id");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.user_groupid.fetchValue(var1);
      this.user_id.fetchValue(var1);
      this.group_id.fetchValue(var1);
   }

   public Integer get_user_groupid() {
      return this.user_groupid.getValue();
   }

   public Integer get_user_id() {
      return this.user_id.getValue();
   }

   public Integer get_group_id() {
      return this.group_id.getValue();
   }
}
