package org.etlforge.integration.server.db.trandb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.etlforge.db.KongaDbUtils;

public final class KeyValuesTab extends TranDbTable {
   public KeyValuesTab() {
      this(null);
   }

   public KeyValuesTab(Connection var1) {
      super("key_values", var1);
   }

   public String getValueForKey(String var1) throws SQLException {
      return this.getValueImpl(var1, "key_value");
   }

   private String getValueImpl(String var1, String var2) throws SQLException {
      this.ensureOpen();
      String var3 = "select " + var2 + " from key_values where key_name = ?";
      PreparedStatement var4 = this.prepareStatement(var3);
      ResultSet var5 = null;

      try {
         var4.setString(1, var1);
         var5 = var4.executeQuery();
         return var5.next() ? var5.getString(1) : null;
      } finally {
         KongaDbUtils.close(var5);
         KongaDbUtils.close(var4);
      }
   }

   private void ensureOpen() throws SQLException {
      if (!this.isOpen()) {
         this.open();
      }
   }

   public String getLongValueForKey(String var1) throws SQLException {
      return this.getValueImpl(var1, "long_key_value");
   }

   public void setKeyValue(String var1, String var2) throws SQLException {
      this.setValueImpl(var1, var2, "key_value");
   }

   private void setValueImpl(String var1, String var2, String var3) throws SQLException {
      this.ensureOpen();
      this.removeKey(var1);
      String var4 = "insert into key_values (key_name, " + var3 + ") values (?, ?)";
      PreparedStatement var5 = this.prepareStatement(var4);

      try {
         var5.setString(1, var1);
         var5.setString(2, var2);
         var5.execute();
      } finally {
         KongaDbUtils.close(var5);
      }
   }

   public void removeKey(String var1) throws SQLException {
      this.ensureOpen();
      String var2 = "delete from key_values where key_name = ?";
      PreparedStatement var3 = this.prepareStatement(var2);

      try {
         var3.setString(1, var1);
         var3.execute();
      } finally {
         KongaDbUtils.close(var3);
      }
   }

   public void setLongKeyValue(String var1, String var2) throws SQLException {
      this.setValueImpl(var1, var2, "long_key_value");
   }
}
