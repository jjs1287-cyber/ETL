package org.jitterbit.integration.server.db.sqlite;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

class SQLitePreparedStatement implements PreparedStatement {
   private final PreparedStatement impl;
   private final Connection conn;

   public SQLitePreparedStatement(PreparedStatement var1, Connection var2) {
      this.impl = var1;
      this.conn = var2;
   }

   void throwMethodNotSupported(String var1) throws SQLException {
      throw new SQLException("The method " + var1 + " is not supported by this driver.");
   }

   @Override
   public void addBatch() throws SQLException {
      this.throwMethodNotSupported("addBatch()");
   }

   @Override
   public void addBatch(String var1) throws SQLException {
      this.throwMethodNotSupported("addBatch(String sql)");
   }

   @Override
   public void cancel() throws SQLException {
      this.impl.cancel();
   }

   @Override
   public void clearBatch() throws SQLException {
      this.throwMethodNotSupported("clearBatch()");
   }

   @Override
   public void clearParameters() throws SQLException {
      this.impl.clearParameters();
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
   public boolean execute() throws SQLException {
      return (new Executor<Boolean>(this.getConnection()) {
         public Boolean run() throws SQLException {
            return SQLitePreparedStatement.this.impl.execute();
         }
      }).executeWithRetry();
   }

   @Override
   public boolean execute(final String var1, final int var2) throws SQLException {
      return (new Executor<Boolean>(this.getConnection()) {
         public Boolean run() throws SQLException {
            return SQLitePreparedStatement.this.impl.execute(var1, var2);
         }
      }).executeWithRetry();
   }

   @Override
   public boolean execute(final String var1, final int[] var2) throws SQLException {
      return (new Executor<Boolean>(this.getConnection()) {
         public Boolean run() throws SQLException {
            return SQLitePreparedStatement.this.impl.execute(var1, var2);
         }
      }).executeWithRetry();
   }

   @Override
   public boolean execute(final String var1, final String[] var2) throws SQLException {
      return (new Executor<Boolean>(this.getConnection()) {
         public Boolean run() throws SQLException {
            return SQLitePreparedStatement.this.impl.execute(var1, var2);
         }
      }).executeWithRetry();
   }

   @Override
   public boolean execute(final String var1) throws SQLException {
      return (new Executor<Boolean>(this.getConnection()) {
         public Boolean run() throws SQLException {
            return SQLitePreparedStatement.this.impl.execute(var1);
         }
      }).executeWithRetry();
   }

   @Override
   public int[] executeBatch() throws SQLException {
      this.throwMethodNotSupported("executeBatch()");
      return new int[0];
   }

   @Override
   public ResultSet executeQuery() throws SQLException {
      return (new Executor<ResultSet>(this.getConnection()) {
         public ResultSet run() throws SQLException {
            return SQLitePreparedStatement.this.impl.executeQuery();
         }
      }).executeWithRetry();
   }

   @Override
   public ResultSet executeQuery(final String var1) throws SQLException {
      return (new Executor<ResultSet>(this.getConnection()) {
         public ResultSet run() throws SQLException {
            return SQLitePreparedStatement.this.impl.executeQuery(var1);
         }
      }).executeWithRetry();
   }

   @Override
   public int executeUpdate() throws SQLException {
      return (new Executor<Integer>(this.getConnection()) {
         public Integer run() throws SQLException {
            return SQLitePreparedStatement.this.impl.executeUpdate();
         }
      }).executeWithRetry();
   }

   @Override
   public int executeUpdate(final String var1, final int var2) throws SQLException {
      return (new Executor<Integer>(this.getConnection()) {
         public Integer run() throws SQLException {
            return SQLitePreparedStatement.this.impl.executeUpdate(var1, var2);
         }
      }).executeWithRetry();
   }

   @Override
   public int executeUpdate(final String var1, final int[] var2) throws SQLException {
      return (new Executor<Integer>(this.getConnection()) {
         public Integer run() throws SQLException {
            return SQLitePreparedStatement.this.impl.executeUpdate(var1, var2);
         }
      }).executeWithRetry();
   }

   @Override
   public int executeUpdate(final String var1, final String[] var2) throws SQLException {
      return (new Executor<Integer>(this.getConnection()) {
         public Integer run() throws SQLException {
            return SQLitePreparedStatement.this.impl.executeUpdate(var1, var2);
         }
      }).executeWithRetry();
   }

   @Override
   public int executeUpdate(final String var1) throws SQLException {
      return (new Executor<Integer>(this.getConnection()) {
         public Integer run() throws SQLException {
            return SQLitePreparedStatement.this.impl.executeUpdate(var1);
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
      return new SQLiteResultSet(this.impl.getGeneratedKeys(), this.conn);
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
   public ResultSetMetaData getMetaData() throws SQLException {
      return this.impl.getMetaData();
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
   public ParameterMetaData getParameterMetaData() throws SQLException {
      return this.impl.getParameterMetaData();
   }

   @Override
   public int getQueryTimeout() throws SQLException {
      return this.impl.getQueryTimeout();
   }

   @Override
   public ResultSet getResultSet() throws SQLException {
      return new SQLiteResultSet(this.impl.getResultSet(), this.conn);
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
   public void setArray(int var1, Array var2) throws SQLException {
      this.impl.setArray(var1, var2);
   }

   @Override
   public void setAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      this.impl.setAsciiStream(var1, var2, var3);
   }

   @Override
   public void setAsciiStream(int var1, InputStream var2, long var3) throws SQLException {
      this.impl.setAsciiStream(var1, var2, var3);
   }

   @Override
   public void setAsciiStream(int var1, InputStream var2) throws SQLException {
      this.impl.setAsciiStream(var1, var2);
   }

   @Override
   public void setBigDecimal(int var1, BigDecimal var2) throws SQLException {
      this.impl.setBigDecimal(var1, var2);
   }

   @Override
   public void setBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      this.impl.setBinaryStream(var1, var2, var3);
   }

   @Override
   public void setBinaryStream(int var1, InputStream var2, long var3) throws SQLException {
      this.impl.setBinaryStream(var1, var2, var3);
   }

   @Override
   public void setBinaryStream(int var1, InputStream var2) throws SQLException {
      this.impl.setBinaryStream(var1, var2);
   }

   @Override
   public void setBlob(int var1, Blob var2) throws SQLException {
      this.impl.setBlob(var1, var2);
   }

   @Override
   public void setBlob(int var1, InputStream var2, long var3) throws SQLException {
      this.impl.setBlob(var1, var2, var3);
   }

   @Override
   public void setBlob(int var1, InputStream var2) throws SQLException {
      this.impl.setBlob(var1, var2);
   }

   @Override
   public void setBoolean(int var1, boolean var2) throws SQLException {
      this.impl.setBoolean(var1, var2);
   }

   @Override
   public void setByte(int var1, byte var2) throws SQLException {
      this.impl.setByte(var1, var2);
   }

   @Override
   public void setBytes(int var1, byte[] var2) throws SQLException {
      this.impl.setBytes(var1, var2);
   }

   @Override
   public void setCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      this.impl.setCharacterStream(var1, var2, var3);
   }

   @Override
   public void setCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.impl.setCharacterStream(var1, var2, var3);
   }

   @Override
   public void setCharacterStream(int var1, Reader var2) throws SQLException {
      this.impl.setCharacterStream(var1, var2);
   }

   @Override
   public void setClob(int var1, Clob var2) throws SQLException {
      this.impl.setClob(var1, var2);
   }

   @Override
   public void setClob(int var1, Reader var2, long var3) throws SQLException {
      this.impl.setClob(var1, var2, var3);
   }

   @Override
   public void setClob(int var1, Reader var2) throws SQLException {
      this.impl.setClob(var1, var2);
   }

   @Override
   public void setCursorName(String var1) throws SQLException {
      this.impl.setCursorName(var1);
   }

   @Override
   public void setDate(int var1, Date var2, Calendar var3) throws SQLException {
      this.impl.setDate(var1, var2, var3);
   }

   @Override
   public void setDate(int var1, Date var2) throws SQLException {
      this.impl.setDate(var1, var2);
   }

   @Override
   public void setDouble(int var1, double var2) throws SQLException {
      this.impl.setDouble(var1, var2);
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
   public void setFloat(int var1, float var2) throws SQLException {
      this.impl.setFloat(var1, var2);
   }

   @Override
   public void setInt(int var1, int var2) throws SQLException {
      this.impl.setInt(var1, var2);
   }

   @Override
   public void setLong(int var1, long var2) throws SQLException {
      this.impl.setLong(var1, var2);
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
   public void setNCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.impl.setNCharacterStream(var1, var2, var3);
   }

   @Override
   public void setNCharacterStream(int var1, Reader var2) throws SQLException {
      this.impl.setNCharacterStream(var1, var2);
   }

   @Override
   public void setNClob(int var1, NClob var2) throws SQLException {
      this.impl.setNClob(var1, var2);
   }

   @Override
   public void setNClob(int var1, Reader var2, long var3) throws SQLException {
      this.impl.setNClob(var1, var2, var3);
   }

   @Override
   public void setNClob(int var1, Reader var2) throws SQLException {
      this.impl.setNClob(var1, var2);
   }

   @Override
   public void setNString(int var1, String var2) throws SQLException {
      this.impl.setNString(var1, var2);
   }

   @Override
   public void setNull(int var1, int var2, String var3) throws SQLException {
      this.impl.setNull(var1, var2, var3);
   }

   @Override
   public void setNull(int var1, int var2) throws SQLException {
      this.impl.setNull(var1, var2);
   }

   @Override
   public void setObject(int var1, Object var2, int var3, int var4) throws SQLException {
      this.impl.setObject(var1, var2, var3, var4);
   }

   @Override
   public void setObject(int var1, Object var2, int var3) throws SQLException {
      this.impl.setObject(var1, var2, var3);
   }

   @Override
   public void setObject(int var1, Object var2) throws SQLException {
      this.impl.setObject(var1, var2);
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
   public void setRef(int var1, Ref var2) throws SQLException {
      this.impl.setRef(var1, var2);
   }

   @Override
   public void setRowId(int var1, RowId var2) throws SQLException {
      this.impl.setRowId(var1, var2);
   }

   @Override
   public void setSQLXML(int var1, SQLXML var2) throws SQLException {
      this.impl.setSQLXML(var1, var2);
   }

   @Override
   public void setShort(int var1, short var2) throws SQLException {
      this.impl.setShort(var1, var2);
   }

   @Override
   public void setString(int var1, String var2) throws SQLException {
      this.impl.setString(var1, var2);
   }

   @Override
   public void setTime(int var1, Time var2, Calendar var3) throws SQLException {
      this.impl.setTime(var1, var2, var3);
   }

   @Override
   public void setTime(int var1, Time var2) throws SQLException {
      this.impl.setTime(var1, var2);
   }

   @Override
   public void setTimestamp(int var1, Timestamp var2, Calendar var3) throws SQLException {
      this.impl.setTimestamp(var1, var2, var3);
   }

   @Override
   public void setTimestamp(int var1, Timestamp var2) throws SQLException {
      this.impl.setTimestamp(var1, var2);
   }

   @Override
   public void setURL(int var1, URL var2) throws SQLException {
      this.impl.setURL(var1, var2);
   }

   @Override
   public void setUnicodeStream(int var1, InputStream var2, int var3) throws SQLException {
      this.impl.setUnicodeStream(var1, var2, var3);
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
