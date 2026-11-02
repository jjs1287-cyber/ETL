package com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsInstanceFilter implements Serializable {
   private String bulkLoadGuid;
   private int maxRecords;
   private String startDate;
   private String endDate;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsInstanceFilter.class, true);

   public WsInstanceFilter() {
   }

   public WsInstanceFilter(String var1, int var2, String var3, String var4) {
      this.bulkLoadGuid = var1;
      this.maxRecords = var2;
      this.startDate = var3;
      this.endDate = var4;
   }

   public String getBulkLoadGuid() {
      return this.bulkLoadGuid;
   }

   public void setBulkLoadGuid(String var1) {
      this.bulkLoadGuid = var1;
   }

   public int getMaxRecords() {
      return this.maxRecords;
   }

   public void setMaxRecords(int var1) {
      this.maxRecords = var1;
   }

   public String getStartDate() {
      return this.startDate;
   }

   public void setStartDate(String var1) {
      this.startDate = var1;
   }

   public String getEndDate() {
      return this.endDate;
   }

   public void setEndDate(String var1) {
      this.endDate = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsInstanceFilter)) {
         return false;
      }

      WsInstanceFilter var2 = (WsInstanceFilter)var1;
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
            this.bulkLoadGuid == null && var2.getBulkLoadGuid() == null || this.bulkLoadGuid != null && this.bulkLoadGuid.equals(var2.getBulkLoadGuid())
         )
         && this.maxRecords == var2.getMaxRecords()
         && (this.startDate == null && var2.getStartDate() == null || this.startDate != null && this.startDate.equals(var2.getStartDate()))
         && (this.endDate == null && var2.getEndDate() == null || this.endDate != null && this.endDate.equals(var2.getEndDate()));
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
      if (this.getBulkLoadGuid() != null) {
         var1 += this.getBulkLoadGuid().hashCode();
      }

      var1 += this.getMaxRecords();
      if (this.getStartDate() != null) {
         var1 += this.getStartDate().hashCode();
      }

      if (this.getEndDate() != null) {
         var1 += this.getEndDate().hashCode();
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
      typeDesc.setXmlType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsInstanceFilter"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("bulkLoadGuid");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "BulkLoadGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("maxRecords");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "MaxRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("startDate");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "StartDate"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("endDate");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "EndDate"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
