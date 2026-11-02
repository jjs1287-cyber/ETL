package org.jitterbit.integration.server.db.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

class SQLiteStatement implements Statement {
   private final Statement impl;
   private final Connection conn;

   public SQLiteStatement(Statement var1, Connection var2) {
      this.impl = var1;
      this.conn = var2;
   }

   @Override
   public void addBatch(String var1) throws SQLException {
      this.impl.addBatch(var1);
   }

   @Override
   public void cancel() throws SQLException {
      this.impl.cancel();
   }

   @Override
   public void clearBatch() throws SQLException {
      this.impl.clearBatch();
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
   public boolean execute(String var1, int var2) throws SQLException {
      return this.impl.execute(var1, var2);
   }

   @Override
   public boolean execute(String var1, int[] var2) throws SQLException {
      return this.impl.execute(var1, var2);
   }

   @Override
   public boolean execute(String var1, String[] var2) throws SQLException {
      return this.impl.execute(var1, var2);
   }

   @Override
   public boolean execute(final String var1) throws SQLException {
      return (new Executor<Boolean>(this.getConnection()) {
         public Boolean run() throws SQLException {
            return SQLiteStatement.this.impl.execute(var1);
         }
      }).executeWithRetry();
   }

   @Override
   public int[] executeBatch() throws SQLException {
      return (new Executor<int[]>(this.getConnection()) {
         public int[] run() throws SQLException {
            return SQLiteStatement.this.impl.executeBatch();
         }
      }).executeWithRetry();
   }

   @Override
   public ResultSet executeQuery(final String var1) throws SQLException {
      return (new Executor<ResultSet>(this.getConnection()) {
         public ResultSet run() throws SQLException {
            return SQLiteStatement.this.impl.executeQuery(var1);
         }
      }).executeWithRetry();
   }

   @Override
   public int executeUpdate(String var1, int var2) throws SQLException {
      return this.impl.executeUpdate(var1, var2);
   }

   @Override
   public int executeUpdate(String var1, int[] var2) throws SQLException {
      return this.impl.executeUpdate(var1, var2);
   }

   @Override
   public int executeUpdate(String var1, String[] var2) throws SQLException {
      return this.impl.executeUpdate(var1, var2);
   }

   @Override
   public int executeUpdate(final String var1) throws SQLException {
      return (new Executor<Integer>(this.getConnection()) {
         public Integer run() throws SQLException {
            return SQLiteStatement.this.impl.executeUpdate(var1);
         }
      }).executeWithRetry();
   }

   @Override
   public Connection getConnection() throws SQLException {
      return this.conn;
   }

   @Override
   public int getFetchDirection() throws SQLException {
      return this.impl.getFetchDirection();
   }

   @Override
   public int getFetchSize() throws SQLException {
      return this.impl.getFetchSize();
   }

   @Override
   public ResultSet getGeneratedKeys() throws SQLException {
      return this.impl.getGeneratedKeys();
   }

   @Override
   public int getMaxFieldSize() throws SQLException {
      return this.impl.getMaxFieldSize();
   }

   @Override
   public int getMaxRows() throws SQLException {
      return this.impl.getMaxRows();
   }

   @Override
   public boolean getMoreResults() throws SQLException {
      return this.impl.getMoreResults();
   }

   @Override
   public boolean getMoreResults(int var1) throws SQLException {
      return this.impl.getMoreResults(var1);
   }

   @Override
   public int getQueryTimeout() throws SQLException {
      return this.impl.getQueryTimeout();
   }

   @Override
   public ResultSet getResultSet() throws SQLException {
      return this.impl.getResultSet();
   }

   @Override
   public int getResultSetConcurrency() throws SQLException {
      return this.impl.getResultSetConcurrency();
   }

   @Override
   public int getResultSetHoldability() throws SQLException {
      return this.impl.getResultSetHoldability();
   }

   @Override
   public int getResultSetType() throws SQLException {
      return this.impl.getResultSetType();
   }

   @Override
   public int getUpdateCount() throws SQLException {
      return this.impl.getUpdateCount();
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
   public boolean isPoolable() throws SQLException {
      return this.impl.isPoolable();
   }

   @Override
   public boolean isWrapperFor(Class<?> var1) throws SQLException {
      return this.impl.isWrapperFor(var1);
   }

   @Override
   public void setCursorName(String var1) throws SQLException {
      this.impl.setCursorName(var1);
   }

   @Override
   public void setEscapeProcessing(boolean var1) throws SQLException {
      this.impl.setEscapeProcessing(var1);
   }

   @Override
   public void setFetchDirection(int var1) throws SQLException {
      this.impl.setFetchDirection(var1);
   }

   @Override
   public void setFetchSize(int var1) throws SQLException {
      this.impl.setFetchSize(var1);
   }

   @Override
   public void setMaxFieldSize(int var1) throws SQLException {
      this.impl.setMaxFieldSize(var1);
   }

   @Override
   public void setMaxRows(int var1) throws SQLException {
      this.impl.setMaxRows(var1);
   }

   @Override
   public void setPoolable(boolean var1) throws SQLException {
      this.impl.setPoolable(var1);
   }

   @Override
   public void setQueryTimeout(int var1) throws SQLException {
      this.impl.setQueryTimeout(var1);
   }

   @Override
   public <T> T unwrap(Class<T> var1) throws SQLException {
      return this.impl.unwrap(var1);
   }

   @Override
   public void closeOnCompletion() throws SQLException {
   }

   @Override
   public boolean isCloseOnCompletion() throws SQLException {
      return false;
   }
}
