package org.etlforge.integration.server.db.storedproc;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SPParameter {
   public String Name;
   public int DataType;
   public String DataTypeName;
   public boolean IsOutput;
   public boolean IsInput;
   public boolean IsReturn;
   public int InputOutput;
   public int Ordinal;
   public String Value;
   public int Length;
   public SPParameter Parent;

   public String GetFullName() {
      return this.Parent != null ? this.Parent.GetFullName() + "." + this.Name : this.Name;
   }

   public SPParameter(ResultSet var1) throws SQLException {
      int var2 = var1.getMetaData().getColumnCount();
      this.Name = var1.getString(4);
      this.DataType = var1.getInt(6);
      this.InputOutput = var1.getShort(5);
      this.IsOutput = this.InputOutput == 4 || this.InputOutput == 2 || this.InputOutput == 5;
      this.IsInput = this.InputOutput == 1 || this.InputOutput == 2;
      this.IsReturn = this.InputOutput == 5;
      if (var2 >= 18) {
         this.Ordinal = var1.getInt(18);
      } else {
         this.Ordinal = var1.getRow();
      }

      this.Length = 0;
      if (var2 >= 17) {
         this.Length = var1.getInt(17);
      }

      if (this.Length == 0) {
         this.Length = var1.getInt(9);
      }

      this.DataTypeName = var1.getString(7).toUpperCase();
      this.Parent = null;
   }

   public static SPParameter FromOracleRecord(int var0, ResultSetMetaData var1, SPParameter var2) throws SQLException {
      return new SPParameter(var1.getColumnName(var0), var1.getColumnType(var0), var1.getColumnTypeName(var0), var2, var0, var1.getColumnDisplaySize(var0));
   }

   public SPParameter(String var1, int var2, String var3, SPParameter var4, int var5, int var6) throws SQLException {
      this.Name = var1;
      this.DataType = var2;
      this.DataTypeName = var3;
      this.Parent = var4;
      this.Ordinal = var5;
      this.Length = var6;
   }

   public SPParameter(String var1, int var2, int var3, int var4) throws SQLException {
      this.Name = var1;
      this.DataType = var2;
      this.InputOutput = var3;
      this.IsOutput = this.InputOutput == 4 || this.InputOutput == 2 || this.InputOutput == 5;
      this.IsInput = this.InputOutput == 1 || this.InputOutput == 2;
      this.IsReturn = this.InputOutput == 5;
      this.Ordinal = var4;
      this.Parent = null;
   }

   public SPParameter(String var1, int var2, int var3, int var4, String var5) throws SQLException {
      this.Name = var1;
      this.DataType = var2;
      this.InputOutput = var3;
      this.IsOutput = this.InputOutput == 4 || this.InputOutput == 2 || this.InputOutput == 5;
      this.IsInput = this.InputOutput == 1 || this.InputOutput == 2;
      this.IsReturn = this.InputOutput == 5;
      this.Ordinal = var4;
      this.Parent = null;
      this.DataTypeName = var5;
   }

   private String formatError() {
      return "Error formatting parameter data type for stored procedure. Parameter: '"
         + this.GetFullName()
         + "' Expected Type: '"
         + this.DataTypeName
         + "' Given Value: '"
         + this.Value
         + "'";
   }

   private String lengthError() {
      return "Error parsing stored procedure parameter, value length too long.  Parameter: '"
         + this.GetFullName()
         + "' Maximum Length: "
         + this.Length
         + " Actual Length: "
         + this.Value.length()
         + " Given Value: '"
         + this.Value
         + "'";
   }

   public Object ParseValue() throws SQLException {
      switch (this.DataType) {
         case -16:
         case -15:
         case -1:
         case 1:
         case 12:
            if (this.Length > 0 && this.Value.length() > this.Length) {
               throw new SQLException(this.lengthError());
            }

            return this.Value;
         case -6:
         case -5:
         case 4:
         case 5:
            try {
               return Integer.parseInt(this.Value);
            } catch (Exception var5) {
               throw new SQLException(this.formatError());
            }
         case 2:
         case 3:
            try {
               return new BigDecimal(this.Value);
            } catch (Exception var4) {
               throw new SQLException(this.formatError());
            }
         case 6:
         case 8:
            try {
               return Double.parseDouble(this.Value);
            } catch (Exception var3) {
               throw new SQLException(this.formatError());
            }
         case 16:
            try {
               return Boolean.parseBoolean(this.Value);
            } catch (Exception var2) {
               throw new SQLException(this.formatError());
            }
         case 91:
         case 92:
         case 93:
            return this.dateParse(this.Value);
         default:
            return this.Value;
      }
   }

   private Date dateParse(String var1) throws SQLException {
      boolean var2 = this.shouldSwapDateAndMonth();
      String var3 = var2 ? "yyyy-dd-MM" : "yyyy-MM-dd";
      if (var1.length() > 10) {
         var3 = var3 + " hh:mm:ss";
      }

      SimpleDateFormat var4 = new SimpleDateFormat(var3);
      long var5 = 0L;

      try {
         var5 = var4.parse(var1).getTime();
      } catch (Exception var8) {
         throw new SQLException("Unable to format datetime, expected format: '" + var3 + "', given datetime: '" + var1 + "'");
      }

      return new Date(var5);
   }

   private boolean shouldSwapDateAndMonth() {
      DateFormat var1 = DateFormat.getDateInstance(3, Locale.getDefault());
      if (var1 instanceof SimpleDateFormat) {
         String var2 = ((SimpleDateFormat)var1).toPattern();
         return !var2.startsWith("M");
      } else {
         return false;
      }
   }
}
