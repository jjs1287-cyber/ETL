package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.etlforge.db.KongaDbUtils;

public class TranDb {
   public static boolean isValidAdminUser(Connection var0, String var1, String var2) throws SQLException {
      String var3 = "SELECT UsersTab.User_Id FROM UsersTab JOIN User_GroupTab ON UsersTab.User_ID = User_GroupTab.User_ID WHERE LOWER(Login) = LOWER('"
         + var1
         + "') AND Password = '"
         + var2
         + "' AND Group_ID = 0 ";
      var3 = var3 + "UNION SELECT User_Id FROM UsersTab WHERE LOWER(Login) = LOWER('" + var1 + "') AND Password = '" + var2 + "' AND User_Id = -1";
      Statement var4 = null;
      ResultSet var5 = null;

      try {
         var4 = var0.createStatement();
         var5 = var4.executeQuery(var3);
         return var5.next();
      } finally {
         KongaDbUtils.close(var5);
         KongaDbUtils.close(var4);
      }
   }

   public static boolean isValidUser(Connection var0, String var1, String var2) throws SQLException {
      PreparedStatement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = var0.prepareStatement("SELECT Password FROM UsersTab WHERE LOWER(Login) = ?");
         var3.setString(1, var1.toLowerCase());
         var4 = var3.executeQuery();
         if (var4.next()) {
            String var5 = var4.getString(1);
            return var2 != null ? var2.equals(var5) : var4.wasNull();
         } else {
            return false;
         }
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }
   }
}
