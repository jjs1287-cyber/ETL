package org.etlforge.integration.server.db.utils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.Collections2;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.server.BackendEncoding;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.integration.structure.crom.CROM_DB;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.CromTreeException;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureFactory;
import org.etlforge.io.KongaIoUtils;

public final class DbUtils {
   private static final String sqlEscapeChars = "'\\";
   public static final int SQL_TIME = 10;
   public static final int SQL_TIMESTAMP = 11;
   public static final int SQL_ODBC_DATE = 9;
   private static final int ODBC_DATE = 9;
   private static final Function<CROM, String> CROM_TO_VALUE = new Function<CROM, String>() {
      public String apply(CROM var1) {
         String var2 = var1.getValue();
         if (var2 == null) {
            return "(null)";
         } else {
            return var2.length() > 128 ? var2.substring(0, 128) + "..." : var2;
         }
      }
   };

   public static CROM_Tree createCromTree(String var0) {
      File var1 = new File(var0);
      MappingTreeStructure var2 = MappingTreeStructureFactory.fromFile(var1, BackendEncoding.getCharsetForReading());

      CROM_Tree var3;
      try {
         var3 = CROM_Tree.parse(var2);
      } catch (CromTreeException var6) {
         String var5 = getJtrParsingErrorMessage(var0, var6);
         throw new RuntimeException(var5, var6);
      }

      if (var3.getRoot() == null) {
         String var4 = getJtrParsingErrorMessage(var0, null);
         throw new RuntimeException(var4);
      } else {
         translateFromOdbcDateType(var3.getRoot());
         return var3;
      }
   }

   private static void translateFromOdbcDateType(CROM var0) {
      if (var0.getSqlDataType() == 9) {
         var0.setSqlDataType(91);
      }

      for (CROM var2 : var0.getChildren()) {
         translateFromOdbcDateType(var2);
      }
   }

   private static final String getJtrParsingErrorMessage(String var0, Exception var1) {
      String var2 = var1 != null ? ": " + var1.getMessage() + "\n" : ". ";
      return MessageFormat.format(
         "There seems to be a problem with the file that describes the table structure{0}The path to the file we tried to parse is {1}", var2, var0
      );
   }

   private static boolean isSQLServerSubprotocol(String var0) {
      return var0.startsWith("SQLSERVER") || var0.startsWith("SQL SERVER") || var0.startsWith("JTDS");
   }

   public static String getCurrentSequenceSql(String var0, String var1, String var2) {
      String var3 = var0.toUpperCase();
      if (isSQLServerSubprotocol(var3)) {
         return "SELECT @@IDENTITY";
      }

      if (var3.startsWith("MYSQL")) {
         return "SELECT last_insert_id()";
      }

      if (var3.startsWith("DB2")) {
         return "select identity_val_local()";
      }

      if (var3.startsWith("SYBASE")) {
         return "SELECT @@IDENTITY";
      }

      int var4 = var1.length();
      assert var4 > 3;
      String var5 = var1.substring(0, 1);
      String var6 = var1.substring(var4 - 1);
      String var7 = "";
      String var8 = "";
      StringTokenizer var9 = new StringTokenizer(var1, var5 + var6 + ".", true);

      while (var9.hasMoreTokens()) {
         String var10 = var9.nextToken();
         if (var10.equals(var5)) {
            StringBuilder var11 = new StringBuilder();

            while (var9.hasMoreTokens()) {
               String var12 = var9.nextToken();
               if (var12.equals(var6)) {
                  break;
               }

               var11.append(var12);
            }

            var10 = var11.toString();
            assert var8.isEmpty() || var7.isEmpty();
            if (!var8.isEmpty()) {
               var7 = var8;
            }

            var8 = var10;
         }
      }

      if (var3.startsWith("POSTGRESQL")) {
         if (var7.isEmpty()) {
            var7 = "public";
         }

         return String.format("SELECT currval('\"%s\".\"%s_%s_seq\"');", var7, var8, var2);
      } else {
         if (var3.startsWith("ORACLE")) {
            return String.format("select \"%s_SEQ\".currval from dual", var8);
         }

         if (!var7.isEmpty()) {
            var7 = var5 + var7 + var6 + ".";
         }

         return String.format("SELECT MAX(%s%s%s) FROM %s%s%s%s", var5, var2, var6, var7, var5, var8, var6);
      }
   }

