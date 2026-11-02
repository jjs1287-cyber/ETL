package com.etlforge.integration.server.implementation.webservice.bulkload;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsBulkLoadResult implements Serializable {
   private boolean allBatchesWereSent;
   private WsBulkLoadError error;
   private String logFile;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsBulkLoadResult.class, true);

   public WsBulkLoadResult() {
   }

   public WsBulkLoadResult(boolean var1, WsBulkLoadError var2, String var3) {
      this.allBatchesWereSent = var1;
      this.error = var2;
      this.logFile = var3;
   }

   public boolean isAllBatchesWereSent() {
      return this.allBatchesWereSent;
   }

   public void setAllBatchesWereSent(boolean var1) {
      this.allBatchesWereSent = var1;
   }

   public WsBulkLoadError getError() {
      return this.error;
   }

   public void setError(WsBulkLoadError var1) {
      this.error = var1;
   }

   public String getLogFile() {
      return this.logFile;
   }

   public void setLogFile(String var1) {
      this.logFile = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsBulkLoadResult)) {
         return false;
      }

      WsBulkLoadResult var2 = (WsBulkLoadResult)var1;
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
      boolean var3 = this.allBatchesWereSent == var2.isAllBatchesWereSent()
         && (this.error == null && var2.getError() == null || this.error != null && this.error.equals(var2.getError()))
         && (this.logFile == null && var2.getLogFile() == null || this.logFile != null && this.logFile.equals(var2.getLogFile()));
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
      var1 += (this.isAllBatchesWereSent() ? Boolean.TRUE : Boolean.FALSE).hashCode();
      if (this.getError() != null) {
         var1 += this.getError().hashCode();
      }

      if (this.getLogFile() != null) {
         var1 += this.getLogFile().hashCode();
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
      typeDesc.setXmlType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("allBatchesWereSent");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "allBatchesWereSent"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "boolean"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("error");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "error"));
      var0.setXmlType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadError"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("logFile");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "logFile"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
