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

public final class PersistorValuesTable extends AbstractSQLiteTable {
   public PersistorValuesTable(Connection var1) {
      super(var1, "persistor_values");
   }

   public boolean create() {
      Statement var1 = null;

      try {
         var1 = this.connection.createStatement();
         var1.execute("create table " + this.tableName + " (id integer, key text, value text not null, primary key (id, key));");
         return true;
      } catch (SQLException var7) {
         var7.printStackTrace();
         return false;
      } finally {
         KongaDbUtils.close(var1);
      }
   }

   public void deleteAll(long var1) {
      PreparedStatement var3 = null;

      try {
         var3 = this.connection.prepareStatement("DELETE FROM " + this.tableName + " WHERE id = ?");
         var3.setLong(1, var1);
         var3.execute();
      } catch (SQLException var8) {
         var8.printStackTrace();
      } finally {
         KongaDbUtils.close(var3);
      }
   }

   public void putString(long var1, String var3, String var4) {
      PreparedStatement var5 = null;

      try {
         var5 = this.connection.prepareStatement("REPLACE INTO " + this.tableName + " VALUES (?, ?, ?)");
         var5.setLong(1, var1);
         var5.setString(2, var3);
         var5.setString(3, var4);
         var5.execute();
      } catch (SQLException var10) {
         var10.printStackTrace();
      } finally {
         KongaDbUtils.close(var5);
      }
   }

   public String getString(long var1, String var3) {
      PreparedStatement var4 = null;
      ResultSet var5 = null;

      try {
         var4 = this.connection.prepareStatement("SELECT value from " + this.tableName + " WHERE id = ? and key = ?");
         var4.setLong(1, var1);
         var4.setString(2, var3);
         var5 = var4.executeQuery();
         if (var5.next()) {
            return var5.getString(1);
         }
      } catch (SQLException var10) {
         var10.printStackTrace();
      } finally {
         KongaDbUtils.close(var4);
      }

      return null;
   }

   public List<Pair<String, String>> getAllValues(long var1) {
      ArrayList var3 = Lists.newArrayList();
      PreparedStatement var4 = null;
      ResultSet var5 = null;

      try {
         var4 = this.connection.prepareStatement("SELECT key, value FROM " + this.tableName + " WHERE id = ?");
         var4.setLong(1, var1);
         var5 = var4.executeQuery();

         while (var5.next()) {
            String var6 = var5.getString(1);
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
}
