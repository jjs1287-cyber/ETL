package org.etlforge.integration.structure.crom;

import java.io.IOException;
import java.io.Writer;
import org.etlforge.integration.structure.mapping.JtrXmlFormatUtils;
import org.etlforge.integration.structure.mapping.StringIndexMap;
import org.xml.sax.Attributes;

public final class CROM_DB extends CROM_ext {
   public static final String ELEMENT = "DB";
   private static final String SQL_FILTER = "Filter";
   private static final String SQL_ORDER_BY = "OrderBy";
   private static final String BD_SCHEMA = "Schema";
   private static final String TABLE_NAME = "TableName";
   private static final String MANUAL_SQL_STATEMENT = "ManualSQL";
   private String sqlFilter;
   private String sqlOrderBy;
   private String dbSchema;
   private String tableName;
   private String manualSqlStatement;

   public CROM_DB() {
   }

   public CROM_DB(Attributes var1) {
      this.tableName = JtrXmlFormatUtils.getString(var1, "TableName");
      this.dbSchema = JtrXmlFormatUtils.getString(var1, "Schema");
      this.sqlFilter = JtrXmlFormatUtils.getString(var1, "Filter");
      this.sqlOrderBy = JtrXmlFormatUtils.getString(var1, "OrderBy");
      this.manualSqlStatement = JtrXmlFormatUtils.getString(var1, "ManualSQL");
   }

   public String toString(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1);
      var2.append('<').append("DB");
      this.appendAttributes(var2);
      var2.append("/>\n");
      return var2.toString();
   }

   private void appendAttributes(StringBuilder var1) {
      JtrXmlFormatUtils.addAttribute(var1, "TableName", this.tableName);
      JtrXmlFormatUtils.addAttribute(var1, "Schema", this.dbSchema);
      JtrXmlFormatUtils.addAttribute(var1, "Filter", this.sqlFilter);
      JtrXmlFormatUtils.addAttribute(var1, "OrderBy", this.sqlOrderBy);
      JtrXmlFormatUtils.addAttribute(var1, "ManualSQL", this.manualSqlStatement);
   }

   @Override
   public String getSqlFilter() {
      return this.sqlFilter;
   }

   @Override
   public void setSqlFilter(String var1) {
      this.sqlFilter = var1;
   }

   @Override
   public String getSqlOrderBy() {
      return this.sqlOrderBy;
   }

   @Override
   public void setSqlOrderBy(String var1) {
      this.sqlOrderBy = var1;
   }

   @Override
   public String getSqlTableName() {
      return this.tableName;
   }

   @Override
   public void setSqlTableName(String var1) {
      this.tableName = var1;
   }

   @Override
   public void setDbSchema(String var1) {
      this.dbSchema = var1;
   }

   public String getManualSqlStatement() {
      return this.manualSqlStatement;
   }

   public void setManualSqlStatement(String var1) {
      this.manualSqlStatement = var1;
   }

   @Override
   public void writeXml(Writer var1, StringIndexMap var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append('<').append("DB");
      this.appendAttributes(var3);
      var3.append("/>\n");

      try {
         var1.write(var3.toString());
      } catch (IOException var5) {
         var5.printStackTrace();
      }
   }

   public CROM_DB clone() {
      return (CROM_DB)super.clone();
   }
}
