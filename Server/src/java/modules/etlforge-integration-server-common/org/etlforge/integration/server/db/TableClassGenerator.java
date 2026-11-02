package org.etlforge.integration.server.db;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.etlforge.db.DBConnectionInfo;
import org.etlforge.db.KongaDbUtils;
import org.etlforge.io.KongaIoUtils;

class TableClassGenerator {
   private final Connection connection;

   public static void main(String[] var0) throws Exception {
      TableClassGenerator var1 = new TableClassGenerator();
      var1.generate(new File("Z:\\dev\\tmp\\transformationstab_new.txt"), "transformationstab_new");
   }

   public TableClassGenerator() throws SQLException {
      DBConnectionInfo var1 = ServerDbInfo.getTranDbConnectionInfo();
      this.connection = var1.getConnection();
   }

   public void generate(File var1, String var2) throws SQLException, IOException {
      try {
         List var3 = this.getColumnInfo(var2);
         StringBuilder var4 = new StringBuilder();
         this.writeFieldDeclarations(var4, var3);
         this.writePopulateMethod(var4, var3);
         this.writeGetters(var4, var3);
         this.writeFile(var1, var4);
      } finally {
         this.close();
      }
   }

   private void close() {
      KongaDbUtils.close(this.connection);
   }

   private List<TableClassGenerator.ColumnInfo> getColumnInfo(String var1) throws SQLException {
      ArrayList var2 = new ArrayList();
      DatabaseMetaData var3 = this.connection.getMetaData();
      ResultSet var4 = var3.getColumns(null, null, var1, null);

      try {
         while (var4.next()) {
            String var5 = var4.getString(4);
            String var6 = var4.getString(6);
            var2.add(new TableClassGenerator.ColumnInfo(var5, var6));
         }
      } finally {
         KongaDbUtils.close(var4);
      }

      return var2;
   }

   private void writeFieldDeclarations(StringBuilder var1, Collection<TableClassGenerator.ColumnInfo> var2) {
      var1.append("*** FIELDS ***").append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);

      for (TableClassGenerator.ColumnInfo var4 : var2) {
         var4.addFieldDeclaration(var1);
      }

      var1.append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);
   }

   private void writePopulateMethod(StringBuilder var1, Collection<TableClassGenerator.ColumnInfo> var2) {
      var1.append("*** POPULATE() ***").append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);
      var1.append("public void populate(ResultSet rs) throws SQLException {").append(KongaIoUtils.LINE_BREAK);

      for (TableClassGenerator.ColumnInfo var4 : var2) {
         var4.addPopulateStatement(var1);
      }

      var1.append("}").append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);
   }

   private void writeGetters(StringBuilder var1, Collection<TableClassGenerator.ColumnInfo> var2) {
      var1.append("*** GETTERS ***").append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);

      for (TableClassGenerator.ColumnInfo var4 : var2) {
         var4.addGetter(var1);
      }

      var1.append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);
   }

   private void writeFile(File var1, StringBuilder var2) throws IOException {
      KongaIoUtils.createAndWrite(var1, var2.toString());
   }

   private static class ColumnInfo {
      private final String name;
      private final TableClassGenerator.Type type;

      public ColumnInfo(String var1, String var2) {
         this.name = var1;
         this.type = TableClassGenerator.Type.fromTypeString(var2);
      }

      public void addFieldDeclaration(StringBuilder var1) {
         var1.append("private final ");
         this.type.appendColumnDataType(var1);
         var1.append(" ").append(this.name).append(" = ");
         this.type.appendColumnDataCreation(this.name, var1);
         var1.append(";").append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);
      }

      public void addPopulateStatement(StringBuilder var1) {
         var1.append("\t").append(this.name).append(".fetchValue(rs);").append(KongaIoUtils.LINE_BREAK);
      }

      private void addGetter(StringBuilder var1) {
         this.type.appendGetter(this.name, var1);
      }
   }

   private enum Type {
      INT,
      STRING,
      TIMESTAMP;

      public static TableClassGenerator.Type fromTypeString(String var0) {
         if (var0.startsWith("int")) {
            return INT;
         } else if (var0.startsWith("timestamp")) {
            return TIMESTAMP;
         } else if (!var0.startsWith("varchar") && !var0.startsWith("text")) {
            System.err.println("Do not know how to handle the type " + var0);
            System.err.println("   Using STRING as default");
            return STRING;
         } else {
            return STRING;
         }
      }

      public void appendColumnDataType(StringBuilder var1) {
         switch (this) {
            case INT:
               var1.append("ColumnData<Integer>");
               break;
            case STRING:
               var1.append("ColumnData<String>");
               break;
            case TIMESTAMP:
               var1.append("ColumnData<Timestamp>");
         }
      }

      public void appendColumnDataCreation(String var1, StringBuilder var2) {
         switch (this) {
            case INT:
               var2.append("ColumnData.newIntColumn");
               break;
            case STRING:
               var2.append("ColumnData.newStringColumn");
               break;
            case TIMESTAMP:
               var2.append("ColumnData.newTimestampColumn");
         }

         var2.append("(\"").append(var1).append("\")");
      }

      public void appendGetter(String var1, StringBuilder var2) {
         var2.append("public ");
         switch (this) {
            case INT:
               var2.append("Integer");
               break;
            case STRING:
               var2.append("String");
               break;
            case TIMESTAMP:
               var2.append("Timestamp");
         }

         var2.append(" get_").append(var1).append("() {").append(KongaIoUtils.LINE_BREAK);
         var2.append("\treturn ").append(var1).append(".getValue();").append(KongaIoUtils.LINE_BREAK);
         var2.append("}").append(KongaIoUtils.LINE_BREAK).append(KongaIoUtils.LINE_BREAK);
      }
   }
}
