package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.accesscontrol.Group;

public class GroupsTab extends TranDbTable {
   public GroupsTab() {
      this(null);
   }

   public GroupsTab(Connection var1) {
      super("groupstab", var1);
   }

   public GroupsTabRow query(long var1) throws SQLException {
      if (!this.isOpen()) {
         this.open();
      }

      PreparedStatement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = this.prepareStatement("SELECT * FROM groupstab WHERE group_id = ?");
         var3.setLong(1, var1);
         var4 = var3.executeQuery();
         return this.getRow(var4);
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }
   }

   public Group getGroup(long var1) throws SQLException {
      GroupsTabRow var3 = this.query(var1);
      return var3 != null ? var3.createGroup() : null;
   }

   private GroupsTabRow getRow(ResultSet var1) throws SQLException {
      if (var1.next()) {
         GroupsTabRow var2 = new GroupsTabRow();
         var2.populate(var1);
         return var2;
      } else {
         return null;
      }
   }
}
