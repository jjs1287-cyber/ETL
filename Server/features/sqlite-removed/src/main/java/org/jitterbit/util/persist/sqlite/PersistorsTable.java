package org.jitterbit.util.persist.sqlite;

import com.google.common.collect.Lists;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.jitterbit.collections.Pair;
import org.jitterbit.db.KongaDbUtils;

public final class PersistorsTable extends AbstractSQLiteTable {
   public PersistorsTable(Connection var1) {
      super(var1, "persistors");
   }

   public boolean create() {
      Statement var1 = null;

      try {
         var1 = this.connection.createStatement();
         var1.execute("create table " + this.tableName + " (id integer primary key autoincrement, parent_id integer, name text not null);");
         return true;
      } catch (SQLException var7) {
         var7.printStackTrace();
         return false;
      } finally {
         KongaDbUtils.close(var1);
      }
   }

   public long getMaxRowId() throws SQLException {
      Statement var1 = this.connection.createStatement();
      ResultSet var2 = null;

      try {
         var2 = var1.executeQuery("SELECT MAX(ROWID) FROM " + this.tableName);
         if (var2.next()) {
            return var2.getLong(1);
         } else {
            throw new SQLException("No row ID");
         }
      } finally {
         KongaDbUtils.close(var2);
         KongaDbUtils.close(var1);
      }
   }

   public void delete(long var1) {
      PreparedStatement var3 = null;

      try {
         var3 = this.connection.prepareStatement("DELETE FROM " + this.tableName + " WHERE id = ?");
         var3.setLong(1, var1);
         var3.execute();
      } catch (SQLException var5) {
         KongaDbUtils.close(var3);
      }
   }

   public List<Pair<Long, String>> getIdAndNameOfAllChildren(long var1) {
      ArrayList var3 = Lists.newArrayList();
      PreparedStatement var4 = null;
      ResultSet var5 = null;

      try {
         var4 = this.connection.prepareStatement("SELECT id, name FROM " + this.tableName + " WHERE parent_id = ?");
         var4.setLong(1, var1);
         var5 = var4.executeQuery();

         while (var5.next()) {
            Long var6 = var5.getLong(1);
            String var7 = var5.getString(2);
            var3.add(Pair.of(var6, var7));
         }
      } catch (SQLException var11) {
         var11.printStackTrace();
      } finally {
         KongaDbUtils.close(var5);
         KongaDbUtils.close(var4);
      }

      return var3;
   }

   public List<Long> getChildIds(long var1, String var3) {
      ArrayList var4 = Lists.newArrayList();
      PreparedStatement var5 = null;
      ResultSet var6 = null;

      try {
         var5 = this.connection.prepareStatement("SELECT id FROM " + this.tableName + " WHERE parent_id = ? AND name = ?");
         var5.setLong(1, var1);
         var5.setString(2, var3);
         var6 = var5.executeQuery();

         while (var6.next()) {
            var4.add(var6.getLong(1));
         }
      } catch (SQLException var11) {
         var11.printStackTrace();
      } finally {
         KongaDbUtils.close(var6);
         KongaDbUtils.close(var5);
      }

      return var4;
   }

   public Long getFirstChildId(long var1, String var3) {
      PreparedStatement var4 = null;
      ResultSet var5 = null;

      try {
         var4 = this.connection.prepareStatement("SELECT id FROM " + this.tableName + " WHERE parent_id = ? AND name = ? ORDER BY id LIMIT 1");
         var4.setLong(1, var1);
         var4.setString(2, var3);
         var5 = var4.executeQuery();
         if (var5.next()) {
            return var5.getLong(1);
         }
      } catch (SQLException var10) {
         var10.printStackTrace();
      } finally {
         KongaDbUtils.close(var5);
         KongaDbUtils.close(var4);
      }

      return null;
   }

   public Long insertChild(long var1, String var3) {
      PreparedStatement var4 = null;

      try {
         var4 = this.connection.prepareStatement("INSERT INTO " + this.tableName + " (parent_id, name) values (?, ?)");
         var4.setLong(1, var1);
         var4.setString(2, var3);
         var4.execute();
         return this.getMaxRowId();
      } catch (SQLException var10) {
         var10.printStackTrace();
         return null;
      } finally {
         KongaDbUtils.close(var4);
      }
   }

   public Long getRootPersistorId(String var1) {
      PreparedStatement var2 = null;
      ResultSet var3 = null;

      try {
         var2 = this.connection.prepareStatement("SELECT id FROM " + this.tableName + " WHERE parent_id IS NULL AND name = ?");
         var2.setString(1, var1);
         var3 = var2.executeQuery();
         if (var3.next()) {
            return var3.getLong(1);
         }
      } catch (SQLException var8) {
         var8.printStackTrace();
      } finally {
         KongaDbUtils.close(var3);
         KongaDbUtils.close(var2);
      }

      return null;
   }

   public List<Pair<Long, String>> getRoots() {
      ArrayList var1 = Lists.newArrayList();
      PreparedStatement var2 = null;
      ResultSet var3 = null;

      try {
         var2 = this.connection.prepareStatement("SELECT id, name FROM " + this.tableName + " WHERE parent_id IS NULL");
         var3 = var2.executeQuery();

         while (var3.next()) {
            long var4 = var3.getLong(1);
            String var6 = var3.getString(2);
            var1.add(Pair.of(var4, var6));
         }
      } catch (SQLException var10) {
         var10.printStackTrace();
      } finally {
         KongaDbUtils.close(var3);
         KongaDbUtils.close(var2);
      }

      return var1;
   }

   public Long insertNewPersistor(String var1) {
      PreparedStatement var2 = null;

      try {
         var2 = this.connection.prepareStatement("INSERT INTO persistors (name) VALUES (?)");
         var2.setString(1, var1);
         var2.execute();
         return this.getMaxRowId();
      } catch (SQLException var7) {
         var7.printStackTrace();
      } finally {
         KongaDbUtils.close(var2);
      }

      return null;
   }

   public void selectAllToConsole() {
      Statement var1 = null;
      ResultSet var2 = null;

      try {
         var1 = this.connection.createStatement();
         var2 = var1.executeQuery("SELECT * FROM " + this.tableName);

         while (var2.next()) {
            System.out.println(String.format("%s, %s, %s", var2.getString(1), var2.getString(2), var2.getString(3)));
         }
      } catch (SQLException var7) {
         var7.printStackTrace();
      } finally {
         KongaDbUtils.close(var2);
         KongaDbUtils.close(var1);
      }
   }
}
