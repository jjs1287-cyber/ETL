package org.etlforge.integration.server.db.storedproc;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.db.ResultSetToStringArrayConverter;
import org.etlforge.db.ResultSetToStringListConverter;
import org.etlforge.integration.server.BackendEncoding;
import org.etlforge.integration.server.db.source.SourceValueReader;

public final class CallStoredProcedure {
   private final Connection connection;
   private boolean IsOracle;
   private final ResultSetToStringArrayConverter converter;
   private boolean autoCommit;
   private final SourceValueReader valueReader;
   private boolean hasReturnVal;

   public CallStoredProcedure(Connection var1) {
      Preconditions.checkNotNull(var1);
      this.connection = var1;
      this.IsOracle = var1 instanceof OracleConnection;
      if (this.IsOracle) {
         try {
            ((OracleConnection)var1).setDefaultRowPrefetch(100);
         } catch (Exception var3) {
         }
      }

      this.hasReturnVal = false;
      this.autoCommit = this.checkAutoCommit();
      this.valueReader = new SourceValueReader();
      this.converter = new ResultSetToStringArrayConverter(new ResultSetToStringListConverter() {
         @Override
         protected String readValueAsString(ResultSet var1, int var2) throws SQLException {
            return CallStoredProcedure.this.valueReader.readColumnValueAsString(var1, var2);
         }
      });
   }

   private boolean checkAutoCommit() {
      try {
         return this.connection.getAutoCommit();
      } catch (SQLException var2) {
         return true;
      }
   }

   public SPResult execute(String var1, String[] var2, int var3) throws SQLException {
      Preconditions.checkArgument(StringUtils.isNotEmpty(var1), "stored proc name may not be null or empty");
      CallableStatement var4 = null;

      try {
         if (var2 == null) {
            var2 = new String[0];
         }

         int var5 = var2.length;
         int var6 = 0;
         SPParameter[] var7 = this.getSPParameters(var1, var2);
         int var8 = var7.length;
         if (this.hasReturnVal) {
            var8--;
         }

         if (var8 != var5) {
            throw new SQLException("Bad number of stored proc parameters, expected: " + Integer.toString(var8) + " given: " + Integer.toString(var5));
         }

         String var9 = this.generateSPCallSQL(var1, var5);
         var4 = this.connection.prepareCall(var9);
         var6 = this.registerParameters(var4, var7);
         int var10 = var4.executeUpdate();
         List var11 = this.getOutputParameters(var4, var7);
         ArrayList var12 = Lists.newArrayList();
         if (!this.IsOracle) {
            ResultSet var13 = var4.getResultSet();
            KongaDbUtils.close(var13);
         }

         String var20 = "0";
         if (this.hasReturnVal) {
            var20 = "1";
         }

         return new SPResult(var12, var11, var20, var10);
      } finally {
         KongaDbUtils.close(var4);
      }
   }

   private String generateSPCallSQL(String var1, int var2) {
      String var3 = "";
      if (this.hasReturnVal) {
         var3 = " ? = ";
      }

      String var4 = "{" + var3 + "call " + var1 + "(";

      for (int var5 = 0; var5 < var2; var5++) {
         var4 = var4 + "?,";
      }

      if (var2 > 0) {
         var4 = var4.substring(0, var4.length() - 1);
      }

      return var4 + ")}";
   }

   private ResultSet findProcParams(String var1) throws SQLException {
      if (!var1.contains(".")) {
         return this.findProcParams(null, null, var1);
      }

      String[] var2 = var1.split("\\.");
      String var3 = var2[var2.length - 1];
      if (var2.length > 2) {
         return this.findProcParams(var2[1], var2[0], var3);
      }

      ResultSet var4 = this.findProcParams(var2[0], null, var3);
      return var4 != null ? var4 : this.findProcParams(null, var2[0], var3);
   }

   private ResultSet findProcParams(String var1, String var2, String var3) throws SQLException {
      DatabaseMetaData var4 = this.connection.getMetaData();
      ResultSet var5 = var4.getProcedures(var1, var2, var3);
      if (!var5.next()) {
         if (var1 != null) {
            var1 = var1.toUpperCase();
         }

         if (var2 != null) {
            var2 = var2.toUpperCase();
         }

         if (var3 != null) {
            var3 = var3.toUpperCase();
         }

         var5 = var4.getProcedures(var1, var2, var3);
         if (!var5.next()) {
            return null;
         }
      }

      if (var1 == null && var2 == null) {
         String var6 = var5.getString(1);
         if (var6 != null) {
            throw new SQLException(
               "Missing package/catalog name for proc: '" + var3 + "' To use this proc, include the package in the name like this: '" + var6 + "." + var3 + "'"
            );
         }
      }

      if (var1 != null && var2 == null) {
         var2 = "%";
      }

      return var4.getProcedureColumns(var1, var2, var3, "%");
   }

