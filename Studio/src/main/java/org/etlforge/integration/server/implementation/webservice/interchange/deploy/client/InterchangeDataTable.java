package org.etlforge.integration.server.implementation.webservice.interchange.deploy.client;

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

public class InterchangeDataTable implements Serializable {
   private String[] columnNames;
   private InterchangeDataRow[] rows;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(InterchangeDataTable.class, true);

   public InterchangeDataTable() {
   }

   public InterchangeDataTable(String[] var1, InterchangeDataRow[] var2) {
      this.columnNames = var1;
      this.rows = var2;
   }

   public String[] getColumnNames() {
      return this.columnNames;
   }

   public void setColumnNames(String[] var1) {
      this.columnNames = var1;
   }

   public InterchangeDataRow[] getRows() {
      return this.rows;
   }

   public void setRows(InterchangeDataRow[] var1) {
      this.rows = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof InterchangeDataTable)) {
         return false;
      }

      InterchangeDataTable var2 = (InterchangeDataTable)var1;
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
      boolean var3 = (
            this.columnNames == null && var2.getColumnNames() == null || this.columnNames != null && Arrays.equals(this.columnNames, var2.getColumnNames())
         )
         && (this.rows == null && var2.getRows() == null || this.rows != null && Arrays.equals(this.rows, var2.getRows()));
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
      if (this.getColumnNames() != null) {
         for (int var2 = 0; var2 < Array.getLength(this.getColumnNames()); var2++) {
            Object var3 = Array.get(this.getColumnNames(), var2);
            if (var3 != null && !var3.getClass().isArray()) {
               var1 += var3.hashCode();
            }
         }
      }

      if (this.getRows() != null) {
         for (int var4 = 0; var4 < Array.getLength(this.getRows()); var4++) {
            Object var5 = Array.get(this.getRows(), var4);
            if (var5 != null && !var5.getClass().isArray()) {
               var1 += var5.hashCode();
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
      typeDesc.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataTable")
      );
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("columnNames");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "columnNames"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("rows");
      var0.setXmlName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "rows"));
      var0.setXmlType(
         new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "InterchangeDataRow")
      );
      var0.setNillable(true);
      var0.setItemQName(new QName("urn:org.etlforge.integration.server.implementation.webservice.interchange.deploy.DeployInterchangeData", "item"));
      typeDesc.addFieldDesc(var0);
   }
}
