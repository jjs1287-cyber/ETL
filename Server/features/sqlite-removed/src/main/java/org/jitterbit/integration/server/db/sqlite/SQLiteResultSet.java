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
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

class SQLiteResultSet implements ResultSet {
   private final ResultSet impl;
   private final Connection conn;

   SQLiteResultSet(ResultSet var1, Connection var2) {
      this.impl = var1;
      this.conn = var2;
   }

   @Override
   public boolean absolute(int var1) throws SQLException {
      return this.impl.absolute(var1);
   }

   @Override
   public void afterLast() throws SQLException {
      this.impl.afterLast();
   }

   @Override
   public void beforeFirst() throws SQLException {
      this.impl.beforeFirst();
   }

   @Override
   public void cancelRowUpdates() throws SQLException {
      this.impl.cancelRowUpdates();
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
   public void deleteRow() throws SQLException {
      this.impl.deleteRow();
   }

   @Override
   public int findColumn(String var1) throws SQLException {
      return this.impl.findColumn(var1);
   }

   @Override
   public boolean first() throws SQLException {
      return this.impl.first();
   }

   @Override
   public Array getArray(int var1) throws SQLException {
      return this.impl.getArray(var1);
   }

   @Override
   public Array getArray(String var1) throws SQLException {
      return this.impl.getArray(var1);
   }

   @Override
   public InputStream getAsciiStream(int var1) throws SQLException {
      return this.impl.getAsciiStream(var1);
   }

   @Override
   public InputStream getAsciiStream(String var1) throws SQLException {
      return this.impl.getAsciiStream(var1);
   }

   @Override
   public BigDecimal getBigDecimal(int var1, int var2) throws SQLException {
      return this.impl.getBigDecimal(var1, var2);
   }

   @Override
   public BigDecimal getBigDecimal(int var1) throws SQLException {
      return this.impl.getBigDecimal(var1);
   }

   @Override
   public BigDecimal getBigDecimal(String var1, int var2) throws SQLException {
      return this.impl.getBigDecimal(var1, var2);
   }

   @Override
   public BigDecimal getBigDecimal(String var1) throws SQLException {
      return this.impl.getBigDecimal(var1);
   }

   @Override
   public InputStream getBinaryStream(int var1) throws SQLException {
      return this.impl.getBinaryStream(var1);
   }

   @Override
   public InputStream getBinaryStream(String var1) throws SQLException {
      return this.impl.getBinaryStream(var1);
   }

   @Override
   public Blob getBlob(int var1) throws SQLException {
      return this.impl.getBlob(var1);
   }

   @Override
   public Blob getBlob(String var1) throws SQLException {
      return this.impl.getBlob(var1);
   }

   @Override
   public boolean getBoolean(int var1) throws SQLException {
      return this.impl.getBoolean(var1);
   }

   @Override
   public boolean getBoolean(String var1) throws SQLException {
      return this.impl.getBoolean(var1);
   }

   @Override
   public byte getByte(int var1) throws SQLException {
      return this.impl.getByte(var1);
   }

   @Override
   public byte getByte(String var1) throws SQLException {
      return this.impl.getByte(var1);
   }

   @Override
   public byte[] getBytes(int var1) throws SQLException {
      return this.impl.getBytes(var1);
   }

   @Override
   public byte[] getBytes(String var1) throws SQLException {
      return this.impl.getBytes(var1);
   }

   @Override
   public Reader getCharacterStream(int var1) throws SQLException {
      return this.impl.getCharacterStream(var1);
   }

   @Override
   public Reader getCharacterStream(String var1) throws SQLException {
      return this.impl.getCharacterStream(var1);
   }

   @Override
   public Clob getClob(int var1) throws SQLException {
      return this.impl.getClob(var1);
   }

   @Override
   public Clob getClob(String var1) throws SQLException {
      return this.impl.getClob(var1);
   }

   @Override
   public int getConcurrency() throws SQLException {
      return this.impl.getConcurrency();
   }

   @Override
   public String getCursorName() throws SQLException {
      return this.impl.getCursorName();
   }

   @Override
   public Date getDate(int var1, Calendar var2) throws SQLException {
      return this.impl.getDate(var1, var2);
   }

   @Override
   public Date getDate(int var1) throws SQLException {
      return this.impl.getDate(var1);
   }

   @Override
   public Date getDate(String var1, Calendar var2) throws SQLException {
      return this.impl.getDate(var1, var2);
   }

   @Override
   public Date getDate(String var1) throws SQLException {
      return this.impl.getDate(var1);
   }

   @Override
   public double getDouble(int var1) throws SQLException {
      return this.impl.getDouble(var1);
   }

   @Override
   public double getDouble(String var1) throws SQLException {
      return this.impl.getDouble(var1);
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
   public float getFloat(int var1) throws SQLException {
      return this.impl.getFloat(var1);
   }

   @Override
   public float getFloat(String var1) throws SQLException {
      return this.impl.getFloat(var1);
   }

   @Override
   public int getHoldability() throws SQLException {
      return this.impl.getHoldability();
   }

   @Override
   public int getInt(int var1) throws SQLException {
      return this.impl.getInt(var1);
   }

   @Override
   public int getInt(String var1) throws SQLException {
      return this.impl.getInt(var1);
   }

   @Override
   public long getLong(int var1) throws SQLException {
      return this.impl.getLong(var1);
   }

   @Override
   public long getLong(String var1) throws SQLException {
      return this.impl.getLong(var1);
   }

   @Override
   public ResultSetMetaData getMetaData() throws SQLException {
      return this.impl.getMetaData();
   }

   @Override
   public Reader getNCharacterStream(int var1) throws SQLException {
      return this.impl.getNCharacterStream(var1);
   }

   @Override
   public Reader getNCharacterStream(String var1) throws SQLException {
      return this.impl.getNCharacterStream(var1);
   }

   @Override
   public NClob getNClob(int var1) throws SQLException {
      return this.impl.getNClob(var1);
   }

   @Override
   public NClob getNClob(String var1) throws SQLException {
      return this.impl.getNClob(var1);
   }

   @Override
   public String getNString(int var1) throws SQLException {
      return this.impl.getNString(var1);
   }

   @Override
   public String getNString(String var1) throws SQLException {
      return this.impl.getNString(var1);
   }

   @Override
   public Object getObject(int var1, Map<String, Class<?>> var2) throws SQLException {
      return this.impl.getObject(var1, var2);
   }

   @Override
   public Object getObject(int var1) throws SQLException {
      return this.impl.getObject(var1);
   }

   @Override
   public Object getObject(String var1, Map<String, Class<?>> var2) throws SQLException {
      return this.impl.getObject(var1, var2);
   }

   @Override
   public Object getObject(String var1) throws SQLException {
      return this.impl.getObject(var1);
   }

   @Override
   public Ref getRef(int var1) throws SQLException {
      return this.impl.getRef(var1);
   }

   @Override
   public Ref getRef(String var1) throws SQLException {
      return this.impl.getRef(var1);
   }

   @Override
   public int getRow() throws SQLException {
      return this.impl.getRow();
   }

   @Override
   public RowId getRowId(int var1) throws SQLException {
      return this.impl.getRowId(var1);
   }

   @Override
   public RowId getRowId(String var1) throws SQLException {
      return this.impl.getRowId(var1);
   }

   @Override
   public SQLXML getSQLXML(int var1) throws SQLException {
      return this.impl.getSQLXML(var1);
   }

   @Override
   public SQLXML getSQLXML(String var1) throws SQLException {
      return this.impl.getSQLXML(var1);
   }

   @Override
   public short getShort(int var1) throws SQLException {
      return this.impl.getShort(var1);
   }

   @Override
   public short getShort(String var1) throws SQLException {
      return this.impl.getShort(var1);
   }

   @Override
   public Statement getStatement() throws SQLException {
      return this.impl.getStatement();
   }

   @Override
   public String getString(int var1) throws SQLException {
      return this.impl.getString(var1);
   }

   @Override
   public String getString(String var1) throws SQLException {
      return this.impl.getString(var1);
   }

   @Override
   public Time getTime(int var1, Calendar var2) throws SQLException {
      return this.impl.getTime(var1, var2);
   }

   @Override
   public Time getTime(int var1) throws SQLException {
      return this.impl.getTime(var1);
   }

   @Override
   public Time getTime(String var1, Calendar var2) throws SQLException {
      return this.impl.getTime(var1, var2);
   }

   @Override
   public Time getTime(String var1) throws SQLException {
      return this.impl.getTime(var1);
   }

   @Override
   public Timestamp getTimestamp(int var1, Calendar var2) throws SQLException {
      return this.impl.getTimestamp(var1, var2);
   }

   @Override
   public Timestamp getTimestamp(int var1) throws SQLException {
      return this.impl.getTimestamp(var1);
   }

   @Override
   public Timestamp getTimestamp(String var1, Calendar var2) throws SQLException {
      return this.impl.getTimestamp(var1, var2);
   }

   @Override
   public Timestamp getTimestamp(String var1) throws SQLException {
      return this.impl.getTimestamp(var1);
   }

   @Override
   public int getType() throws SQLException {
      return this.impl.getType();
   }

   @Override
   public URL getURL(int var1) throws SQLException {
      return this.impl.getURL(var1);
   }

   @Override
   public URL getURL(String var1) throws SQLException {
      return this.impl.getURL(var1);
   }

   @Override
   public InputStream getUnicodeStream(int var1) throws SQLException {
      return this.impl.getUnicodeStream(var1);
   }

   @Override
   public InputStream getUnicodeStream(String var1) throws SQLException {
      return this.impl.getUnicodeStream(var1);
   }

   @Override
   public SQLWarning getWarnings() throws SQLException {
      return this.impl.getWarnings();
   }

   @Override
   public void insertRow() throws SQLException {
      this.impl.insertRow();
   }

   @Override
   public boolean isAfterLast() throws SQLException {
      return this.impl.isAfterLast();
   }

   @Override
   public boolean isBeforeFirst() throws SQLException {
      return this.impl.isBeforeFirst();
   }

   @Override
   public boolean isClosed() throws SQLException {
      return this.impl.isClosed();
   }

   @Override
   public boolean isFirst() throws SQLException {
      return this.impl.isFirst();
   }

   @Override
   public boolean isLast() throws SQLException {
      return this.impl.isLast();
   }

   @Override
   public boolean isWrapperFor(Class<?> var1) throws SQLException {
      return this.impl.isWrapperFor(var1);
   }

   @Override
   public boolean last() throws SQLException {
      return this.impl.last();
   }

   @Override
   public void moveToCurrentRow() throws SQLException {
      this.impl.moveToCurrentRow();
   }

   @Override
   public void moveToInsertRow() throws SQLException {
      this.impl.moveToInsertRow();
   }

   @Override
   public boolean next() throws SQLException {
      return (new Executor<Boolean>(this.conn) {
         Boolean run() throws SQLException {
            return SQLiteResultSet.this.impl.next();
         }
      }).executeWithRetry();
   }

   @Override
   public boolean previous() throws SQLException {
      return this.impl.previous();
   }

   @Override
   public void refreshRow() throws SQLException {
      this.impl.refreshRow();
   }

   @Override
   public boolean relative(int var1) throws SQLException {
      return this.impl.relative(var1);
   }

   @Override
   public boolean rowDeleted() throws SQLException {
      return this.impl.rowDeleted();
   }

   @Override
   public boolean rowInserted() throws SQLException {
      return this.impl.rowInserted();
   }

   @Override
   public boolean rowUpdated() throws SQLException {
      return this.impl.rowUpdated();
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
   public <T> T unwrap(Class<T> var1) throws SQLException {
      return this.impl.unwrap(var1);
   }

   @Override
   public void updateArray(int var1, Array var2) throws SQLException {
      this.impl.updateArray(var1, var2);
   }

   @Override
   public void updateArray(String var1, Array var2) throws SQLException {
      this.impl.updateArray(var1, var2);
   }

   @Override
   public void updateAsciiStream(int var1, InputStream var2, int var3) throws SQLException {
      this.impl.updateAsciiStream(var1, var2, var3);
   }

   @Override
   public void updateAsciiStream(int var1, InputStream var2, long var3) throws SQLException {
      this.impl.updateAsciiStream(var1, var2, var3);
   }

   @Override
   public void updateAsciiStream(int var1, InputStream var2) throws SQLException {
      this.impl.updateAsciiStream(var1, var2);
   }

   @Override
   public void updateAsciiStream(String var1, InputStream var2, int var3) throws SQLException {
      this.impl.updateAsciiStream(var1, var2, var3);
   }

   @Override
   public void updateAsciiStream(String var1, InputStream var2, long var3) throws SQLException {
      this.impl.updateAsciiStream(var1, var2, var3);
   }

   @Override
   public void updateAsciiStream(String var1, InputStream var2) throws SQLException {
      this.impl.updateAsciiStream(var1, var2);
   }

   @Override
   public void updateBigDecimal(int var1, BigDecimal var2) throws SQLException {
      this.impl.updateBigDecimal(var1, var2);
   }

   @Override
   public void updateBigDecimal(String var1, BigDecimal var2) throws SQLException {
      this.impl.updateBigDecimal(var1, var2);
   }

   @Override
   public void updateBinaryStream(int var1, InputStream var2, int var3) throws SQLException {
      this.impl.updateBinaryStream(var1, var2, var3);
   }

   @Override
   public void updateBinaryStream(int var1, InputStream var2, long var3) throws SQLException {
      this.impl.updateBinaryStream(var1, var2, var3);
   }

   @Override
   public void updateBinaryStream(int var1, InputStream var2) throws SQLException {
      this.impl.updateBinaryStream(var1, var2);
   }

   @Override
   public void updateBinaryStream(String var1, InputStream var2, int var3) throws SQLException {
      this.impl.updateBinaryStream(var1, var2, var3);
   }

   @Override
   public void updateBinaryStream(String var1, InputStream var2, long var3) throws SQLException {
      this.impl.updateBinaryStream(var1, var2, var3);
   }

   @Override
   public void updateBinaryStream(String var1, InputStream var2) throws SQLException {
      this.impl.updateBinaryStream(var1, var2);
   }

   @Override
   public void updateBlob(int var1, Blob var2) throws SQLException {
      this.impl.updateBlob(var1, var2);
   }

   @Override
   public void updateBlob(int var1, InputStream var2, long var3) throws SQLException {
      this.impl.updateBlob(var1, var2, var3);
   }

   @Override
   public void updateBlob(int var1, InputStream var2) throws SQLException {
      this.impl.updateBlob(var1, var2);
   }

   @Override
   public void updateBlob(String var1, Blob var2) throws SQLException {
      this.impl.updateBlob(var1, var2);
   }

   @Override
   public void updateBlob(String var1, InputStream var2, long var3) throws SQLException {
      this.impl.updateBlob(var1, var2, var3);
   }

   @Override
   public void updateBlob(String var1, InputStream var2) throws SQLException {
      this.impl.updateBlob(var1, var2);
   }

   @Override
   public void updateBoolean(int var1, boolean var2) throws SQLException {
      this.impl.updateBoolean(var1, var2);
   }

   @Override
   public void updateBoolean(String var1, boolean var2) throws SQLException {
      this.impl.updateBoolean(var1, var2);
   }

   @Override
   public void updateByte(int var1, byte var2) throws SQLException {
      this.impl.updateByte(var1, var2);
   }

   @Override
   public void updateByte(String var1, byte var2) throws SQLException {
      this.impl.updateByte(var1, var2);
   }

   @Override
   public void updateBytes(int var1, byte[] var2) throws SQLException {
      this.impl.updateBytes(var1, var2);
   }

   @Override
   public void updateBytes(String var1, byte[] var2) throws SQLException {
      this.impl.updateBytes(var1, var2);
   }

   @Override
   public void updateCharacterStream(int var1, Reader var2, int var3) throws SQLException {
      this.impl.updateCharacterStream(var1, var2, var3);
   }

   @Override
   public void updateCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.impl.updateCharacterStream(var1, var2, var3);
   }

   @Override
   public void updateCharacterStream(int var1, Reader var2) throws SQLException {
      this.impl.updateCharacterStream(var1, var2);
   }

   @Override
   public void updateCharacterStream(String var1, Reader var2, int var3) throws SQLException {
      this.impl.updateCharacterStream(var1, var2, var3);
   }

   @Override
   public void updateCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.impl.updateCharacterStream(var1, var2, var3);
   }

   @Override
   public void updateCharacterStream(String var1, Reader var2) throws SQLException {
      this.impl.updateCharacterStream(var1, var2);
   }

   @Override
   public void updateClob(int var1, Clob var2) throws SQLException {
      this.impl.updateClob(var1, var2);
   }

   @Override
   public void updateClob(int var1, Reader var2, long var3) throws SQLException {
      this.impl.updateClob(var1, var2, var3);
   }

   @Override
   public void updateClob(int var1, Reader var2) throws SQLException {
      this.impl.updateClob(var1, var2);
   }

   @Override
   public void updateClob(String var1, Clob var2) throws SQLException {
      this.impl.updateClob(var1, var2);
   }

   @Override
   public void updateClob(String var1, Reader var2, long var3) throws SQLException {
      this.impl.updateClob(var1, var2, var3);
   }

   @Override
   public void updateClob(String var1, Reader var2) throws SQLException {
      this.impl.updateClob(var1, var2);
   }

   @Override
   public void updateDate(int var1, Date var2) throws SQLException {
      this.impl.updateDate(var1, var2);
   }

   @Override
   public void updateDate(String var1, Date var2) throws SQLException {
      this.impl.updateDate(var1, var2);
   }

   @Override
   public void updateDouble(int var1, double var2) throws SQLException {
      this.impl.updateDouble(var1, var2);
   }

   @Override
   public void updateDouble(String var1, double var2) throws SQLException {
      this.impl.updateDouble(var1, var2);
   }

   @Override
   public void updateFloat(int var1, float var2) throws SQLException {
      this.impl.updateFloat(var1, var2);
   }

   @Override
   public void updateFloat(String var1, float var2) throws SQLException {
      this.impl.updateFloat(var1, var2);
   }

   @Override
   public void updateInt(int var1, int var2) throws SQLException {
      this.impl.updateInt(var1, var2);
   }

   @Override
   public void updateInt(String var1, int var2) throws SQLException {
      this.impl.updateInt(var1, var2);
   }

   @Override
   public void updateLong(int var1, long var2) throws SQLException {
      this.impl.updateLong(var1, var2);
   }

   @Override
   public void updateLong(String var1, long var2) throws SQLException {
      this.impl.updateLong(var1, var2);
   }

   @Override
   public void updateNCharacterStream(int var1, Reader var2, long var3) throws SQLException {
      this.impl.updateNCharacterStream(var1, var2, var3);
   }

   @Override
   public void updateNCharacterStream(int var1, Reader var2) throws SQLException {
      this.impl.updateNCharacterStream(var1, var2);
   }

   @Override
   public void updateNCharacterStream(String var1, Reader var2, long var3) throws SQLException {
      this.impl.updateNCharacterStream(var1, var2, var3);
   }

   @Override
   public void updateNCharacterStream(String var1, Reader var2) throws SQLException {
      this.impl.updateNCharacterStream(var1, var2);
   }

   @Override
   public void updateNClob(int var1, NClob var2) throws SQLException {
      this.impl.updateNClob(var1, var2);
   }

   @Override
   public void updateNClob(int var1, Reader var2, long var3) throws SQLException {
      this.impl.updateNClob(var1, var2, var3);
   }

   @Override
   public void updateNClob(int var1, Reader var2) throws SQLException {
      this.impl.updateNClob(var1, var2);
   }

   @Override
   public void updateNClob(String var1, NClob var2) throws SQLException {
      this.impl.updateNClob(var1, var2);
   }

   @Override
   public void updateNClob(String var1, Reader var2, long var3) throws SQLException {
      this.impl.updateNClob(var1, var2, var3);
   }

   @Override
   public void updateNClob(String var1, Reader var2) throws SQLException {
      this.impl.updateNClob(var1, var2);
   }

   @Override
   public void updateNString(int var1, String var2) throws SQLException {
      this.impl.updateNString(var1, var2);
   }

   @Override
   public void updateNString(String var1, String var2) throws SQLException {
      this.impl.updateNString(var1, var2);
   }

   @Override
   public void updateNull(int var1) throws SQLException {
      this.impl.updateNull(var1);
   }

   @Override
   public void updateNull(String var1) throws SQLException {
      this.impl.updateNull(var1);
   }

   @Override
   public void updateObject(int var1, Object var2, int var3) throws SQLException {
      this.impl.updateObject(var1, var2, var3);
   }

   @Override
   public void updateObject(int var1, Object var2) throws SQLException {
      this.impl.updateObject(var1, var2);
   }

   @Override
   public void updateObject(String var1, Object var2, int var3) throws SQLException {
      this.impl.updateObject(var1, var2, var3);
   }

   @Override
   public void updateObject(String var1, Object var2) throws SQLException {
      this.impl.updateObject(var1, var2);
   }

   @Override
   public void updateRef(int var1, Ref var2) throws SQLException {
      this.impl.updateRef(var1, var2);
   }

   @Override
   public void updateRef(String var1, Ref var2) throws SQLException {
      this.impl.updateRef(var1, var2);
   }

   @Override
   public void updateRow() throws SQLException {
      this.impl.updateRow();
   }

   @Override
   public void updateRowId(int var1, RowId var2) throws SQLException {
      this.impl.updateRowId(var1, var2);
   }

   @Override
   public void updateRowId(String var1, RowId var2) throws SQLException {
      this.impl.updateRowId(var1, var2);
   }

   @Override
   public void updateSQLXML(int var1, SQLXML var2) throws SQLException {
      this.impl.updateSQLXML(var1, var2);
   }

   @Override
   public void updateSQLXML(String var1, SQLXML var2) throws SQLException {
      this.impl.updateSQLXML(var1, var2);
   }

   @Override
   public void updateShort(int var1, short var2) throws SQLException {
      this.impl.updateShort(var1, var2);
   }

   @Override
   public void updateShort(String var1, short var2) throws SQLException {
      this.impl.updateShort(var1, var2);
   }

   @Override
   public void updateString(int var1, String var2) throws SQLException {
      this.impl.updateString(var1, var2);
   }

   @Override
   public void updateString(String var1, String var2) throws SQLException {
      this.impl.updateString(var1, var2);
   }

   @Override
   public void updateTime(int var1, Time var2) throws SQLException {
      this.impl.updateTime(var1, var2);
   }

   @Override
   public void updateTime(String var1, Time var2) throws SQLException {
      this.impl.updateTime(var1, var2);
   }

   @Override
   public void updateTimestamp(int var1, Timestamp var2) throws SQLException {
      this.impl.updateTimestamp(var1, var2);
   }

   @Override
   public void updateTimestamp(String var1, Timestamp var2) throws SQLException {
      this.impl.updateTimestamp(var1, var2);
   }

   @Override
   public boolean wasNull() throws SQLException {
      return this.impl.wasNull();
   }

   @Override
   public <T> T getObject(int var1, Class<T> var2) throws SQLException {
      return null;
   }

   @Override
   public <T> T getObject(String var1, Class<T> var2) throws SQLException {
      return null;
   }
}
