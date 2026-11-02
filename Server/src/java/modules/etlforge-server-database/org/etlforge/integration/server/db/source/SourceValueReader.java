package org.etlforge.integration.server.db.source;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.lang.KongaStringUtils;

public final class SourceValueReader {
   private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
   private final DateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

   public String readColumnValueAsString(ResultSet var1, int var2) throws SQLException {
      String var3 = null;
      int var4 = var1.getMetaData().getColumnType(var2);
      switch (var4) {
         case -7:
            var3 = var1.getBoolean(var2) ? "1" : "0";
            break;
         case -4:
         case 2004:
            var3 = toHexString(var1, var2);
            break;
         case -3:
         case -2:
            byte[] var5 = var1.getBytes(var2);
            var3 = toHexString(var5);
            break;
         case 9:
         case 91:
            Date var6 = var1.getDate(var2);
            if (var6 != null) {
               var3 = this.dateFormat.format(var6);
            }
            break;
         case 16:
            var3 = Boolean.toString(var1.getBoolean(var2));
            break;
         case 93:
            Timestamp var7 = var1.getTimestamp(var2);
            if (var7 != null) {
               var3 = this.timestampFormat.format(var7);
            }
            break;
         default:
            var3 = var1.getString(var2);
      }

      if (var1.wasNull()) {
         var3 = null;
      }

      return var3;
   }

   private static String toHexString(byte[] var0) {
      return var0 != null ? KongaStringUtils.toHexString(var0) : null;
   }

   private static String toHexString(ResultSet var0, int var1) throws SQLException {
      InputStream var2 = var0.getBinaryStream(var1);

      try {
         return KongaIoUtils.inputStreamToHexString(var2);
      } catch (IOException var7) {
         throw new SQLException("An I/O error occurred while reading binary data from the database. The error message was: " + var7.getMessage(), var7);
      } finally {
         KongaIoUtils.close(var2);
      }
   }
}
