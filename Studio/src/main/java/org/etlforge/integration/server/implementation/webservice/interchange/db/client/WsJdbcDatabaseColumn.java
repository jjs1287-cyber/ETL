package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsJdbcDatabaseColumn implements Serializable {
   private String name;
   private String type;
   private String defaultValue;
   private int sqlType;
   private Boolean isPrimaryKey;
   private Boolean isNullable;
   private int columnSize;
   private int decimalDigits;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsJdbcDatabaseColumn.class, true);

   public WsJdbcDatabaseColumn() {
   }

   public WsJdbcDatabaseColumn(String var1, String var2, String var3, int var4, Boolean var5, Boolean var6, int var7, int var8) {
      this.name = var1;
      this.type = var2;
      this.defaultValue = var3;
      this.sqlType = var4;
      this.isPrimaryKey = var5;
      this.isNullable = var6;
      this.columnSize = var7;
      this.decimalDigits = var8;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getDefaultValue() {
      return this.defaultValue;
   }

   public void setDefaultValue(String var1) {
      this.defaultValue = var1;
   }

   public int getSqlType() {
      return this.sqlType;
   }

   public void setSqlType(int var1) {
      this.sqlType = var1;
   }

   public Boolean getIsPrimaryKey() {
      return this.isPrimaryKey;
   }

   public void setIsPrimaryKey(Boolean var1) {
      this.isPrimaryKey = var1;
   }

   public Boolean getIsNullable() {
      return this.isNullable;
   }

   public void setIsNullable(Boolean var1) {
      this.isNullable = var1;
   }

   public int getColumnSize() {
      return this.columnSize;
   }

   public void setColumnSize(int var1) {
      this.columnSize = var1;
   }

   public int getDecimalDigits() {
      return this.decimalDigits;
   }

   public void setDecimalDigits(int var1) {
      this.decimalDigits = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsJdbcDatabaseColumn)) {
         return false;
      }

      WsJdbcDatabaseColumn var2 = (WsJdbcDatabaseColumn)var1;
      if (var1 == null) {
         return false;
      }

      if (this == var1) {
         return true;
      }

      if (this.__equalsCalc != null) {
         return this.__equalsCalc == var1;
      }

      this.__equalsCalc = var1;
      boolean var3 = (this.name == null && var2.getName() == null || this.name != null && this.name.equals(var2.getName()))
         && (this.type == null && var2.getType() == null || this.type != null && this.type.equals(var2.getType()))
         && (this.defaultValue == null && var2.getDefaultValue() == null || this.defaultValue != null && this.defaultValue.equals(var2.getDefaultValue()))
         && this.sqlType == var2.getSqlType()
         && (this.isPrimaryKey == null && var2.getIsPrimaryKey() == null || this.isPrimaryKey != null && this.isPrimaryKey.equals(var2.getIsPrimaryKey()))
         && (this.isNullable == null && var2.getIsNullable() == null || this.isNullable != null && this.isNullable.equals(var2.getIsNullable()))
         && this.columnSize == var2.getColumnSize()
         && this.decimalDigits == var2.getDecimalDigits();
      this.__equalsCalc = null;
      return var3;
   }

   @Override
   public synchronized int hashCode() {
      if (this.__hashCodeCalc) {
         return 0;
      }

      this.__hashCodeCalc = true;
      int var1 = 1;
      if (this.getName() != null) {
         var1 += this.getName().hashCode();
      }

      if (this.getType() != null) {
         var1 += this.getType().hashCode();
      }

      if (this.getDefaultValue() != null) {
         var1 += this.getDefaultValue().hashCode();
      }

      var1 += this.getSqlType();
      if (this.getIsPrimaryKey() != null) {
         var1 += this.getIsPrimaryKey().hashCode();
      }

      if (this.getIsNullable() != null) {
         var1 += this.getIsNullable().hashCode();
      }

      var1 += this.getColumnSize();
      var1 += this.getDecimalDigits();
      this.__hashCodeCalc = false;
      return var1;
   }

   public static TypeDesc getTypeDesc() {
      return typeDesc;
   }

   public static Serializer getSerializer(String var0, Class var1, QName var2) {
      return new BeanSerializer(var1, var2, typeDesc);
   }

   public static Deserializer getDeserializer(String var0, Class var1, QName var2) {
      return new BeanDeserializer(var1, var2, typeDesc);
   }

   static {
      typeDesc.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseColumn"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("name");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "name"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("type");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "type"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("defaultValue");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "defaultValue"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sqlType");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "sqlType"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("isPrimaryKey");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "isPrimaryKey"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("isNullable");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "isNullable"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("columnSize");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "columnSize"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("decimalDigits");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "decimalDigits"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
