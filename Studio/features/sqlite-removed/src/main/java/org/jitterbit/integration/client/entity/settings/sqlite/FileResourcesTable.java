package org.jitterbit.integration.client.entity.settings.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.jitterbit.db.KongaDbUtils;
import org.jitterbit.integration.data.entity.IntegrationEntityId;
import org.jitterbit.util.persist.sqlite.AbstractSQLiteTable;
import org.jitterbit.util.string.StringLimiter;

final class FileResourcesTable extends AbstractSQLiteTable {
   public FileResourcesTable(Connection var1) {
      super(var1, "file_resources");
   }

   public boolean create() {
      Statement var1 = null;

      try {
         var1 = this.connection.createStatement();
         var1.execute("create table " + this.tableName + " (entity text, name text, value text not null, primary key (entity, name));");
         return true;
      } catch (SQLException var7) {
         var7.printStackTrace();
         return false;
      } finally {
         KongaDbUtils.close(var1);
      }
   }

   public boolean exists(IntegrationEntityId var1, String var2) {
      PreparedStatement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = this.connection.prepareStatement("SELECT COUNT(*) FROM " + this.tableName + " WHERE entity = ? and name = ?");
         this.bindIdAndName(var3, var1, var2);
         var4 = var3.executeQuery();
         if (var4.next()) {
            long var5 = var4.getLong(1);
            return var5 == 1L;
         }
      } catch (SQLException var11) {
         var11.printStackTrace();
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }

      return false;
   }

   private void bindIdAndName(PreparedStatement var1, IntegrationEntityId var2, String var3) throws SQLException {
      var1.setString(1, var2.toString());
      var1.setString(2, var3);
   }

   public String read(IntegrationEntityId var1, String var2) {
      PreparedStatement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = this.connection.prepareStatement("SELECT value FROM " + this.tableName + " WHERE entity = ? and name = ?");
         this.bindIdAndName(var3, var1, var2);
         var4 = var3.executeQuery();
         if (var4.next()) {
            return var4.getString(1);
         }
      } catch (SQLException var9) {
         var9.printStackTrace();
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }

      return null;
   }

   public void write(IntegrationEntityId var1, String var2, String var3) {
      PreparedStatement var4 = null;

      try {
         var4 = this.connection.prepareStatement("REPLACE INTO " + this.tableName + " VALUES (?, ?, ?)");
         this.bindIdAndName(var4, var1, var2);
         var4.setString(3, var3);
         var4.execute();
      } catch (SQLException var9) {
         var9.printStackTrace();
      } finally {
         KongaDbUtils.close(var4);
      }
   }

   public boolean delete(IntegrationEntityId var1, String var2) {
      PreparedStatement var3 = null;

      try {
         var3 = this.connection.prepareStatement("DELETE FROM " + this.tableName + " WHERE entity = ? AND name = ?");
         this.bindIdAndName(var3, var1, var2);
         var3.execute();
         return true;
      } catch (SQLException var9) {
         var9.printStackTrace();
         return false;
      } finally {
         KongaDbUtils.close(var3);
      }
   }

   public void copyTo(IntegrationEntityId var1, FileResourcesTable var2) {
      PreparedStatement var3 = null;
      ResultSet var4 = null;

      try {
         var3 = this.connection.prepareStatement("SELECT name, value FROM " + this.tableName + " WHERE entity = ?");
         var3.setString(1, var1.toString());
         var4 = var3.executeQuery();

         while (var4.next()) {
            String var5 = var4.getString(1);
            String var6 = var4.getString(2);
            var2.write(var1, var5, var6);
         }
      } catch (SQLException var10) {
         var10.printStackTrace();
      } finally {
         KongaDbUtils.close(var4);
         KongaDbUtils.close(var3);
      }
   }

   public void dumpToConsole() {
      Statement var1 = null;
      ResultSet var2 = null;

      try {
         var1 = this.connection.createStatement();
         var2 = var1.executeQuery("SELECT * FROM " + this.tableName);

         while (var2.next()) {
            String var3 = String.format("[ID: %s] [%s] = %s", var2.getString(1), var2.getString(2), StringLimiter.limit(var2.getString(3), 100));
            System.out.println(var3);
         }
      } catch (SQLException var7) {
         var7.printStackTrace();
      } finally {
         KongaDbUtils.close(var2);
         KongaDbUtils.close(var1);
      }
   }
}
