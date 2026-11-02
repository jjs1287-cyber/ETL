package org.etlforge.integration.server.db.trandb;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.integration.server.accesscontrol.User;
import org.etlforge.integration.server.db.ColumnData;

public class UsersTabRow implements TranDbTableRow {
   private final ColumnData<Integer> user_id = ColumnData.newIntColumn("user_id");
   private final ColumnData<String> first_name = ColumnData.newStringColumn("first_name");
   private final ColumnData<String> last_name = ColumnData.newStringColumn("last_name");
   private final ColumnData<String> login = ColumnData.newStringColumn("login");
   private final ColumnData<String> password = ColumnData.newStringColumn("password");
   private final ColumnData<String> email = ColumnData.newStringColumn("email");
   private final ColumnData<Integer> access_level = ColumnData.newIntColumn("access_level");

   @Override
   public void populate(ResultSet var1) throws SQLException {
      this.user_id.fetchValue(var1);
      this.first_name.fetchValue(var1);
      this.last_name.fetchValue(var1);
      this.login.fetchValue(var1);
      this.password.fetchValue(var1);
      this.email.fetchValue(var1);
      this.access_level.fetchValue(var1);
   }

   public User createUser() {
      if (this.user_id.getValue() == null) {
         throw new IllegalStateException("This row has not been populated");
      } else {
         return new User(this.user_id.getValue().intValue(), this.login.getValue());
      }
   }

   public Integer get_user_id() {
      return this.user_id.getValue();
   }

   public String get_first_name() {
      return this.first_name.getValue();
   }

   public String get_last_name() {
      return this.last_name.getValue();
   }

   public String get_login() {
      return this.login.getValue();
   }

   public String get_password() {
      return this.password.getValue();
   }

   public String get_email() {
      return this.email.getValue();
   }

   public Integer get_access_level() {
      return this.access_level.getValue();
   }
}
