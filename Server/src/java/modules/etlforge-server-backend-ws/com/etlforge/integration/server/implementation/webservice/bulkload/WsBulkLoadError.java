package com.etlforge.integration.server.implementation.webservice.bulkload;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsBulkLoadError implements Serializable {
   private int errorCode;
   private String error;
   private String warnings;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsBulkLoadError.class, true);

   public WsBulkLoadError() {
   }

   public WsBulkLoadError(int var1, String var2, String var3) {
      this.errorCode = var1;
      this.error = var2;
      this.warnings = var3;
   }

   public int getErrorCode() {
      return this.errorCode;
   }

   public void setErrorCode(int var1) {
      this.errorCode = var1;
   }

   public String getError() {
      return this.error;
   }

   public void setError(String var1) {
      this.error = var1;
   }

   public String getWarnings() {
      return this.warnings;
   }

   public void setWarnings(String var1) {
      this.warnings = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsBulkLoadError)) {
         return false;
      }

      WsBulkLoadError var2 = (WsBulkLoadError)var1;
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
      boolean var3 = this.errorCode == var2.getErrorCode()
         && (this.error == null && var2.getError() == null || this.error != null && this.error.equals(var2.getError()))
         && (this.warnings == null && var2.getWarnings() == null || this.warnings != null && this.warnings.equals(var2.getWarnings()));
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
      var1 += this.getErrorCode();
      if (this.getError() != null) {
         var1 += this.getError().hashCode();
      }

      if (this.getWarnings() != null) {
         var1 += this.getWarnings().hashCode();
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
      typeDesc.setXmlType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadError"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("errorCode");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "errorCode"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("error");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "error"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("warnings");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "warnings"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