   private ResultSet findOracleProcParamsQuery(String var1, String var2) throws SQLException {
      String var3 = "select object_id, subprogram_id from sys.all_procedures where PROCEDURE_NAME = '"
         + var2.toUpperCase()
         + "' and OBJECT_NAME = '"
         + var1.toUpperCase()
         + "'";
      OracleConnection var4 = (OracleConnection)this.connection;
      OracleStatement var5 = (OracleStatement)var4.createStatement(1003, 1007);
      var5.setFetchSize(1);
      OracleResultSet var6 = (OracleResultSet)var5.executeQuery(var3);
      if (!var6.next()) {
         return null;
      }

      String var7 = var6.getNUMBER(1).stringValue();
      String var8 = var6.getNUMBER(2).stringValue();
      var6.close();
      var5.close();
      String var9 = "select  argument_name, in_out, data_type, data_scale, type_owner, type_name  from sys.all_arguments a  where a.OBJECT_ID = "
         + var7
         + " and a.SUBPROGRAM_ID = "
         + var8
         + " and data_level = 0 order by position";
      Statement var10 = this.connection.createStatement(1003, 1007);
      var10.setFetchSize(100);
      ResultSet var11 = var10.executeQuery(var9);
      return !var11.isBeforeFirst() ? null : var11;
   }

   private List<SPParameter> getPrmListFromOracle(String var1) throws SQLException {
      ArrayList var2 = new ArrayList();
      if (!var1.contains(".")) {
         return null;
      }

      ResultSet var3 = this.findOracleProcParamsQuery(var1.split("\\.")[0], var1.split("\\.")[1]);
      if (var3 == null) {
         return null;
      }

      int var4 = 0;

      while (var3.next()) {
         boolean var5 = false;
         String var6 = var3.getString(1);
         if (var3.wasNull()) {
            var5 = true;
         }

         String var7 = var3.getString(2);
         byte var8;
         if (var5) {
            var8 = 5;
         } else if (var7.equals("IN")) {
            var8 = 1;
         } else if (var7.equals("OUT")) {
            var8 = 4;
         } else if (!var7.equals("INOUT") && !var7.equals("IN_OUT")) {
            var8 = 0;
         } else {
            var8 = 2;
         }

         String var10 = var3.getString(3);
         String var11 = var10.toUpperCase();
         int var12 = var3.getInt(4);
         boolean var13 = var3.wasNull();
         String var14 = var3.getString(5);
         String var15 = var3.getString(6);
         if (var11.contains("BOOLEAN") || var11.contains("RECORD") || var11.contains("TABLE")) {
            throw new SQLException(
               "Unsupported parameter type found: "
                  + var11
                  + ", Oracle JDBC does not support this data type.  Parameter: "
                  + (var5 ? "Return Value" : var4 + 1)
                  + " "
                  + (var5 ? "" : var6)
            );
         }

         short var9;
         if (var11.equals("OBJECT")) {
            var9 = 1111;
         } else if (var11.equals("NUMBER")) {
            var9 = 3;
         } else if (var11.equals("DATA")) {
            var9 = 93;
         } else {
            var9 = 12;
         }

         String var16;
         if (var11.equals("OBJECT")) {
            var16 = var14 + "." + var15;
         } else {
            var16 = var10;
         }

         SPParameter var17 = new SPParameter(var6, var9, var8, var4++, var16);
         var2.add(var17);
      }

      KongaDbUtils.close(var3);
      return var2;
   }

   private List<SPParameter> getPrmListFromDatabase(String var1) throws SQLException {
      ArrayList var2 = new ArrayList();
      ResultSet var3 = this.findProcParams(var1);
      if (var3 == null) {
         throw new SQLException("Stored proc or function could not be found: " + var1);
      }

      while (var3.next()) {
         SPParameter var4 = new SPParameter(var3);
         var2.add(var4);
      }

      KongaDbUtils.close(var3);
      return var2;
   }

   private SPParameter[] getSPParameters(String var1, String[] var2) throws SQLException {
      List var3 = null;
      if (this.IsOracle) {
         var3 = this.getPrmListFromOracle(var1);
      }

      if (var3 == null) {
         this.getPrmListFromDatabase(var1);
      }

      SPParameter[] var4 = (SPParameter[]) var3.toArray(new SPParameter[var3.size()]);
      int var5 = 0;

      for (int var6 = 0; var6 < var4.length; var6++) {
         if (var4[var6].IsReturn) {
            this.hasReturnVal = true;
         } else if (var5 < var2.length) {
            var4[var6].Ordinal = var5;
            var4[var6].Value = var2[var5];
            var5++;
         }
      }

      return var4;
   }

