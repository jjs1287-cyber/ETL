package org.jitterbit.integration.server.db.sqlite;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Logger;
import org.jitterbit.db.KongaDbUtils;
import org.sqlite.JDBC;

public class SQLiteDriver implements Driver {
   private static final String JB_PREFIX = "jdbc:jbsqlite:";
   private final Driver impl = new JDBC();

   @Override
   public boolean acceptsURL(String var1) throws SQLException {
      return var1 != null && var1.toLowerCase().startsWith("jdbc:jbsqlite:");
   }

   @Override
   public Connection connect(String var1, Properties var2) throws SQLException {
      return !this.acceptsURL(var1) ? null : new SQLiteConnection(var1, var2);
   }

   @Override
   public int getMajorVersion() {
      return this.impl.getMajorVersion();
   }

   @Override
   public int getMinorVersion() {
      return this.impl.getMinorVersion();
   }

   @Override
   public DriverPropertyInfo[] getPropertyInfo(String var1, Properties var2) throws SQLException {
      return this.impl.getPropertyInfo(var1, var2);
   }

   @Override
   public boolean jdbcCompliant() {
      return this.impl.jdbcCompliant();
   }

   public static void main(String[] var0) {
      Connection var1 = null;

      try {
         Class.forName("org.jitterbit.integration.server.db.sqlite.SQLiteDriver");
         var1 = DriverManager.getConnection("jdbc:jbsqlite:Z:/tmp/test.db");
         Statement var2 = var1.createStatement();
         var2.executeUpdate("drop table if exists people;");
         var2.executeUpdate("create table people (name, occupation);");
         PreparedStatement var3 = var1.prepareStatement("insert into people values (?, ?);");
         var3.setString(1, "Gandhi");
         var3.setString(2, "politics");
         var3.execute();

         while (true) {
            try {
               var1.setAutoCommit(false);
               var3.setString(1, "Turing");
               var3.setString(2, "computers");
               var3.execute();
               var3.setString(1, "Wittgenstein");
               var3.setString(2, "smartypants");
               var3.execute();
               var1.commit();
               var1.setAutoCommit(true);
               break;
            } catch (SQLException var10) {
               var10.printStackTrace();
               if (!SQLiteUtils.isDbLockedException(var10)) {
                  throw var10;
               }

               KongaDbUtils.rollback(var1);
               var1.setAutoCommit(true);
            }
         }

         ResultSet var4 = var2.executeQuery("select * from people;");

         while (var4.next()) {
            System.out.println("name = " + var4.getString("name"));
            System.out.println("job = " + var4.getString("occupation"));
         }

         var4.close();
      } catch (ClassNotFoundException var11) {
         var11.printStackTrace();
      } catch (SQLException var12) {
         var12.printStackTrace();
      } finally {
         KongaDbUtils.close(var1);
      }
   }

   @Override
   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      throw new SQLFeatureNotSupportedException();
   }

   static {
      try {
         DriverManager.registerDriver(new SQLiteDriver());
      } catch (SQLException var1) {
         var1.printStackTrace();
      }
   }
}
