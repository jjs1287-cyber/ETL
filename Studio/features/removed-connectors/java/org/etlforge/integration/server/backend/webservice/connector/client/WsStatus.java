package org.etlforge.integration.server.backend.webservice.connector.client;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class WsStatus implements Serializable {
   private int statusCode;
   private String message;
   private Object __equalsCalc = null;
   private boolean __hashCodeCalc = false;
   private static TypeDesc typeDesc = new TypeDesc(WsStatus.class, true);

   public WsStatus() {
   }

   public WsStatus(int var1, String var2) {
      this.statusCode = var1;
      this.message = var2;
   }

   public int getStatusCode() {
      return this.statusCode;
   }

   public void setStatusCode(int var1) {
      this.statusCode = var1;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String var1) {
      this.message = var1;
   }

   @Override
   public synchronized boolean equals(Object var1) {
      if (!(var1 instanceof WsStatus)) {
         return false;
      }

      WsStatus var2 = (WsStatus)var1;
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
      boolean var3 = this.statusCode == var2.getStatusCode()
         && (this.message == null && var2.getMessage() == null || this.message != null && this.message.equals(var2.getMessage()));
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
      var1 += this.getStatusCode();
      if (this.getMessage() != null) {
         var1 += this.getMessage().hashCode();
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
      typeDesc.setXmlType(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "WsStatus"));
      ElementDesc var0 = new ElementDesc();
      var0.setFieldName("statusCode");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "StatusCode"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
      var0.setNillable(false);
      typeDesc.addFieldDesc(var0);
      var0 = new ElementDesc();
      var0.setFieldName("message");
      var0.setXmlName(new QName("http://connector.webservice.backend.server.integration.etlforge.invalid", "Message"));
      var0.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
      var0.setNillable(true);
      typeDesc.addFieldDesc(var0);
   }
}
