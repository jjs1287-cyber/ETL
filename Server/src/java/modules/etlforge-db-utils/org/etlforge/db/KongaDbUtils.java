package org.etlforge.db;

import com.google.common.base.Preconditions;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class KongaDbUtils {
   public static void isValidForWriting(Connection var0) throws SQLException {
      Preconditions.checkState(!var0.isClosed(), "Connection is closed");
      Preconditions.checkState(!var0.isReadOnly(), "Connection is read only!");
   }

   public static boolean close(Connection var0, boolean var1) {
      if (var0 == null) {
         return true;
      }

      boolean var2 = true;

      try {
         if (var1) {
            var0.commit();
         }
      } catch (SQLException var12) {
         var2 = false;
         var12.printStackTrace();
      } finally {
         try {
            var0.close();
         } catch (SQLException var11) {
            var2 = false;
            var11.printStackTrace();
         }
      }

      return var2;
   }

   public static boolean close(Connection var0) {
      return close(var0, false);
   }

   public static boolean close(Statement var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (SQLException var2) {
            var2.printStackTrace();
            return false;
         }
      }

      return true;
   }

   public static boolean close(ResultSet var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (SQLException var2) {
            var2.printStackTrace();
            return false;
         }
      }

      return true;
   }

   public static boolean rollback(Connection var0) {
      try {
         var0.rollback();
         return true;
      } catch (SQLException var2) {
         System.err.println("WARNING: Rollback failed. " + var2.getMessage());
         var2.printStackTrace();
         return false;
      }
   }

   public static int getSequenceID(String var0, Connection var1) throws SQLException {
      Statement var2 = null;
      ResultSet var3 = null;

      try {
         var2 = var1.createStatement();
         var3 = var2.executeQuery("SELECT currval('" + var0 + "_id_seq')");
         if (var3.next()) {
            return var3.getInt(1);
         } else {
            throw new SQLException("Failed to get auto generated ID for " + var0);
         }
      } finally {
         close(var3);
         close(var2);
      }
   }

   public static int getNextId(String var0, String var1, Connection var2) throws SQLException {
      if (var2.getAutoCommit()) {
         throw new SQLException("Cannot get the next id using a connection whith auto commit = true!");
      }

      lockTableForUpdate(var0, var2);
      String var3 = "SELECT MAX(" + var1 + ") FROM " + var0;
      Statement var4 = null;
      ResultSet var5 = null;

      try {
         var4 = var2.createStatement();
         var5 = var4.executeQuery(var3);
         if (!var5.next()) {
            throw new SQLException("This should not have happened but the sql statement\"" + var3 + "\" returned no result...");
         } else {
            return var5.getInt(1) + 1;
         }
      } finally {
         close(var4);
         close(var5);
      }
   }

   public static void lockTableForUpdate(String var0, Connection var1) throws SQLException {
      Statement var2 = var1.createStatement();

      try {
         var2.executeUpdate("LOCK " + var0 + " IN SHARE UPDATE EXCLUSIVE MODE");
      } finally {
         close(var2);
      }
   }

   public static int getIdentity(Connection var0) throws SQLException {
      Statement var1 = null;
      ResultSet var2 = null;

      try {
         var1 = var0.createStatement();
         var2 = var1.executeQuery("SELECT last_insert_id()");
         if (var2.next()) {
            return var2.getInt(1);
         } else {
            throw new SQLException("Failed to get auto generated ID");
         }
      } finally {
         close(var2);
         close(var1);
      }
   }

   public static int getIdentity(PreparedStatement var0) throws SQLException {
      ResultSet var1 = null;

      try {
         var1 = var0.executeQuery();
         if (var1.next()) {
            return var1.getInt(1);
         } else {
            throw new SQLException("Failed to get auto generated ID from prepared statement.");
         }
      } finally {
         close(var1);
      }
   }

   private KongaDbUtils() {
   }
}