   public static boolean csvRequireEscape(String var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.startsWith("\"") ? true : var0.indexOf(44) >= 0 || var0.indexOf(10) >= 0 || var0.indexOf(13) >= 0;
      }
   }

   public static String csvEscape(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("\"");
      int var2 = 0;
      int var3 = 0;

      while ((var3 = var0.indexOf(34, var2)) >= 0) {
         var1.append(var0.substring(var2, var3 + 1)).append("\"");
         var2 = var3 + 1;
      }

      if (var2 < var0.length()) {
         var1.append(var0.substring(var2));
      }

      var1.append("\"");
      return var1.toString();
   }

   public static void writeStructFile(Writer var0, CROM var1, boolean var2) throws IOException {
      String var3 = var1.getDisplayName();
      if (var3 != null && !var3.isEmpty()) {
         boolean var4 = false;
         if (!var2) {
            var0.write(var3);
            var4 = true;
         }

         for (CROM var6 : var1.getChildren()) {
            if (var6.isElementNode()) {
               break;
            }

            if (var6.isAttributeNode()) {
               if (var4) {
                  var0.write(",");
               } else {
                  var4 = true;
               }

               String var7 = var6.getDisplayName();
               if (csvRequireEscape(var7)) {
                  var0.write(csvEscape(var7));
               } else {
                  var0.write(var7);
               }
            }
         }
      }

      for (CROM var9 : var1.getChildren()) {
         if (var9.isElementNode()) {
            writeStructFile(var0, var9, var2);
         }
      }
   }

   public static CROM findTable(String var0, CROM var1) throws DbUtilsException {
      String var2 = var1.getName();
      if (var0.equals(var2)) {
         return var1;
      }

      for (CROM var4 : var1.getChildren()) {
         if (var4.isElementNode() && var0.equals(var4.getName())) {
            return var4;
         }
      }

      for (CROM var6 = var1.getParent(); var6 != null; var6 = var6.getParent()) {
         if (var0.equals(var6.getName())) {
            return var6;
         }

         for (CROM var5 : var6.getChildren()) {
            if (var5.isElementNode() && var0.equals(var5.getName())) {
               return var5;
            }
         }
      }

      throw new DbUtilsException("findTable failed, cannot find table from \"" + var2 + "\" for id=\"" + var0 + "\"");
   }

   public static boolean requireSqlEscape(String var0) {
      for (char var4 : var0.toCharArray()) {
         if ("'\\".indexOf(var4) >= 0) {
            return true;
         }
      }

      return false;
   }

   public static String sqlEscape(String var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append('\'');
      int var2 = 0;
      int var3 = var0.length();

      for (int var4 = 0; var4 < var3; var4++) {
         char var5 = var0.charAt(var4);
         if ("'\\".indexOf(var5) >= 0) {
            var1.append(var0.substring(var2, var4 + 1)).append(var5);
            var2 = var4 + 1;
         }
      }

      if (var2 < var3) {
         var1.append(var0.substring(var2, var3));
      }

      var1.append('\'');
      return var1.toString();
   }

   public static BeginEndQuote extractBeginAndEndQuoteFromJTR(CROM var0) {
      CROM var1 = var0.getChild(0);
      assert var1.isElementNode();
      String var2 = var1.getSqlTableName();
      if (StringUtils.isEmpty(var2)) {
         String var3 = ((CROM_DB)var1.getExtension()).getManualSqlStatement();
         if (StringUtils.isEmpty(var3)) {
            throw new RuntimeException(
               "ETLForge has created inconsistent information about the database structure. Neither a table name, nor a manual SQL statement has been defined. This is a bug. Please report it to ETLForge Support."
            );
         } else {
            return BeginEndQuote.BLANK;
         }
      } else {
         return new BeginEndQuote(var2.substring(0, 1), var2.substring(var2.length() - 1));
      }
   }

   public static Iterable<CROM> getTableColumns(CROM var0) {
      ArrayList var1 = Lists.newArrayList();

      for (CROM var3 : var0.getChildren()) {
         if (var3.isElementNode()) {
            break;
         }

         if (var3.isAttributeNode() && !var3.isPrecondition()) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public static void bindParameter(PreparedStatement var0, int var1, CROM var2, String var3) throws SQLException {
      int var4 = var2.getSqlDataType();
      if (shouldBeBoundToNull(var3, var4)) {
         var0.setNull(var1, var4);
      } else {
         switch (var4) {
            case -7:
               boolean var14 = "1".equals(var3) || "true".equalsIgnoreCase(var3);
               var0.setBoolean(var1, var14);
               break;
            case -6:
            case 5:
               var0.setShort(var1, Short.parseShort(var3));
               break;
            case -5:
               var0.setLong(var1, Long.parseLong(var3));
               break;
            case -4:
            case 2004:
               byte[] var13 = fromHexString(var3);
               ByteArrayInputStream var15 = new ByteArrayInputStream(var13);
               var0.setBinaryStream(var1, var15, var13.length);
               KongaIoUtils.close(var15);
               break;
            case -3:
            case -2:
               byte[] var12 = fromHexString(var3);
               var0.setBytes(var1, var12);
               break;
            case 2:
            case 3:
               var0.setBigDecimal(var1, new BigDecimal(var3));
               break;
            case 4:
               var0.setInt(var1, Integer.parseInt(var3));
               break;
            case 6:
            case 7:
               var0.setFloat(var1, Float.parseFloat(var3));
               break;
            case 8:
               var0.setDouble(var1, Double.parseDouble(var3));
               break;
            case 10:
            case 92:
               if (var3.length() > 10 && var3.indexOf(58) > 10) {
                  var3 = var3.substring(11);
               }

               Time var11 = Time.valueOf(var3);
               var0.setTime(var1, var11);
               break;
            case 11:
            case 93:
               if (var3.length() <= 10) {
                  var3 = var3 + " 00:00:00";
               }

               Timestamp var10 = Timestamp.valueOf(var3);
               var0.setTimestamp(var1, var10);
               break;
            case 91:
               if (var3.length() > 10 && var3.indexOf(58) > 10) {
                  var3 = var3.substring(0, 10);
               }

               Date var9 = Date.valueOf(var3);
               var0.setDate(var1, var9);
               break;
            case 2005:
               try {
                  StringReader var8 = new StringReader(var3);
                  var0.setClob(var1, var8, var3.length());
               } catch (AbstractMethodError var7) {
                  var0.setString(var1, var3);
               }
               break;
            default:
               int var5 = var3.length();
               int var6 = var2.getDbColSize();
               if (var5 > var6 && var6 > 0) {
                  var3 = var3.substring(0, var6);
               }

               var0.setString(var1, var3);
         }
      }
   }

   private static boolean shouldBeBoundToNull(String var0, int var1) {
      if (var0 == null) {
         return true;
      } else {
         return !var0.isEmpty() ? false : isDataTypeNumeric(var1) || isDataTypeDateOrTime(var1);
      }
   }

   private static boolean isDataTypeNumeric(int var0) {
      switch (var0) {
         case -7:
         case -6:
         case -5:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
         case 8:
            return true;
         case -4:
         case -3:
         case -2:
         case -1:
         case 0:
         case 1:
         default:
            return false;
      }
   }

   private static boolean isDataTypeDateOrTime(int var0) {
      switch (var0) {
         case 9:
         case 10:
         case 11:
         case 91:
         case 92:
         case 93:
            return true;
         default:
            return false;
      }
   }

   private static byte[] fromHexString(String var0) {
      try {
         return Hex.decodeHex(var0.toCharArray());
      } catch (DecoderException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static String reportSqlForErrorMessage(String var0, List<CROM> var1) {
      StringBuilder var2 = new StringBuilder("'").append(var0).append("'");
      if (var1 != null && !var1.isEmpty()) {
         var2.append(" Parameters = [");
         var2.append(Collections2.join(var1, CROM_TO_VALUE, ", "));
         var2.append("]");
      }

      return var2.toString();
   }

   private DbUtils() {
   }

   public static class ReadLine {
      final int bufferLength;
      char[] buf;
      int pos;
      int len;
      Reader in;

      public ReadLine(Reader var1) {
         this.bufferLength = 10000;
         this.buf = new char[this.bufferLength];
         this.len = 0;
         this.pos = 0;
         this.in = var1;
      }

      public ReadLine(Reader var1, int var2) {
         this.bufferLength = var2;
         this.buf = new char[var2];
         this.len = 0;
         this.pos = 0;
         this.in = var1;
      }

      public String gets() throws IOException {
         StringBuilder var1 = new StringBuilder();
         boolean var2 = false;

         while (!var2) {
            if (this.pos >= this.len && this.len >= 0) {
               this.len = this.in.read(this.buf);
               this.pos = 0;
            }

            if (this.len < 0 && var1.length() == 0) {
               return null;
            }

            int var3;
            for (var3 = this.pos; var3 < this.len; var3++) {
               char var4 = this.buf[var3];
               if (var4 == '\n' || var4 == '\r') {
                  var3++;
                  var2 = true;
                  break;
               }
            }

            if (var3 > this.pos) {
               var1.append(this.buf, this.pos, var3 - this.pos);
            }

            this.pos = var3;
         }

         return var1.toString();
      }
   }
}
