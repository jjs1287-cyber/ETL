package org.jitterbit.integration.server.db.sqlite;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import org.sqlite.JDBC;

class SQLiteConnection implements Connection {
   private final Connection impl;

   SQLiteConnection(String var1, Properties var2) throws SQLException {
      JDBC var3 = new JDBC();
      String var4 = "jdbc:" + var1.substring(7);
      this.impl = var3.connect(var4, var2);
      if (this.impl == null) {
         throw new SQLException("Invalid JDBC connection URL: " + var4);
      }
   }

   @Override
   public void clearWarnings() throws SQLException {
      this.impl.clearWarnings();
   }

   @Override
   public void close() throws SQLException {
      this.impl.close();
   }

   @Override
   public void commit() throws SQLException {
      this.impl.commit();
   }

   @Override
   public Array createArrayOf(String var1, Object[] var2) throws SQLException {
      return this.impl.createArrayOf(var1, var2);
   }

   @Override
   public Blob createBlob() throws SQLException {
      return this.impl.createBlob();
   }

   @Override
   public Clob createClob() throws SQLException {
      return this.impl.createClob();
   }

   @Override
   public NClob createNClob() throws SQLException {
      return this.impl.createNClob();
   }

   @Override
   public SQLXML createSQLXML() throws SQLException {
      return this.impl.createSQLXML();
   }

   @Override
   public Statement createStatement() throws SQLException {
      return new SQLiteStatement(this.impl.createStatement(), this);
   }

   @Override
   public Statement createStatement(int var1, int var2, int var3) throws SQLException {
      return new SQLiteStatement(this.impl.createStatement(var1, var2, var3), this);
   }

   @Override
   public Statement createStatement(int var1, int var2) throws SQLException {
      return new SQLiteStatement(this.impl.createStatement(var1, var2), this);
   }

   @Override
   public Struct createStruct(String var1, Object[] var2) throws SQLException {
      return this.impl.createStruct(var1, var2);
   }

   @Override
   public boolean getAutoCommit() throws SQLException {
      return this.impl.getAutoCommit();
   }

   @Override
   public String getCatalog() throws SQLException {
      return this.impl.getCatalog();
   }

   @Override
   public Properties getClientInfo() throws SQLException {
      return this.impl.getClientInfo();
   }

   @Override
   public String getClientInfo(String var1) throws SQLException {
      return this.impl.getClientInfo(var1);
   }

   @Override
   public int getHoldability() throws SQLException {
      return this.impl.getHoldability();
   }

   @Override
   public DatabaseMetaData getMetaData() throws SQLException {
      return this.impl.getMetaData();
   }

   @Override
   public int getTransactionIsolation() throws SQLException {
      return this.impl.getTransactionIsolation();
   }

   @Override
   public Map<String, Class<?>> getTypeMap() throws SQLException {
      return this.impl.getTypeMap();
   }

   @Override
   public SQLWarning getWarnings() throws SQLException {
      return this.impl.getWarnings();
   }

   @Override
   public boolean isClosed() throws SQLException {
      return this.impl.isClosed();
   }

   @Override
   public boolean isReadOnly() throws SQLException {
      return this.impl.isReadOnly();
   }

   @Override
   public boolean isValid(int var1) throws SQLException {
      return this.impl.isValid(var1);
   }

   @Override
   public boolean isWrapperFor(Class<?> var1) throws SQLException {
      return this.impl.isWrapperFor(var1);
   }

   @Override
   public String nativeSQL(String var1) throws SQLException {
      return this.impl.nativeSQL(var1);
   }

   @Override
   public CallableStatement prepareCall(String var1, int var2, int var3, int var4) throws SQLException {
      return this.impl.prepareCall(var1, var2, var3, var4);
   }

   @Override
   public CallableStatement prepareCall(String var1, int var2, int var3) throws SQLException {
      return this.impl.prepareCall(var1, var2, var3);
   }

   @Override
   public CallableStatement prepareCall(String var1) throws SQLException {
      return this.impl.prepareCall(var1);
   }

   @Override
   public PreparedStatement prepareStatement(String var1, int var2, int var3, int var4) throws SQLException {
      return new SQLitePreparedStatement(this.impl.prepareStatement(var1, var2, var3, var4), this);
   }

   @Override
   public PreparedStatement prepareStatement(String var1, int var2, int var3) throws SQLException {
      return new SQLitePreparedStatement(this.impl.prepareStatement(var1, var2, var3), this);
   }

   @Override
   public PreparedStatement prepareStatement(String var1, int var2) throws SQLException {
      return new SQLitePreparedStatement(this.impl.prepareStatement(var1, var2), this);
   }

   @Override
   public PreparedStatement prepareStatement(String var1, int[] var2) throws SQLException {
      return new SQLitePreparedStatement(this.impl.prepareStatement(var1, var2), this);
   }

   @Override
   public PreparedStatement prepareStatement(String var1, String[] var2) throws SQLException {
      return new SQLitePreparedStatement(this.impl.prepareStatement(var1, var2), this);
   }

   @Override
   public PreparedStatement prepareStatement(String var1) throws SQLException {
      final String var2 = var1;
      final SQLiteConnection var3 = this;
      return (new Executor<PreparedStatement>(var3) {
         public PreparedStatement run() throws SQLException {
            return new SQLitePreparedStatement(SQLiteConnection.this.impl.prepareStatement(var2), var3);
         }
      }).executeWithRetry();
   }

   @Override
   public void releaseSavepoint(Savepoint var1) throws SQLException {
      this.impl.releaseSavepoint(var1);
   }

   @Override
   public void rollback() throws SQLException {
      this.impl.rollback();
   }

   @Override
   public void rollback(Savepoint var1) throws SQLException {
      this.impl.rollback(var1);
   }

   @Override
   public void setAutoCommit(boolean var1) throws SQLException {
      this.impl.setAutoCommit(var1);
   }

   @Override
   public void setCatalog(String var1) throws SQLException {
      this.impl.setCatalog(var1);
   }

   @Override
   public void setClientInfo(Properties var1) throws SQLClientInfoException {
      this.impl.setClientInfo(var1);
   }

   @Override
   public void setClientInfo(String var1, String var2) throws SQLClientInfoException {
      this.impl.setClientInfo(var1, var2);
   }

   @Override
   public void setHoldability(int var1) throws SQLException {
      this.impl.setHoldability(var1);
   }

   @Override
   public void setReadOnly(boolean var1) throws SQLException {
      this.impl.setReadOnly(var1);
   }

   @Override
   public Savepoint setSavepoint() throws SQLException {
      return this.impl.setSavepoint();
   }

   @Override
   public Savepoint setSavepoint(String var1) throws SQLException {
      return this.impl.setSavepoint(var1);
   }

   @Override
   public void setTransactionIsolation(int var1) throws SQLException {
   }

   @Override
   public void setTypeMap(Map<String, Class<?>> var1) throws SQLException {
      this.impl.setTypeMap(var1);
   }

   @Override
   public <T> T unwrap(Class<T> var1) throws SQLException {
      return this.impl.unwrap(var1);
   }

   @Override
   public void setSchema(String var1) throws SQLException {
   }

   @Override
   public String getSchema() throws SQLException {
      return null;
   }

   @Override
   public void abort(java.util.concurrent.Executor var1) throws SQLException {
   }

   @Override
   public void setNetworkTimeout(java.util.concurrent.Executor var1, int var2) throws SQLException {
   }

   @Override
   public int getNetworkTimeout() throws SQLException {
      return 0;
   }
}
