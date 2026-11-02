package org.etlforge.integration.server.db.trandb;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.accesscontrol.Group;
import org.etlforge.integration.server.accesscontrol.User;

public class UserGroupTab extends TranDbTable {
   public UserGroupTab() {
      this(null);
   }

   public UserGroupTab(Connection var1) {
      super("user_grouptab", var1);
   }

   public List<Group> getGroupsForUser(User var1) throws SQLException {
      return this.getGroupsForUser(var1.getDatabaseId());
   }

   public List<Group> getGroupsForUser(long var1) throws SQLException {
      if (!this.isOpen()) {
         this.open();
      }

      ArrayList var3 = Lists.newArrayList();
      GroupsTab var4 = new GroupsTab(this.getConnection());
      PreparedStatement var5 = null;
      ResultSet var6 = null;

      try {
         var5 = this.prepareStatement("SELECT group_id FROM user_grouptab WHERE user_id = ?");
         var5.setLong(1, var1);
         var6 = var5.executeQuery();

         while (var6.next()) {
            long var7 = var6.getLong(1);
            var3.add(var4.getGroup(var7));
         }
      } finally {
         KongaDbUtils.close(var6);
         KongaDbUtils.close(var5);
      }

      return var3;
   }

   public List<User> getMembers(Group var1) throws SQLException {
      return this.getMembers(var1.getDatabaseId());
   }

   public List<User> getMembers(long var1) throws SQLException {
      if (!this.isOpen()) {
         this.open();
      }

      ArrayList var3 = Lists.newArrayList();
      UsersTab var4 = new UsersTab(this.getConnection());
      PreparedStatement var5 = null;
      ResultSet var6 = null;

      try {
         var5 = this.prepareStatement("SELECT user_id FROM user_grouptab WHERE group_id = ?");
         var5.setLong(1, var1);
         var6 = var5.executeQuery();

         while (var6.next()) {
            long var7 = var6.getLong(1);
            var3.add(var4.getUser(var7));
         }
      } finally {
         KongaDbUtils.close(var6);
         KongaDbUtils.close(var5);
      }

      return var3;
   }
}
