package com.etlforge.integration.server.implementation.webservice.bulkload;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsBulkLoadParams implements Serializable {
   private String salesforceVersion;
   private long operationInstanceId;
   private long lastOperationInstanceId;
   private long bulkLoadId;
   private String sourceFilePath;
   private String targetFilePath;
   private String queryString;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsBulkLoadParams.class, true);

   public WsBulkLoadParams() {
   }

   public WsBulkLoadParams(String var1, long var2, long var4, long var6, String var8, String var9, String var10) {
      this.salesforceVersion = var1;
      this.operationInstanceId = var2;
      this.lastOperationInstanceId = var4;
      this.bulkLoadId = var6;
      this.sourceFilePath = var8;
      this.targetFilePath = var9;
      this.queryString = var10;
   }

   public String getSalesforceVersion() {
      return this.salesforceVersion;
   }

   public void setSalesforceVersion(String var1) {
      this.salesforceVersion = var1;
   }

   public long getOperationInstanceId() {
      return this.operationInstanceId;
   }

   public void setOperationInstanceId(long var1) {
      this.operationInstanceId = var1;
   }

   public long getLastOperationInstanceId() {
      return this.lastOperationInstanceId;
   }

   public void setLastOperationInstanceId(long var1) {
      this.lastOperationInstanceId = var1;
   }

   public long getBulkLoadId() {
      return this.bulkLoadId;
   }

   public void setBulkLoadId(long var1) {
      this.bulkLoadId = var1;
   }

   public String getSourceFilePath() {
      return this.sourceFilePath;
   }

   public void setSourceFilePath(String var1) {
      this.sourceFilePath = var1;
   }

   public String getTargetFilePath() {
      return this.targetFilePath;
   }

   public void setTargetFilePath(String var1) {
      this.targetFilePath = var1;
   }

   public String getQueryString() {
      return this.queryString;
   }

   public void setQueryString(String var1) {
      this.queryString = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsBulkLoadParams)) {
         return false;
      }

      WsBulkLoadParams var2 = (WsBulkLoadParams)var1;
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
            this.salesforceVersion == null && var2.getSalesforceVersion() == null
               || this.salesforceVersion != null && this.salesforceVersion.equals(var2.getSalesforceVersion())
         )
         && this.operationInstanceId == var2.getOperationInstanceId()
         && this.lastOperationInstanceId == var2.getLastOperationInstanceId()
         && this.bulkLoadId == var2.getBulkLoadId()
         && (
            this.sourceFilePath == null && var2.getSourceFilePath() == null
               || this.sourceFilePath != null && this.sourceFilePath.equals(var2.getSourceFilePath())
         )
         && (
            this.targetFilePath == null && var2.getTargetFilePath() == null
               || this.targetFilePath != null && this.targetFilePath.equals(var2.getTargetFilePath())
         )
         && (this.queryString == null && var2.getQueryString() == null || this.queryString != null && this.queryString.equals(var2.getQueryString()));
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
      if (this.getSalesforceVersion() != null) {
         var1 += this.getSalesforceVersion().hashCode();
      }

      var1 += new Long(this.getOperationInstanceId()).hashCode();
      var1 += new Long(this.getLastOperationInstanceId()).hashCode();
      var1 += new Long(this.getBulkLoadId()).hashCode();
      if (this.getSourceFilePath() != null) {
         var1 += this.getSourceFilePath().hashCode();
      }

      if (this.getTargetFilePath() != null) {
         var1 += this.getTargetFilePath().hashCode();
      }

      if (this.getQueryString() != null) {
         var1 += this.getQueryString().hashCode();
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
      typeDesc.setXmlType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadParams"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("salesforceVersion");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "salesforceVersion"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("operationInstanceId");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "operationInstanceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("lastOperationInstanceId");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "lastOperationInstanceId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("bulkLoadId");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "bulkLoadId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("sourceFilePath");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "sourceFilePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("targetFilePath");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "targetFilePath"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("queryString");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "queryString"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
