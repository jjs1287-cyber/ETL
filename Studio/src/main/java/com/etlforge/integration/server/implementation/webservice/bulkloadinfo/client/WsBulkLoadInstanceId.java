package com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsBulkLoadInstanceId implements Serializable {
   private String bulkLoadGuid;
   private long operationInstanceId;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsBulkLoadInstanceId.class, true);

   public WsBulkLoadInstanceId() {
   }

   public WsBulkLoadInstanceId(String var1, long var2) {
      this.bulkLoadGuid = var1;
      this.operationInstanceId = var2;
   }

   public String getBulkLoadGuid() {
      return this.bulkLoadGuid;
   }

   public void setBulkLoadGuid(String var1) {
      this.bulkLoadGuid = var1;
   }

   public long getOperationInstanceId() {
      return this.operationInstanceId;
   }

   public void setOperationInstanceId(long var1) {
      this.operationInstanceId = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsBulkLoadInstanceId)) {
         return false;
      }

      WsBulkLoadInstanceId var2 = (WsBulkLoadInstanceId)var1;
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
         && this.operationInstanceId == var2.getOperationInstanceId();
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

      var1 += new Long(this.getOperationInstanceId()).hashCode();
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
      typeDesc.setXmlType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadInstanceId"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("bulkLoadGuid");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "BulkLoadGuid"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationInstanceId");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "OperationInstanceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
