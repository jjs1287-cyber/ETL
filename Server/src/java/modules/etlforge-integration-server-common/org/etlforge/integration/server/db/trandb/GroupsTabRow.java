package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.accesscontrol.Group;
import org.etlforge.integration.server.db.ColumnData;

public class GroupsTabRow implements TranDbTableRow {
   private final ColumnData<Integer> group_id = ColumnData.newIntColumn("group_id");
   private final ColumnData<String> group_name = ColumnData.newStringColumn("group_name");
   private final ColumnData<String> description = ColumnData.newStringColumn("description");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.group_id.fetchValue(var1);
      this.group_name.fetchValue(var1);
      this.description.fetchValue(var1);
   }

   public Group createGroup() {
      if (this.group_id.getValue() == null) {
         throw new IllegalStateException("This row has not been populated");
      } else {
         return new Group(this.group_id.getValue().intValue(), this.group_name.getValue());
      }
   }

   public Integer get_group_id() {
      return this.group_id.getValue();
   }

   public String get_group_name() {
      return this.group_name.getValue();
   }

   public String get_description() {
      return this.description.getValue();
   }
}
