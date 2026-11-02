package com.etlforge.integration.server.implementation.webservice.bulkloadinfo.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsBulkLoadInstance implements Serializable {
   private WsBulkLoadInstanceId id;
   private String startTime;
   private String endTime;
   private long lastOperationInstanceId;
   private String jobId;
   private String filePath;
   private long fileSize;
   private long startPos;
   private long successRecords;
   private long failureRecords;
   private int status;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsBulkLoadInstance.class, true);

   public WsBulkLoadInstance() {
   }

   public WsBulkLoadInstance(
      WsBulkLoadInstanceId var1, String var2, String var3, long var4, String var6, String var7, long var8, long var10, long var12, long var14, int var16
   ) {
      this.id = var1;
      this.startTime = var2;
      this.endTime = var3;
      this.lastOperationInstanceId = var4;
      this.jobId = var6;
      this.filePath = var7;
      this.fileSize = var8;
      this.startPos = var10;
      this.successRecords = var12;
      this.failureRecords = var14;
      this.status = var16;
   }

   public WsBulkLoadInstanceId getId() {
      return this.id;
   }

   public void setId(WsBulkLoadInstanceId var1) {
      this.id = var1;
   }

   public String getStartTime() {
      return this.startTime;
   }

   public void setStartTime(String var1) {
      this.startTime = var1;
   }

   public String getEndTime() {
      return this.endTime;
   }

   public void setEndTime(String var1) {
      this.endTime = var1;
   }

   public long getLastOperationInstanceId() {
      return this.lastOperationInstanceId;
   }

   public void setLastOperationInstanceId(long var1) {
      this.lastOperationInstanceId = var1;
   }

   public String getJobId() {
      return this.jobId;
   }

   public void setJobId(String var1) {
      this.jobId = var1;
   }

   public String getFilePath() {
      return this.filePath;
   }

   public void setFilePath(String var1) {
      this.filePath = var1;
   }

   public long getFileSize() {
      return this.fileSize;
   }

   public void setFileSize(long var1) {
      this.fileSize = var1;
   }

   public long getStartPos() {
      return this.startPos;
   }

   public void setStartPos(long var1) {
      this.startPos = var1;
   }

   public long getSuccessRecords() {
      return this.successRecords;
   }

   public void setSuccessRecords(long var1) {
      this.successRecords = var1;
   }

   public long getFailureRecords() {
      return this.failureRecords;
   }

   public void setFailureRecords(long var1) {
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
      if (!(var1 instanceof WsBulkLoadInstance)) {
         return false;
      }

      WsBulkLoadInstance var2 = (WsBulkLoadInstance)var1;
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
      boolean var3 = (this.id == null && var2.getId() == null || this.id != null && this.id.equals(var2.getId()))
         && (this.startTime == null && var2.getStartTime() == null || this.startTime != null && this.startTime.equals(var2.getStartTime()))
         && (this.endTime == null && var2.getEndTime() == null || this.endTime != null && this.endTime.equals(var2.getEndTime()))
         && this.lastOperationInstanceId == var2.getLastOperationInstanceId()
         && (this.jobId == null && var2.getJobId() == null || this.jobId != null && this.jobId.equals(var2.getJobId()))
         && (this.filePath == null && var2.getFilePath() == null || this.filePath != null && this.filePath.equals(var2.getFilePath()))
         && this.fileSize == var2.getFileSize()
         && this.startPos == var2.getStartPos()
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
      if (this.getId() != null) {
         var1 += this.getId().hashCode();
      }

      if (this.getStartTime() != null) {
         var1 += this.getStartTime().hashCode();
      }

      if (this.getEndTime() != null) {
         var1 += this.getEndTime().hashCode();
      }

      var1 += new Long(this.getLastOperationInstanceId()).hashCode();
      if (this.getJobId() != null) {
         var1 += this.getJobId().hashCode();
      }

      if (this.getFilePath() != null) {
         var1 += this.getFilePath().hashCode();
      }

      var1 += new Long(this.getFileSize()).hashCode();
      var1 += new Long(this.getStartPos()).hashCode();
      var1 += new Long(this.getSuccessRecords()).hashCode();
      var1 += new Long(this.getFailureRecords()).hashCode();
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
      typeDesc.setXmlType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadInstance"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("id");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "Id"));
      var0.setXmlType(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadInstanceId"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("startTime");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "StartTime"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("endTime");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "EndTime"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("lastOperationInstanceId");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "LastOperationInstanceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("jobId");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "JobId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("filePath");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "FilePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("fileSize");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "FileSize"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("startPos");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "StartPos"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("successRecords");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "SuccessRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("failureRecords");
      var0.setXmlName(new QName("http://bulkloadinfo.webservice.implementation.server.integration.etlforge.invalid", "FailureRecords"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
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
