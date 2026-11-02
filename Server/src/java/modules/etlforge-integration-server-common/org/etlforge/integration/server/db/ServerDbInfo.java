package org.etlforge.integration.server.db;

import org.etlforge.db.DBConnectionInfo;
import org.etlforge.integration.server.conf.ServerConfig;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.io.FileUtils;

public final class ServerDbInfo {
   public static final String TRAN_DB = "TranDb";
   public static final String MSG_DB = "MsgDb";
   private static final String DBINFO_SECTION = "DbInfo";
   private static final String DRIVER_PROPERTY = "Driver";
   private static final String DEFAULT_DRIVER_PROPERTY = "";
   private static final String JDBC_DRIVER_CLASS_PROPERTY = "JdbcDriverClass";
   private static final String DEFAULT_JDBC_DRIVER_CLASS = "org.postgresql.Driver";
   private static final String SQLITE_JDBC_DRIVER_CLASS = "org.etlforge.integration.server.db.sqlite.SQLiteDriver";
   private static final String JDBC_DRIVER_NAME_PROPERTY = "JdbcDriverName";
   private static final String DEFAULT_JDBC_DRIVER_NAME = "postgresql";
   private static final String SQLITE_JDBC_DRIVER_NAME_ORIG = "sqlite";
   private static final String SQLITE_JDBC_DRIVER_NAME_JB = "jbsqlite";

   private ServerDbInfo() {
   }

   private static String getDbName(String var0) {
      if (var0.equalsIgnoreCase("IDb")) return ServerConfig.getOption("DbInfo", "IDb", "IDb");
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
      String var7 = ServerConfig.getOption("DbInfo", "User", "etlforge");
      String var8 = ServerConfig.getOption("DbInfo", "Password", "");
      if (ServerConfig.getOption("DbInfo", "SQLite", false)
          || "sqlite".equalsIgnoreCase(var1) || "jbsqlite".equalsIgnoreCase(var1)
          || var2.toLowerCase(java.util.Locale.ROOT).contains("sqlite")
          || var3.toLowerCase(java.util.Locale.ROOT).contains("sqlite")) {
         throw new IllegalStateException("SQLite is not supported by ETLForge. Configure PostgreSQL in DbInfo.");
      }
      DBConnectionInfo var16 = new DBConnectionInfo(var2, var3, var5, var6, var4, var7, var8);
      return var16;
   }

   public static DBConnectionInfo getIDbConnectionInfo() {
      return getDBConnectionInfo("IDb");
   }

   public static DBConnectionInfo getTranDbConnectionInfo() {
      return getDBConnectionInfo("TranDb");
   }

   public static DBConnectionInfo getMsgDbConnectionInfo() {
      return getDBConnectionInfo("MsgDb");
   }
}
