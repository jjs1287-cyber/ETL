package org.jitterbit.integration.server.db;

import org.jitterbit.db.DBConnectionInfo;
import org.jitterbit.integration.server.conf.ServerConfig;
import org.jitterbit.integration.server.exception.ServerConfigurationException;
import org.jitterbit.io.FileUtils;

public final class ServerDbInfo {
   public static final String TRAN_DB = "TranDb";
   public static final String MSG_DB = "MsgDb";
   private static final String DBINFO_SECTION = "DbInfo";
   private static final String DRIVER_PROPERTY = "Driver";
   private static final String DEFAULT_DRIVER_PROPERTY = "";
   private static final String JDBC_DRIVER_CLASS_PROPERTY = "JdbcDriverClass";
   private static final String DEFAULT_JDBC_DRIVER_CLASS = "org.postgresql.Driver";
   private static final String SQLITE_JDBC_DRIVER_CLASS = "org.jitterbit.integration.server.db.sqlite.SQLiteDriver";
   private static final String JDBC_DRIVER_NAME_PROPERTY = "JdbcDriverName";
   private static final String DEFAULT_JDBC_DRIVER_NAME = "postgresql";
   private static final String SQLITE_JDBC_DRIVER_NAME_ORIG = "sqlite";
   private static final String SQLITE_JDBC_DRIVER_NAME_JB = "jbsqlite";

   private ServerDbInfo() {
   }

   private static String getDbName(String var0) {
      if (var0.equalsIgnoreCase("MsgDb")) {
         return ServerConfig.getOption("DbInfo", "MsgDb", "MsgDb");
      } else {
         return var0.equalsIgnoreCase("TranDb") ? ServerConfig.getOption("DbInfo", "TranDb", "TranDb") : var0;
      }
   }

   private static DBConnectionInfo getDBConnectionInfo(String var0) {
      String var1 = ServerConfig.getOption("DbInfo", "Driver", "");
      String var2 = ServerConfig.getOption("DbInfo", "JdbcDriverClass", "org.postgresql.Driver");
      String var3 = ServerConfig.getOption("DbInfo", "JdbcDriverName", "postgresql");
      String var4 = getDbName(var0);
      String var5 = ServerConfig.getOption("DbInfo", "Server", "localhost");
      int var6 = ServerConfig.getOption("DbInfo", "Port", 5432);
      String var7 = ServerConfig.getOption("DbInfo", "User", "jitterbit");
      String var8 = ServerConfig.getOption("DbInfo", "Password", "");
      boolean var9 = ServerConfig.getOption("DbInfo", "SQLite", false);
      if (var9 || var1 == null || var1.isEmpty() || "sqlite".equalsIgnoreCase(var1) || "jbsqlite".equalsIgnoreCase(var1)) {
         var9 = true;
         var2 = "org.jitterbit.integration.server.db.sqlite.SQLiteDriver";
         var3 = "jbsqlite";
         String var10 = ServerConfig.getOption("DbInfo", "DbDir", "");
         if (var10.isEmpty()) {
            try {
               var10 = ServerConfig.getDataInterchangeDir().getAbsolutePath();
            } catch (ServerConfigurationException var12) {
               var12.printStackTrace();
               var10 = "";
            }

            var10 = FileUtils.joinPath(var10, "db");
         }

         var10 = FileUtils.joinPath(var10, var4.toLowerCase());
         var10 = var10 + ".db";
         var4 = var10;
         var6 = 0;
         var8 = "";
         var7 = "";
         var5 = "";
      }

      DBConnectionInfo var16 = new DBConnectionInfo(var2, var3, var5, var6, var4, var7, var8);
      if (var9) {
         var16.setSQLite(true);
      }

      return var16;
   }

   public static DBConnectionInfo getTranDbConnectionInfo() {
      return getDBConnectionInfo("TranDb");
   }

   public static DBConnectionInfo getMsgDbConnectionInfo() {
      return getDBConnectionInfo("MsgDb");
   }
}
