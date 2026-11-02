package com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsRecordOfSentBatch implements Serializable {
   private WsBulkLoadInstanceId instanceId;
   private WsBatch batch;
   private String timeSent;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsRecordOfSentBatch.class, true);

   public WsRecordOfSentBatch() {
   }

   public WsRecordOfSentBatch(WsBulkLoadInstanceId var1, WsBatch var2, String var3) {
      this.instanceId = var1;
      this.batch = var2;
      this.timeSent = var3;
   }

   public WsBulkLoadInstanceId getInstanceId() {
      return this.instanceId;
   }

   public void setInstanceId(WsBulkLoadInstanceId var1) {
      this.instanceId = var1;
   }

   public WsBatch getBatch() {
      return this.batch;
   }

   public void setBatch(WsBatch var1) {
      this.batch = var1;
   }

   public String getTimeSent() {
      return this.timeSent;
   }

   public void setTimeSent(String var1) {
      this.timeSent = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsRecordOfSentBatch)) {
         return false;
      }

      WsRecordOfSentBatch var2 = (WsRecordOfSentBatch)var1;
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
      boolean var3 = (this.instanceId == null && var2.getInstanceId() == null || this.instanceId != null && this.instanceId.equals(var2.getInstanceId()))
         && (this.batch == null && var2.getBatch() == null || this.batch != null && this.batch.equals(var2.getBatch()))
         && (this.timeSent == null && var2.getTimeSent() == null || this.timeSent != null && this.timeSent.equals(var2.getTimeSent()));
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
      if (this.getInstanceId() != null) {
         var1 += this.getInstanceId().hashCode();
      }

      if (this.getBatch() != null) {
         var1 += this.getBatch().hashCode();
      }

      if (this.getTimeSent() != null) {
         var1 += this.getTimeSent().hashCode();
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
      typeDesc.setXmlType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsRecordOfSentBatch"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("instanceId");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "InstanceId"));
      var0.setXmlType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadInstanceId"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("batch");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "Batch"));
      var0.setXmlType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBatch"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("timeSent");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "TimeSent"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
