package com.etlforge.integration.server.implementation.webservice.bulkloadinfo;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsBatch implements Serializable {
   private String salesforceId;
   private long startPos;
   private long endPos;
   private int numberOfRecords;
   private int processedRecords;
   private int successRecords;
   private int failureRecords;
   private int status;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsBatch.class, true);

   public WsBatch() {
   }

   public WsBatch(String var1, long var2, long var4, int var6, int var7, int var8, int var9, int var10) {
      this.salesforceId = var1;
      this.startPos = var2;
      this.endPos = var4;
      this.numberOfRecords = var6;
      this.processedRecords = var7;
      this.successRecords = var8;
      this.failureRecords = var9;
      this.status = var10;
   }

   public String getSalesforceId() {
      return this.salesforceId;
   }

   public void setSalesforceId(String var1) {
      this.salesforceId = var1;
   }

   public long getStartPos() {
      return this.startPos;
   }

   public void setStartPos(long var1) {
      this.startPos = var1;
   }

   public long getEndPos() {
      return this.endPos;
   }

   public void setEndPos(long var1) {
      this.endPos = var1;
   }

   public int getNumberOfRecords() {
      return this.numberOfRecords;
   }

   public void setNumberOfRecords(int var1) {
      this.numberOfRecords = var1;
   }

   public int getProcessedRecords() {
      return this.processedRecords;
   }

   public void setProcessedRecords(int var1) {
      this.processedRecords = var1;
   }

   public int getSuccessRecords() {
      return this.successRecords;
   }

   public void setSuccessRecords(int var1) {
      this.successRecords = var1;
   }

   public int getFailureRecords() {
      return this.failureRecords;
   }

   public void setFailureRecords(int var1) {
      this.failureRecords = var1;
   }

   public int getStatus() {
      return this.status;
   }

   public void setStatus(int var1) {
      this.status = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsBatch)) {
         return false;
      }

      WsBatch var2 = (WsBatch)var1;
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
            this.salesforceId == null && var2.getSalesforceId() == null || this.salesforceId != null && this.salesforceId.equals(var2.getSalesforceId())
         )
         && this.startPos == var2.getStartPos()
         && this.endPos == var2.getEndPos()
         && this.numberOfRecords == var2.getNumberOfRecords()
         && this.processedRecords == var2.getProcessedRecords()
         && this.successRecords == var2.getSuccessRecords()
         && this.failureRecords == var2.getFailureRecords()
         && this.status == var2.getStatus();
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
      if (this.getSalesforceId() != null) {
         var1 += this.getSalesforceId().hashCode();
      }

      var1 += new Long(this.getStartPos()).hashCode();
      var1 += new Long(this.getEndPos()).hashCode();
      var1 += this.getNumberOfRecords();
      var1 += this.getProcessedRecords();
      var1 += this.getSuccessRecords();
      var1 += this.getFailureRecords();
      var1 += this.getStatus();
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
      typeDesc.setXmlType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBatch"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("salesforceId");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "SalesforceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("startPos");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "StartPos"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("endPos");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "EndPos"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("numberOfRecords");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "NumberOfRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("processedRecords");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "ProcessedRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("successRecords");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "SuccessRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("failureRecords");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "FailureRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("status");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "Status"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
   }
}
