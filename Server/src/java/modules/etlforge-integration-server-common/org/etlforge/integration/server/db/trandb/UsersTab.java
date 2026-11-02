package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.etlforge.integration.server.accesscontrol.User;

public class UsersTab extends TranDbTable {
   public UsersTab() {
      this(null);
   }

   public UsersTab(Connection var1) {
      super("userstab", var1);
   }

   public User getUser(final String var1, final String var2) throws SQLException {
      UsersTab.PreparedStatementExecuterImpl var3 = new UsersTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setString(1, var1.toLowerCase());
            var1x.setString(2, var2);
         }
      };
      UsersTabRow var4 = var3.selectSingleRow("SELECT * FROM userstab WHERE LOWER(login) = ? and password = ?");
      return var4 != null ? var4.createUser() : null;
   }

   public User getUser(long var1) throws SQLException {
      UsersTabRow var3 = this.query(var1);
      return var3 != null ? var3.createUser() : null;
   }

   public UsersTabRow query(final long var1) throws SQLException {
      UsersTab.PreparedStatementExecuterImpl var3 = new UsersTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setLong(1, var1);
         }
      };
      return var3.selectSingleRowWithFilter("user_id = ?");
   }

   public UsersTabRow query(final String var1) throws SQLException {
      UsersTab.PreparedStatementExecuterImpl var2 = new UsersTab.PreparedStatementExecuterImpl() {
         @Override
         protected void bindParams(PreparedStatement var1x) throws SQLException {
            var1x.setString(1, var1);
         }
      };
      return var2.selectSingleRow("SELECT * FROM guidsforusersview WHERE guid = ?");
   }

   private abstract class PreparedStatementExecuterImpl extends PreparedStatementExecuter<UsersTabRow> {
      public PreparedStatementExecuterImpl() {
         super(UsersTab.this);
      }

      protected UsersTabRow createEmptyRow() {
         return new UsersTabRow();
      }
   }
}
