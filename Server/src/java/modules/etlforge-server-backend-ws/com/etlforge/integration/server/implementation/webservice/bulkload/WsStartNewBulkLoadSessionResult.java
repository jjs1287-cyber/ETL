package com.etlforge.integration.server.implementation.webservice.bulkload;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsStartNewBulkLoadSessionResult implements Serializable {
   private String sessionId;
   private WsBulkLoadError returnValue;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsStartNewBulkLoadSessionResult.class, true);

   public WsStartNewBulkLoadSessionResult() {
   }

   public WsStartNewBulkLoadSessionResult(String var1, WsBulkLoadError var2) {
      this.sessionId = var1;
      this.returnValue = var2;
   }

   public String getSessionId() {
      return this.sessionId;
   }

   public void setSessionId(String var1) {
      this.sessionId = var1;
   }

   public WsBulkLoadError getReturnValue() {
      return this.returnValue;
   }

   public void setReturnValue(WsBulkLoadError var1) {
      this.returnValue = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsStartNewBulkLoadSessionResult)) {
         return false;
      }

      WsStartNewBulkLoadSessionResult var2 = (WsStartNewBulkLoadSessionResult)var1;
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
      boolean var3 = (this.sessionId == null && var2.getSessionId() == null || this.sessionId != null && this.sessionId.equals(var2.getSessionId()))
         && (this.returnValue == null && var2.getReturnValue() == null || this.returnValue != null && this.returnValue.equals(var2.getReturnValue()));
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
      if (this.getSessionId() != null) {
         var1 += this.getSessionId().hashCode();
      }

      if (this.getReturnValue() != null) {
         var1 += this.getReturnValue().hashCode();
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
      typeDesc.setXmlType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsStartNewBulkLoadSessionResult"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("sessionId");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "sessionId"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("returnValue");
      var0.setXmlName(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "returnValue"));
      var0.setXmlType(new QName("http://bulkload.webservice.implementation.server.integration.etlforge.invalid", "WsBulkLoadError"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