   private int registerParameters(CallableStatement var1, SPParameter[] var2) throws SQLException {
      int var3 = 0;

      for (int var4 = 0; var4 < var2.length; var4++) {
         if (var2[var4].IsInput) {
            this.setInputParameter(var1, var4 + 1, var2[var4]);
         }

         if (var2[var4].IsOutput) {
            if (this.IsOracle && var2[var4].DataType == 1111) {
               OracleCallableStatement var5 = (OracleCallableStatement)var1;
               var5.registerOutParameter(var4 + 1, 2002, var2[var4].DataTypeName);
            } else {
               var1.registerOutParameter(var4 + 1, 12);
               var3++;
            }
         }
      }

      return var3;
   }

   private void setInputParameter(CallableStatement var1, int var2, SPParameter var3) throws SQLException {
      if (this.IsOracle && var3.DataType == 1111) {
         this.setOracleRecordInputParameter((OracleCallableStatement)var1, var2, var3);
      } else {
         var1.setObject(var2, var3.ParseValue());
      }
   }

   private Map<String, SPParameter> getRecordParameters(ResultSetMetaData var1, SPParameter var2) throws SQLException {
      HashMap var3 = new HashMap();

      for (int var4 = 0; var4 < var1.getColumnCount(); var4++) {
         SPParameter var5 = SPParameter.FromOracleRecord(var4 + 1, var1, var2);
         var3.put(var5.Name.toUpperCase(), var5);
      }

      return var3;
   }

   private void setOracleRecordInputParameter(OracleCallableStatement var1, int var2, SPParameter var3) throws SQLException {
      StructDescriptor var4 = StructDescriptor.createDescriptor(var3.DataTypeName, this.connection);
      ResultSetMetaData var5 = var4.getMetaData();
      Map var6 = this.getRecordParameters(var5, var3);
      String[] var7 = var3.Value.split("\\|");
      Object[] var8 = new Object[var5.getColumnCount()];

      for (int var9 = 0; var9 < var7.length; var9++) {
         String[] var10 = var7[var9].split(",");
         String var11 = var10[0].toUpperCase();
         int var12 = Integer.parseInt(var10[1]);
         String var13;
         if (var10.length > 2) {
            var13 = base64Decode(var10[2]);
         } else {
            var13 = "";
         }

         SPParameter var14 = (SPParameter)var6.get(var11);
         if (var14 != null) {
            int var15 = var14.Ordinal - 1;
            var14.Value = var13;
            if (var12 == 0) {
               var8[var15] = null;
            } else {
               var8[var15] = var14.ParseValue();
            }
         }
      }

      STRUCT var16 = new STRUCT(var4, this.connection, var8);
      var1.setObject(var2, var16);
   }

   private static String base64Decode(String var0) {
      byte[] var1 = var0.getBytes();
      Base64 var2 = new Base64();
      byte[] var3 = var2.decode(var1);
      return new String(var3, BackendEncoding.CHARSET);
   }

   private static String base64Encode(String var0) {
      byte[] var1 = Base64.encodeBase64Chunked(var0.getBytes(BackendEncoding.CHARSET));
      StringBuilder var2 = new StringBuilder();
      var2.append(new String(var1, Charsets.US_ASCII));
      return var2.toString();
   }

   private List<SPParameter> getOutputParameters(CallableStatement var1, SPParameter[] var2) throws SQLException {
      ArrayList var3 = Lists.newArrayList();

      for (int var4 = 0; var4 < var2.length; var4++) {
         if (var2[var4].IsOutput) {
            if (this.IsOracle && var2[var4].DataType == 1111) {
               OracleCallableStatement var5 = (OracleCallableStatement)var1;
               STRUCT var6 = var5.getSTRUCT(var4 + 1);
               ResultSetMetaData var7 = var6.getDescriptor().getMetaData();
               Object[] var8 = var6.getAttributes();
               String var9 = "";

               for (int var10 = 0; var10 < var8.length; var10++) {
                  String var11 = var7.getColumnName(var10 + 1);
                  int var12 = var7.getColumnType(var10 + 1);
                  String var13;
                  if (var8[var10] == null) {
                     var13 = "";
                  } else {
                     var13 = base64Encode(var8[var10].toString());
                  }

                  var9 = var9 + var11 + "," + var12 + "," + var13 + "|";
               }

               var9 = var9.substring(0, var9.length() - 1);
               var2[var4].Value = var9;
            } else {
               var2[var4].Value = var1.getObject(var4 + 1).toString();
               if (var2[var4].Value == null) {
                  var2[var4].DataType = 999;
               }
            }

            var3.add(var2[var4]);
         }
      }

      return var3;
   }

   public void setAutoCommit(boolean var1) throws SQLException {
      if (var1 != this.autoCommit) {
         if (var1) {
            try {
               this.connection.setAutoCommit(var1);
            } catch (SQLException var3) {
            }
         } else {
            this.connection.setAutoCommit(var1);
         }

         this.autoCommit = var1;
      }
   }
}
