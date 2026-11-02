package org.etlforge.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class DBConnectionInfo {
   public static final long SUGGESTED_TIMEOUT_FOR_GET_CONNECTION = TimeUnit.MINUTES.toMillis(10L);
   private final String driverClass;
   private final String subprotocol;
   private final String host;
   private final int port;
   private final String database;
   private final String user;
   private final String password;
   private DBConnectionInfo.DbType databaseType = DBConnectionInfo.DbType.OTHER;

   public DBConnectionInfo(String var1, String var2, String var3, int var4, String var5, String var6, String var7) {
      this.driverClass = var1;
      this.subprotocol = var2;
      this.host = var3;
      this.port = var4;
      this.database = var5;
      this.user = var6;
      this.password = var7;
   }

   public String getJdbcUrl() {
      switch (this.databaseType) {
         case ORACLE:
            return this.createOracleUrl();
         case SQLITE:
            return this.createSQLiteUrl();
         case OTHER:
            return this.createOtherUrl();
         default:
            return this.createOtherUrl();
      }
   }

   private String createOracleUrl() {
      String var1 = "jdbc:" + this.subprotocol + ":";
      if (this.host.length() > 0) {
         var1 = var1 + "@" + this.host;
      }

      if (this.port > 0) {
         var1 = var1 + ":" + Integer.toString(this.port);
      }

      return var1;
   }

   private String createOtherUrl() {
      String var1 = "jdbc:" + this.subprotocol + ":";
      if (this.host.length() > 0) {
         var1 = var1 + "//" + this.host;
      }

      if (this.port > 0) {
         var1 = var1 + ":" + Integer.toString(this.port);
      }

      if (this.database.length() > 0) {
         var1 = var1 + "/" + this.database;
      }

      return var1;
   }

   private String createSQLiteUrl() {
      return "jdbc:" + this.subprotocol + ":" + this.database;
   }

   public void setOracle(boolean var1) {
      this.databaseType = var1 ? DBConnectionInfo.DbType.ORACLE : DBConnectionInfo.DbType.OTHER;
   }

   public void setSQLite(boolean var1) {
      this.databaseType = DBConnectionInfo.DbType.SQLITE;
   }

   public Connection getConnection() throws SQLException {
      try {
         Class.forName(this.driverClass);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      return DriverManager.getConnection(this.getJdbcUrl(), this.user, this.password);
   }

   public Connection getConnection(long var1) throws SQLException {
      Object var3 = null;
      long var4 = Math.max(Math.min(var1, 200L), 1L);
      SQLException var6 = null;

      for (int var7 = 0; var7 < var4; var7++) {
         try {
            return this.getConnection();
         } catch (SQLException var11) {
            if (var7 % 10 == 0) {
               System.err
                  .println(
                     "Waiting for database at "
                        + this.getJdbcUrl()
                        + " to respond. Will wait for another "
                        + new DecimalFormat("#.##").format(var1 * (1.0 - var7 / (float)var4) / 1000.0)
                        + " s. Last error was: "
                        + var11.getMessage()
                  );
            }

            try {
               Thread.sleep(var1 / var4);
            } catch (InterruptedException var10) {
               Thread.currentThread().interrupt();
               var10.printStackTrace();
            }

            var6 = var11;
         }
      }

      if (var6 == null) {
         var6 = new SQLException("Unkown Error.");
      }

      throw var6;
   }

   private enum DbType {
      ORACLE,
      SQLITE,
      OTHER;
   }
}
