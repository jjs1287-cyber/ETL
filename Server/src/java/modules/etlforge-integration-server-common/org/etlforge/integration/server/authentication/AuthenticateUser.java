package org.etlforge.integration.server.authentication;

import java.sql.Connection;
import java.sql.SQLException;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.integration.server.db.ServerDbInfo;
import org.etlforge.integration.server.db.trandb.TranDb;

public final class AuthenticateUser {
   public static boolean isValidUser(String var0, String var1) throws SQLException {
      Connection var2 = null;

      try {
         var2 = ServerDbInfo.getTranDbConnectionInfo().getConnection();
         return TranDb.isValidUser(var2, var0, var1);
      } finally {
         KongaDbUtils.close(var2);
      }
   }

   public static boolean isValidAdminUser(String var0, String var1) throws SQLException {
      Connection var2 = null;

      try {
         var2 = ServerDbInfo.getTranDbConnectionInfo().getConnection();
         return TranDb.isValidAdminUser(var2, var0, var1);
      } finally {
         KongaDbUtils.close(var2);
      }
   }

   private AuthenticateUser() {
   }
}
