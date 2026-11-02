package org.etlforge.integration.server.implementation.webservice.interchange.db.client;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsJdbcDatabaseObject implements Serializable {
   private String objectType;
   private String objectName;
   private String schemaName;
   private WsJdbcDatabaseColumn[] columns;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsJdbcDatabaseObject.class, true);

   public WsJdbcDatabaseObject() {
   }

   public WsJdbcDatabaseObject(String var1, String var2, String var3, WsJdbcDatabaseColumn[] var4) {
      this.objectType = var1;
      this.objectName = var2;
      this.schemaName = var3;
      this.columns = var4;
   }

   public String getObjectType() {
      return this.objectType;
   }

   public void setObjectType(String var1) {
      this.objectType = var1;
   }

   public String getObjectName() {
      return this.objectName;
   }

   public void setObjectName(String var1) {
      this.objectName = var1;
   }

   public String getSchemaName() {
      return this.schemaName;
   }

   public void setSchemaName(String var1) {
      this.schemaName = var1;
   }

   public WsJdbcDatabaseColumn[] getColumns() {
      return this.columns;
   }

   public void setColumns(WsJdbcDatabaseColumn[] var1) {
      this.columns = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsJdbcDatabaseObject)) {
         return false;
      }

      WsJdbcDatabaseObject var2 = (WsJdbcDatabaseObject)var1;
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
      boolean var3 = (this.objectType == null && var2.getObjectType() == null || this.objectType != null && this.objectType.equals(var2.getObjectType()))
         && (this.objectName == null && var2.getObjectName() == null || this.objectName != null && this.objectName.equals(var2.getObjectName()))
         && (this.schemaName == null && var2.getSchemaName() == null || this.schemaName != null && this.schemaName.equals(var2.getSchemaName()))
         && (this.columns == null && var2.getColumns() == null || this.columns != null && Arrays.equals(this.columns, var2.getColumns()));
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
      if (this.getObjectType() != null) {
         var1 += this.getObjectType().hashCode();
      }

      if (this.getObjectName() != null) {
         var1 += this.getObjectName().hashCode();
      }

      if (this.getSchemaName() != null) {
         var1 += this.getSchemaName().hashCode();
      }

      if (this.getColumns() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getColumns()); var2++) {
            Object var3 = Array.get(this.getColumns(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

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
      typeDesc.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseObject"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("objectType");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "objectType"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("objectName");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "objectName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("schemaName");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "schemaName"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("columns");
      var0.setXmlName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "columns"));
      var0.setXmlType(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "WsJdbcDatabaseColumn"));
      var0.setNillable(true);
      var0.setItemQName(new QName("http://db.interchange.webservice.implementation.server.integration.etlforge.invalid", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
